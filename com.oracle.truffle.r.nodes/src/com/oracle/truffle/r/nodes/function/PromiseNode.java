/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.function;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.SlowPath;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.source.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.*;
import com.oracle.truffle.r.nodes.access.ConstantNode.ConstantMissingNode;
import com.oracle.truffle.r.options.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.RPromise.*;
import com.oracle.truffle.r.runtime.env.frame.*;

/**
 * This {@link RNode} implementations are used as a factory-nodes for {@link RPromise}s OR direct
 * evaluation of these {@link RPromise}s, depending on the promises {@link EvalPolicy}.<br/>
 * All these classes are created during/after argument matching and get cached afterwards, so they
 * get (and need to get) called every repeated call to a function with the same arguments.
 */
public abstract class PromiseNode extends RNode {
    /**
     * The {@link RPromiseFactory} which holds all information necessary to construct a proper
     * {@link RPromise} for every case that might occur.
     */
    protected final RPromiseFactory factory;

    /**
     * @param factory {@link #factory}
     */
    protected PromiseNode(RPromiseFactory factory) {
        this.factory = factory;
    }

    /**
     * @param src The {@link SourceSection} of the argument for debugging purposes
     * @param factory {@link #factory}
     * @return Depending on {@link RPromiseFactory#getEvalPolicy()} and
     *         {@link RPromiseFactory#getType()} the proper {@link PromiseNode} implementation
     */
    @SlowPath
    public static PromiseNode create(SourceSection src, RPromiseFactory factory) {
        assert factory.getType() != PromiseType.NO_ARG;

        PromiseNode pn = null;
        switch (factory.getEvalPolicy()) {
            case INLINED:
                if (factory.getType() == PromiseType.ARG_SUPPLIED) {
                    pn = new InlinedSuppliedPromiseNode(factory);
                } else {
                    pn = new InlinedPromiseNode(factory);
                }
                break;

            case PROMISED:
                RNode expr = unfold(factory.getExpr());
                if (isConstantArgument(expr)) {
                    pn = new ConstantPromiseNode(factory);
                    break;
                }

                if (factory.getType() == PromiseType.ARG_SUPPLIED) {
                    if (isVariableArgument(expr)) {
                        pn = new VariableSuppliedPromiseNode(factory, ((ReadVariableNode) expr).getSymbol());
                    } else {
                        pn = new SuppliedPromiseNode(factory);
                    }
                } else {
                    // For ARG_DEFAULT, expr == defaultExpr!
                    if (isVariableArgument(expr) && false) {
                        pn = null;  // TODO
                    } else {
                        pn = new DefaultPromiseNode(factory);
                    }
                }
                break;

            default:
                throw new AssertionError();
        }

        pn.assignSourceSection(src);
        return pn;
    }

    private static RNode unfold(Object argObj) {
        RNode arg = (RNode) argObj;
        if (arg instanceof WrapArgumentNode) {
            return ((WrapArgumentNode) arg).getOperand();
        }
        return arg;
    }

    /**
     * This methods checks if an argument is a {@link ConstantNode}. Thanks to "..." unrolling, this
     * does not need to handle "..." as special case (which might result in a
     * {@link ConstantMissingNode} if empty).
     *
     * @param expr
     * @return Whether the given {@link RNode} is a {@link ConstantNode} (or a {@link ConstantNode}
     *         wrapped into a {@link WrapArgumentNode})
     * @see FastROptions#EagerEvalConstants
     */
    private static boolean isConstantArgument(RNode expr) {
        if (!FastROptions.EagerEvalConstants.getValue()) {
            return false;
        }
        return expr instanceof ConstantNode;
    }

    /**
     * This methods checks if an argument is a {@link ReadVariableNode}.
     *
     * @param expr
     * @return Whether the given {@link RNode} is a {@link ConstantNode} (or a {@link ConstantNode}
     *         wrapped into a {@link WrapArgumentNode})
     * @see FastROptions#EagerEvalConstants
     */
    private static boolean isVariableArgument(RNode expr) {
        if (!FastROptions.EagerEvalVariables.getValue()) {
            return false;
        }
        return expr instanceof ReadVariableNode;
    }

    /**
     * @param promise
     * @return Creates a {@link VarArgPromiseNode} for the given {@link RPromise}
     */
    public static VarArgPromiseNode createVarArg(RPromise promise) {
        VarArgPromiseNode result = new VarArgPromiseNode(promise);
        result.assignSourceSection(((RNode) promise.getRep()).getSourceSection());
        return result;
    }

    /**
     * A optimizing {@link PromiseNode}: It evaluates a constant directly.
     */
    private static final class ConstantPromiseNode extends PromiseNode {

        @Child private RNode constantExpr;
        @CompilationFinal private Object constant = null;

        private ConstantPromiseNode(RPromiseFactory factory) {
            super(factory);
            this.constantExpr = (RNode) factory.getExpr();
        }

        /**
         * Creates a new {@link RPromise} every time.
         */
        @Override
        public Object execute(VirtualFrame frame) {
            if (constant == null) {
                // Eval constant on first time
                CompilerDirectives.transferToInterpreterAndInvalidate();
                constant = constantExpr.execute(frame);
            }

            return factory.createArgEvaluated(constant);
        }
    }

    private static final class VariableSuppliedPromiseNode extends PromiseNode implements EagerFeedback {
        private final Symbol symbol;
        @Child private FrameSlotNode frameSlotNode;

        public VariableSuppliedPromiseNode(RPromiseFactory factory, Symbol symbol) {
            super(factory);
            this.symbol = symbol;
            this.frameSlotNode = FrameSlotNode.create(symbol.getName(), false);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            FrameSlot slot = frameSlotNode.executeFrameSlot(frame);

            // Check if we may apply eager evaluation on this frame slot
            Assumption notChangedNonLocally = FrameSlotChangeMonitor.getMonitor(slot);
            if (!notChangedNonLocally.isValid()) {
                // Cannot apply optimizations, as it (may) get invalidated!
                CompilerDirectives.transferToInterpreterAndInvalidate();
                return replace(new SuppliedPromiseNode(factory)).execute(frame);
            }

            // Execute eagerly
            RNode variableExpr = (RNode) factory.getExpr(); // TODO Handle RShareable and
            // WrapArgumentNode correctly!
            Object result = null;
            try {
                result = variableExpr.execute(frame);
            } catch (Throwable t) {
                // Errors are also side effects
                // TODO Create EagerErrorPromise
                throw RInternalError.unimplemented();
            }

            // Create EagerPromise with the eagerly evaluated value under the assumption that the
            // value won't be altered until 1. read
            return factory.createEagerSuppliedPromise(result, notChangedNonLocally, RArguments.getDepth(frame), this);
        }

        public void onFailure() {
            // TODO Auto-generated method stub

        }

        @SlowPath
        public void onSuccess() {
            // TODO Auto-generated method stub
            System.out.println("Successfully optimized read from '" + symbol.getName() + "'");
        }
    }

    /**
     * A {@link PromiseNode} for supplied arguments
     */
    private static final class SuppliedPromiseNode extends PromiseNode {

        private SuppliedPromiseNode(RPromiseFactory factory) {
            super(factory);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return factory.createPromise(frame.materialize());
        }
    }

    /**
     * A {@link PromiseNode} for defaulted arguments
     */
    private static final class DefaultPromiseNode extends PromiseNode {

        private DefaultPromiseNode(RPromiseFactory factory) {
            super(factory);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return factory.createPromise(null);
        }
    }

    /**
     * This class is meant for supplied arguments (which have to be evaluated in the caller frame)
     * which are supposed to be evaluated {@link EvalPolicy#INLINED}: This means we can simply
     * evaluate it here, and as it's {@link EvalPolicy#INLINED}, return its value and not the
     * {@link RPromise} itself! {@link EvalPolicy#INLINED} {@link PromiseType#ARG_SUPPLIED}
     */
    private final static class InlinedSuppliedPromiseNode extends PromiseNode {
        @Child private RNode expr;
        @Child private InlineCacheNode<VirtualFrame, RNode> promiseExpressionCache = InlineCacheNode.createExpression(3);
        private final PromiseProfile promiseProfile = new PromiseProfile();

        public InlinedSuppliedPromiseNode(RPromiseFactory factory) {
            super(factory);
            this.expr = (RNode) factory.getExpr();
        }

        @Override
        public Object execute(VirtualFrame frame) {
            // builtin.inline: We do re-evaluation every execute inside the caller frame, as we
            // know that the evaluation of default values has no side effects (has to be assured by
            // builtin implementations)
            Object obj = expr.execute(frame);
            if (obj == RMissing.instance) {
                if (factory.getDefaultExpr() == null) {
                    return RMissing.instance;
                }
                RPromise promise = factory.createPromiseDefault();
                return PromiseHelper.evaluate(frame, promiseExpressionCache, promise, promiseProfile);
            } else if (obj instanceof RArgsValuesAndNames) {
                return ((RArgsValuesAndNames) obj).evaluate(frame, promiseProfile);
            } else {
                return RPromise.checkEvaluate(frame, obj, promiseProfile);
            }
        }
    }

    /**
     * This class is meant for default arguments which have to be evaluated in the callee frame -
     * usually. But as this is for {@link EvalPolicy#INLINED}, arguments are simply evaluated inside
     * the caller frame: This means we can simply evaluate it here, and as it's
     * {@link EvalPolicy#INLINED}, return its value and not the {@link RPromise} itself!
     */
    private final static class InlinedPromiseNode extends PromiseNode {
        @Child private RNode defaultExpr;

        public InlinedPromiseNode(RPromiseFactory factory) {
            super(factory);
            // defaultExpr and expr are identical here!
            this.defaultExpr = (RNode) factory.getDefaultExpr();
        }

        @Override
        public Object execute(VirtualFrame frame) {
            // builtin.inline: We do re-evaluation every execute inside the caller frame, based on
            // the assumption that the evaluation of default values should have no side effects
            return defaultExpr.execute(frame);
        }
    }

    /**
     * This {@link RNode} is used to evaluate the expression given in a {@link RPromise} formerly
     * wrapped into a "..." after unrolling.
     */
    public final static class VarArgPromiseNode extends RNode {
        private final RPromise promise;
        @CompilationFinal private boolean isEvaluated = false;

        private final PromiseProfile promiseProfile = new PromiseProfile();

        private VarArgPromiseNode(RPromise promise) {
            this.promise = promise;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            // ExpressionExecutorNode would be overkill, as this is only executed once, and not in
            // the correct frame anyway
            if (!isEvaluated) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                Object result = promise.evaluate(frame, promiseProfile);
                isEvaluated = promise.isEvaluated(promiseProfile);
                return result;
            }

            // Should be easily compile to constant
            return promise.getValue();
        }

        public RPromise getPromise() {
            return promise;
        }
    }

    /**
     * @param src
     * @param evalPolicy {@link EvalPolicy}
     * @param nodes The argument {@link RNode}s that got wrapped into this "..."
     * @param names The argument's names
     * @param callSrc The {@link SourceSection} of the call this "..." belongs to
     * @return Creates either a {@link InlineVarArgsPromiseNode} or a {@link VarArgsPromiseNode},
     *         depending on the {@link EvalPolicy}
     */
    @SlowPath
    public static RNode createVarArgs(SourceSection src, EvalPolicy evalPolicy, RNode[] nodes, String[] names, ClosureCache closureCache, SourceSection callSrc) {
        RNode node;
        switch (evalPolicy) {
            case INLINED:
                node = new InlineVarArgsPromiseNode(nodes, names);
                break;

            case PROMISED:
                node = new VarArgsPromiseNode(nodes, names, closureCache);
                break;

            default:
                throw new AssertionError();
        }

        node.assignSourceSection(src);
        return node;
    }

    /**
     * This class is used for wrapping arguments into "..." ({@link RArgsValuesAndNames}).
     */
    private final static class VarArgsPromiseNode extends RNode {
        protected final RNode[] nodes;
        protected final String[] names;
        protected final ClosureCache closureCache;

        public VarArgsPromiseNode(RNode[] nodes, String[] names, ClosureCache closureCache) {
            this.nodes = nodes;
            this.names = names;
            this.closureCache = closureCache;
        }

        @Override
        @ExplodeLoop
        public Object execute(VirtualFrame frame) {
            Object[] promises = new Object[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                Closure closure = closureCache.getOrCreateClosure(nodes[i]);
                promises[i] = RPromise.create(EvalPolicy.PROMISED, PromiseType.ARG_SUPPLIED, frame.materialize(), closure);
            }
            return new RArgsValuesAndNames(promises, names);
        }
    }

    /**
     * The {@link EvalPolicy#INLINED} counterpart of {@link VarArgsPromiseNode}: This gets a bit
     * more complicated, as "..." might also values from an outer "...", which might resolve to an
     * empty argument list.
     */
    public final static class InlineVarArgsPromiseNode extends RNode {
        @Children private final RNode[] varargs;
        protected final String[] names;

        private final PromiseProfile promiseProfile = new PromiseProfile();

        public InlineVarArgsPromiseNode(RNode[] nodes, String[] names) {
            this.varargs = nodes;
            this.names = names;
        }

        @Override
        @ExplodeLoop
        public Object execute(VirtualFrame frame) {
            Object[] evaluatedArgs = new Object[varargs.length];
            String[] evaluatedNames = names;
            int index = 0;
            for (int i = 0; i < varargs.length; i++) {
                Object argValue = varargs[i].execute(frame);
                if (argValue instanceof RArgsValuesAndNames) {
                    // this can happen if ... is simply passed around (in particular when the call
                    // chain contains two functions with just the ... argument)
                    RArgsValuesAndNames argsValuesAndNames = (RArgsValuesAndNames) argValue;
                    int newLength = evaluatedArgs.length + argsValuesAndNames.length() - 1;
                    if (newLength == 0) {
                        // Corner case: "f <- function(...) g(...); g <- function(...)"
                        // In this case, "..." gets evaluated, and its only content is "...", which
                        // itself is missing. Result: Both disappear!
                        evaluatedArgs[index++] = RMissing.instance;
                        continue;
                    }
                    evaluatedArgs = Utils.resizeArray(evaluatedArgs, newLength);
                    evaluatedNames = Utils.resizeArray(evaluatedNames, newLength);
                    Object[] varargValues = argsValuesAndNames.getValues();
                    for (int j = 0; j < argsValuesAndNames.length(); j++) {
                        evaluatedArgs[index] = RPromise.checkEvaluate(frame, varargValues[j], promiseProfile);
                        evaluatedNames[index] = argsValuesAndNames.getNames()[j];
                        index++;
                    }
                } else {
                    evaluatedArgs[index++] = RPromise.checkEvaluate(frame, argValue, promiseProfile);
                }
            }
            return new RArgsValuesAndNames(evaluatedArgs, evaluatedNames);
        }
    }
}
