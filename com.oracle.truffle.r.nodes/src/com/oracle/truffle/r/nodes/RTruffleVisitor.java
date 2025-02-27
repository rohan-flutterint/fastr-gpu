/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.r.nodes.access.AccessArgumentNode;
import com.oracle.truffle.r.nodes.access.ConstantNode;
import com.oracle.truffle.r.nodes.access.ReadVariadicComponentNode;
import com.oracle.truffle.r.nodes.access.WriteLocalFrameVariableNode;
import com.oracle.truffle.r.nodes.access.WriteReplacementNode;
import com.oracle.truffle.r.nodes.access.WriteVariableNode;
import com.oracle.truffle.r.nodes.access.variables.ReadVariableNode;
import com.oracle.truffle.r.nodes.binary.ColonNode;
import com.oracle.truffle.r.nodes.control.BlockNode;
import com.oracle.truffle.r.nodes.control.BreakNode;
import com.oracle.truffle.r.nodes.control.ForNode;
import com.oracle.truffle.r.nodes.control.IfNode;
import com.oracle.truffle.r.nodes.control.NextNode;
import com.oracle.truffle.r.nodes.control.ReplacementNode;
import com.oracle.truffle.r.nodes.control.WhileNode;
import com.oracle.truffle.r.nodes.function.FormalArguments;
import com.oracle.truffle.r.nodes.function.FunctionBodyNode;
import com.oracle.truffle.r.nodes.function.FunctionDefinitionNode;
import com.oracle.truffle.r.nodes.function.FunctionExpressionNode;
import com.oracle.truffle.r.nodes.function.FunctionStatementsNode;
import com.oracle.truffle.r.nodes.function.GroupDispatchNode;
import com.oracle.truffle.r.nodes.function.PostProcessArgumentsNode;
import com.oracle.truffle.r.nodes.function.RCallNode;
import com.oracle.truffle.r.nodes.function.SaveArgumentsNode;
import com.oracle.truffle.r.nodes.function.WrapDefaultArgumentNode;
import com.oracle.truffle.r.parser.ast.ASTNode;
import com.oracle.truffle.r.parser.ast.AccessVariable;
import com.oracle.truffle.r.parser.ast.AccessVector;
import com.oracle.truffle.r.parser.ast.ArgNode;
import com.oracle.truffle.r.parser.ast.BinaryOperation;
import com.oracle.truffle.r.parser.ast.Break;
import com.oracle.truffle.r.parser.ast.Constant;
import com.oracle.truffle.r.parser.ast.Constant.ConstantType;
import com.oracle.truffle.r.parser.ast.FieldAccess;
import com.oracle.truffle.r.parser.ast.For;
import com.oracle.truffle.r.parser.ast.Formula;
import com.oracle.truffle.r.parser.ast.Function;
import com.oracle.truffle.r.parser.ast.FunctionCall;
import com.oracle.truffle.r.parser.ast.If;
import com.oracle.truffle.r.parser.ast.Missing;
import com.oracle.truffle.r.parser.ast.Next;
import com.oracle.truffle.r.parser.ast.Operation.Operator;
import com.oracle.truffle.r.parser.ast.Repeat;
import com.oracle.truffle.r.parser.ast.Replacement;
import com.oracle.truffle.r.parser.ast.Sequence;
import com.oracle.truffle.r.parser.ast.SimpleAccessTempVariable;
import com.oracle.truffle.r.parser.ast.SimpleAccessVariable;
import com.oracle.truffle.r.parser.ast.SimpleAccessVariadicComponent;
import com.oracle.truffle.r.parser.ast.SimpleAssignVariable;
import com.oracle.truffle.r.parser.ast.UnaryOperation;
import com.oracle.truffle.r.parser.ast.UpdateField;
import com.oracle.truffle.r.parser.ast.UpdateVector;
import com.oracle.truffle.r.parser.ast.While;
import com.oracle.truffle.r.parser.tools.BasicVisitor;
import com.oracle.truffle.r.parser.tools.EvaluatedArgumentsVisitor;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.FastROptions;
import com.oracle.truffle.r.runtime.RGroupGenerics;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.FastPathFactory;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.REmpty;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.env.frame.FrameSlotChangeMonitor;
import com.oracle.truffle.r.runtime.nodes.RNode;
import com.oracle.truffle.r.runtime.nodes.RSyntaxNode;

public final class RTruffleVisitor extends BasicVisitor<RSyntaxNode> {

    public RSyntaxNode transform(ASTNode ast) {
        return ast.accept(this);
    }

    public RFunction transformFunction(String name, Function func, MaterializedFrame enclosingFrame) {
        RootCallTarget callTarget = createFunctionCallTarget(func);
        FastPathFactory fastPath = EvaluatedArgumentsVisitor.process(func);
        return RDataFactory.createFunction(name, callTarget, null, enclosingFrame, fastPath, ((FunctionDefinitionNode) callTarget.getRootNode()).containsDispatch());
    }

    @Override
    public RSyntaxNode visit(Constant c) {
        SourceSection src = c.getSource();
        if (c.getType() == ConstantType.NULL) {
            return ConstantNode.create(src, RNull.instance);
        }
        if (c.getValues().length != 1) {
            throw new UnsupportedOperationException();
        }
        switch (c.getType()) {
            case INT:
                return ConstantNode.create(src, RRuntime.string2int(c.getValues()[0]));
            case DOUBLE:
                return ConstantNode.create(src, RRuntime.string2double(c.getValues()[0]));
            case BOOL:
                switch (c.getValues()[0]) {
                    case "NA":
                        return ConstantNode.create(src, RRuntime.LOGICAL_NA);
                    case "1":
                        return ConstantNode.create(src, RRuntime.LOGICAL_TRUE);
                    case "0":
                        return ConstantNode.create(src, RRuntime.LOGICAL_FALSE);
                    default:
                        throw new AssertionError();
                }
            case STRING:
                return ConstantNode.create(src, c.getValues()[0]);
            case COMPLEX:
                if (c.getValues()[0].equals("NA_complex_")) {
                    return ConstantNode.create(src, RDataFactory.createComplex(RRuntime.COMPLEX_NA_REAL_PART, RRuntime.COMPLEX_NA_IMAGINARY_PART));
                } else {
                    return ConstantNode.create(src, RDataFactory.createComplex(0, RRuntime.string2double(c.getValues()[0])));
                }
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public RSyntaxNode visit(Formula formula) {
        // response may be omitted
        RSyntaxNode response = formula.getResponse() == null ? null : formula.getResponse().accept(this);
        RSyntaxNode model = formula.getModel().accept(this);
        RSyntaxNode[] tildeArgs = new RSyntaxNode[response == null ? 1 : 2];
        int ix = 0;
        if (response != null) {
            tildeArgs[ix++] = response;
        }
        tildeArgs[ix++] = model;
        SourceSection formulaSrc = formula.getSource();
        String formulaCode = formulaSrc.getCode();
        int tildeIndex = formulaCode.indexOf('~');
        SourceSection tildeSrc = ASTNode.adjustedSource(formulaSrc, formulaSrc.getCharIndex() + tildeIndex, 1);
        RCallNode call = RCallNode.createOpCall(formulaSrc, tildeSrc, "~", tildeArgs);
        return call;
    }

    @Override
    public RSyntaxNode visit(Missing m) {
        return ConstantNode.create(REmpty.instance);
    }

    @Override
    public RSyntaxNode visit(FunctionCall call) {
        SourceSection callSource = call.getSource();
        List<ArgNode> arguments = call.getArguments();

        String[] argumentNames = arguments.stream().map(arg -> arg.getName()).toArray(String[]::new);
        ArgumentsSignature signature = ArgumentsSignature.get(argumentNames);
        RSyntaxNode[] nodes = arguments.stream().map(arg -> arg.getValue() == null ? null : arg.getValue().accept(this)).toArray(RSyntaxNode[]::new);

        RNode lhs;
        if (call.isSymbol()) {
            String callName = call.getName();
            if (RGroupGenerics.isGroupGeneric(callName)) {
                return GroupDispatchNode.create(callName, callSource, signature, nodes);
            }
            SourceSection varSource = ASTNode.adjustedSource(callSource, callSource.getCharIndex(), callName.length());
            lhs = ReadVariableNode.createForced(varSource, callName, RType.Function);
        } else {
            lhs = call.getLhsNode().accept(this).asRNode();
        }
        return RCallNode.createCall(callSource, lhs, signature, nodes);
    }

    @Override
    public RSyntaxNode visit(Function func) {
        RootCallTarget callTarget = null;
        try {
            callTarget = createFunctionCallTarget(func);
            FastPathFactory fastPath = EvaluatedArgumentsVisitor.process(func);
            return FunctionExpressionNode.create(func.getSource(), callTarget, fastPath);
        } catch (Throwable err) {
            throw new RInternalError(err, "visit(Function)");
        }
    }

    private RootCallTarget createFunctionCallTarget(Function func) {
        // Parse function statements
        ASTNode astBody = func.getBody();
        FunctionStatementsNode statements;
        if (astBody != null) {
            statements = new FunctionStatementsNode(astBody.getSource(), astBody.accept(this));
        } else {
            statements = new FunctionStatementsNode();
        }

        // Parse argument list
        List<ArgNode> argumentsList = func.getSignature();
        String[] argumentNames = new String[argumentsList.size()];
        RNode[] defaultValues = new RNode[argumentsList.size()];
        SaveArgumentsNode saveArguments;
        AccessArgumentNode[] argAccessNodes = new AccessArgumentNode[argumentsList.size()];
        PostProcessArgumentsNode argPostProcess;
        if (!argumentsList.isEmpty()) {
            RNode[] init = new RNode[argumentsList.size()];
            int index = 0;
            for (ArgNode arg : argumentsList) {
                // Parse argument's default value
                RNode defaultValue;
                ASTNode defaultValNode = arg.getValue();
                if (defaultValNode != null) {
                    // default argument initialization is, in a sense, quite similar to local
                    // variable write and thus should do appropriate state transition and/or
                    // RShareable copy if need be
                    defaultValue = WrapDefaultArgumentNode.create(arg.getValue().accept(this).asRNode());
                } else {
                    defaultValue = null;
                }

                // Create an initialization statement
                AccessArgumentNode accessArg = AccessArgumentNode.create(index);
                argAccessNodes[index] = accessArg;
                init[index] = WriteVariableNode.createArgSave(arg.getName(), accessArg);

                // Store formal arguments
                argumentNames[index] = arg.getName();
                defaultValues[index] = defaultValue;

                index++;
            }

            saveArguments = new SaveArgumentsNode(init);
            if (FastROptions.NewStateTransition.getBooleanValue() && !FastROptions.RefCountIncrementOnly.getBooleanValue()) {
                argPostProcess = PostProcessArgumentsNode.create(argumentsList.size());
            } else {
                argPostProcess = null;
            }
        } else {
            saveArguments = new SaveArgumentsNode(RNode.EMTPY_RNODE_ARRAY);
            argPostProcess = null;
        }

        // Maintain SourceSection
        if (astBody != null && statements.getSourceSection() == null) {
            statements.assignSourceSection(astBody.getSource());
        }
        FormalArguments formals = FormalArguments.createForFunction(defaultValues, ArgumentsSignature.get(argumentNames));
        for (AccessArgumentNode access : argAccessNodes) {
            access.setFormals(formals);
        }

        FrameDescriptor descriptor = new FrameDescriptor();
        FrameSlotChangeMonitor.initializeFunctionFrameDescriptor(descriptor);
        String description = getFunctionDescription(func);
        FunctionDefinitionNode rootNode = new FunctionDefinitionNode(func.getSource(), descriptor, new FunctionBodyNode(saveArguments, statements), formals, description, false, argPostProcess);
        return Truffle.getRuntime().createCallTarget(rootNode);
    }

    private static String getFunctionDescription(Function func) {
        if (func.getDebugName() != null) {
            return func.getDebugName();
        } else {
            String functionBody = func.getSource().getCode();
            return functionBody.substring(0, Math.min(functionBody.length(), 40)).replace("\n", "\\n");
        }
    }

    @Override
    public RSyntaxNode visit(UnaryOperation op) {
        String functionName = op.getOperator().getName();
        assert RGroupGenerics.getGroup(functionName) == RGroupGenerics.Ops : "unexpected group: " + RGroupGenerics.getGroup(functionName) + "(" + functionName + ")";

        RSyntaxNode operand = op.getLHS().accept(this);
        return GroupDispatchNode.create(functionName, op.getSource(), ArgumentsSignature.empty(1), operand);
    }

    @Override
    public RSyntaxNode visit(BinaryOperation op) {
        RSyntaxNode left = op.getLHS().accept(this);
        RSyntaxNode right = op.getRHS().accept(this);
        if (op.getOperator() == Operator.COLON) {
            return ColonNode.create(op.getSource(), left.asRNode(), right.asRNode());
        } else {
            String functionName = op.getOperator().getName();
            if (RGroupGenerics.isGroupGeneric(functionName)) {
                return GroupDispatchNode.create(functionName, op.getSource(), ArgumentsSignature.empty(2), left, right);
            }
            // create a SourceSection for the operator
            SourceSection opSrc = op.getSource();
            String code = opSrc.getCode();
            String opName = op.getOperator().getName();
            int charIndex = code.indexOf(opName);
            SourceSection opNameSrc = opSrc.getSource().createSection(opSrc.getIdentifier(), opSrc.getCharIndex() + charIndex, opName.length());
            return RCallNode.createOpCall(op.getSource(), opNameSrc, functionName, left, right);
        }
    }

    @Override
    public RSyntaxNode visit(Sequence seq) {
        ASTNode[] exprs = seq.getExpressions();
        RNode[] rexprs = new RNode[exprs.length];
        for (int i = 0; i < exprs.length; i++) {
            rexprs[i] = exprs[i].accept(this).asRNode();
        }
        return new BlockNode(seq.getSource(), rexprs);
    }

    @Override
    public RSyntaxNode visit(ASTNode n) {
        throw new UnsupportedOperationException("Unsupported AST Node " + n.getClass().getName());
    }

    @Override
    public RSyntaxNode visit(ArgNode n) {
        assert n.getValue() != null;
        return n.getValue().accept(this);
    }

    private RCallNode createArrayUpdate(List<ArgNode> argList, int argLength, boolean isSubset, RSyntaxNode vector, RSyntaxNode rhs) {
        RSyntaxNode[] nodes = new RSyntaxNode[Math.max(1, argLength) + 2];
        String[] names = new String[nodes.length];
        nodes[0] = vector;
        if (argLength == 0) {
            nodes[1] = ConstantNode.create(REmpty.instance);
        } else {
            for (int i = 0; i < argLength; i++) {
                ArgNode node = argList.get(i);
                if (node.getName() != null) {
                    names[i + 1] = node.getName();
                }
                nodes[i + 1] = node == null ? ConstantNode.create(REmpty.instance) : node.getValue().accept(this);
            }
        }
        nodes[nodes.length - 1] = rhs;
        names[nodes.length - 1] = "value";

        return RCallNode.createCallNotSyntax(ReadVariableNode.createForced(null, isSubset ? "[<-" : "[[<-", RType.Function), ArgumentsSignature.get(names), nodes);
    }

    private int tempNamesCount;

    private void resetTempNames() {
        tempNamesCount = 0;
    }

    private String createTempName() {
        //@formatter:off
        // store a - need to use temporary, otherwise there is a failure in case multiple calls to
        // the replacement form are chained:
        // x<-c(1); y<-c(1); dim(x)<-1; dim(y)<-1; attr(x, "dimnames")<-(attr(y, "dimnames")<-list("b"))
        //@formatter:on
        return "*tmp" + tempNamesCount++ + "*";
    }

    private static ReplacementNode constructReplacementSuffix(RSyntaxNode rhs, RSyntaxNode v, boolean copyRhs, RNode assignFromTemp, String tmpSymbol, String rhsSymbol, SourceSection source) {
        return new ReplacementNode(source, rhs.asRNode(), v.asRNode(), copyRhs, assignFromTemp, tmpSymbol, rhsSymbol);
    }

    @Override
    public RSyntaxNode visit(UpdateVector u) {
        resetTempNames();
        AccessVector a = u.getVector();
        int argLength = a.getIndexes().size() - 1;
        // If recursive no need to set syntaxAST as already handled at top-level
        return doReplacementLeftHandSide(a.getVector(), true, u.getRHS().accept(this), u.isSuper(), u.getSource(), (receiver, rhsAccess) -> {
            return createArrayUpdate(a.getIndexes(), argLength, a.isSubset(), receiver, rhsAccess);
        });
    }

    @Override
    public RSyntaxNode visit(SimpleAssignVariable n) {
        if (n.getExpr() instanceof Function) {
            ((Function) n.getExpr()).setDebugName(n.getVariable().toString());
        }
        RSyntaxNode expression = n.getExpr().accept(this);
        return (RSyntaxNode) WriteVariableNode.create(n.getSource(), n.getVariable(), expression.asRNode(), n.isSuper());
    }

    private RCallNode prepareReplacementCall(FunctionCall f, List<ArgNode> args, String tmpSymbol, String rhsSymbol, boolean simpleReplacement) {
        // massage arguments to replacement function call (replace v with tmp, append a)
        List<ArgNode> rfArgs = new ArrayList<>();
        rfArgs.add(ArgNode.create(null, null, AccessVariable.create(null, tmpSymbol, false)));
        if (args.size() > 1) {
            for (int i = 1; i < args.size(); i++) {
                rfArgs.add(args.get(i));
            }
        }
        rfArgs.add(ArgNode.create(null, null, AccessVariable.create(null, rhsSymbol)));

        // replacement function call (use visitor for FunctionCall)
        FunctionCall rfCall = new FunctionCall(f.getSource(), f.getName(), rfArgs, simpleReplacement);
        return (RCallNode) visit(rfCall);
    }

    //@formatter:off
    /**
     * Handle an assignment of the form {@code xxx(v) <- a} (or similar, with additional arguments).
     * These are called "replacements".
     *
     * According to the R language specification, this corresponds to the following code:
     * <pre>
     * '*tmp*' <- v
     * v <- `xxx<-`('*tmp*', a)
     * rm('*tmp*')
     * </pre>
     *
     * We take an anonymous object to store a, as the anonymous object is unique to this
     * replacement. This value must be stored as it is the result of the entire replacement expression.
     */
    //@formatter:on
    @Override
    public RSyntaxNode visit(Replacement replacement) {
        resetTempNames();
        // preparations
        ASTNode rhsAst = replacement.getExpr();
        RSyntaxNode rhs = rhsAst.accept(this);
        FunctionCall fAst = replacement.getReplacementFunctionCall();
        // fAst has the function name as "x<-" already; we don't want that in the syntaxAST
        fAst.tempSuppressReplacementSuffix(true);
        RNode f = fAst.accept(this).asRNode();
        fAst.tempSuppressReplacementSuffix(false);
        RSyntaxNode syntaxAST = new WriteReplacementNode((RCallNode) f, rhs);
        List<ArgNode> args = fAst.getArguments();
        ASTNode val = args.get(0).getValue();
        String tmpSymbol = createTempName();
        RNode assignFromTemp;
        RSyntaxNode replacementArg;
        String rhsSymbol = createTempName();

        if (val instanceof SimpleAccessVariable) {
            SimpleAccessVariable callArg = (SimpleAccessVariable) val;
            String vSymbol = callArg.getVariable();
            replacementArg = createReplacementForVariableUsing(callArg, vSymbol, replacement.isSuper());
            RCallNode replacementCall = prepareReplacementCall(fAst, args, tmpSymbol, rhsSymbol, true);
            assignFromTemp = WriteLocalFrameVariableNode.createAnonymous(vSymbol, replacementCall, WriteVariableNode.Mode.INVISIBLE, replacement.isSuper());
        } else if (val instanceof AccessVector) {
            AccessVector callArgAst = (AccessVector) val;
            replacementArg = callArgAst.accept(this);
            RCallNode replacementCall = prepareReplacementCall(fAst, args, tmpSymbol, rhsSymbol, false);
            // see AssignVariable.writeVector (number of args must match)
            callArgAst.getArguments().add(ArgNode.create(rhsAst.getSource(), "value", rhsAst));
            RSyntaxNode update = doReplacementLeftHandSide(callArgAst.getVector(), true, replacementCall, replacement.isSuper(), replacement.getSource(), (receiver, rhsAccess) -> {
                return createArrayUpdate(callArgAst.getIndexes(), callArgAst.getIndexes().size() - 1, callArgAst.isSubset(), receiver, rhsAccess);
            });
            assignFromTemp = update.asRNode();
        } else if (val instanceof FunctionCall) {
            FunctionCall callArgAst = (FunctionCall) val;
            replacementArg = callArgAst.accept(this);
            RCallNode replacementCall = prepareReplacementCall(fAst, args, tmpSymbol, rhsSymbol, false);
            assignFromTemp = doReplacementLeftHandSide(callArgAst.getArguments().get(0).getValue(), true, replacementCall, replacement.isSuper(), replacement.getSource(), (receiver, rhsAccess) -> {
                return createFunctionUpdate(rhsAccess, callArgAst);
            }).asRNode();
        } else {
            FieldAccess callArgAst = (FieldAccess) val;
            replacementArg = callArgAst.accept(this);
            RCallNode replacementCall = prepareReplacementCall(fAst, args, tmpSymbol, rhsSymbol, false);
            assignFromTemp = doReplacementLeftHandSide(callArgAst.getLhs(), true, replacementCall, replacement.isSuper(), replacement.getSource(), (receiver, rhsAccess) -> {
                return createFieldUpdate(null, receiver, rhsAccess, callArgAst.getFieldName(), callArgAst.isAt());
            }).asRNode();
        }
        RSyntaxNode result = constructReplacementSuffix(rhs, replacementArg, true, assignFromTemp, tmpSymbol, rhsSymbol, replacement.getSource());
        ((ReplacementNode) result).setSyntaxAST(syntaxAST);
        return result;
    }

    private static ReadVariableNode createReplacementForVariableUsing(SimpleAccessVariable simpleAccessVariable, String variableSymbol, boolean isSuper) {
        SourceSection argSourceSection = simpleAccessVariable.getSource();
        if (isSuper) {
            return ReadVariableNode.createSuperLookup(argSourceSection, variableSymbol);
        } else {
            return ReadVariableNode.create(argSourceSection, variableSymbol, simpleAccessVariable.shouldCopyValue());
        }
    }

    @Override
    public RSyntaxNode visit(SimpleAccessVariable n) {
        return ReadVariableNode.create(n.getSource(), n.getVariable(), n.shouldCopyValue());
    }

    @Override
    public RSyntaxNode visit(SimpleAccessTempVariable n) {
        return ReadVariableNode.create(n.getSource(), n.getSymbol(), false);
    }

    @Override
    public RSyntaxNode visit(SimpleAccessVariadicComponent n) {
        int ind = n.getIndex();
        return new ReadVariadicComponentNode(n.getSource(), ind > 0 ? ind - 1 : ind);
    }

    @Override
    public RSyntaxNode visit(If n) {
        RSyntaxNode condition = n.getCondition().accept(this);
        RSyntaxNode thenPart = n.getTrueCase().accept(this);
        RSyntaxNode elsePart = n.getFalseCase() != null ? n.getFalseCase().accept(this) : null;
        return IfNode.create(n.getSource(), condition, BlockNode.ensureBlock(n.getTrueCase().getSource(), thenPart),
                        BlockNode.ensureBlock(n.getFalseCase() == null ? null : n.getFalseCase().getSource(), elsePart));
    }

    @Override
    public RSyntaxNode visit(While loop) {
        RSyntaxNode condition = loop.getCondition().accept(this);
        RSyntaxNode body = BlockNode.ensureBlock(loop.getBody().getSource(), loop.getBody().accept(this));
        return matchSources(WhileNode.create(condition, body, false), loop);
    }

    @Override
    public RSyntaxNode visit(Break n) {
        return new BreakNode(n.getSource());
    }

    @Override
    public RSyntaxNode visit(Next n) {
        return new NextNode(n.getSource());
    }

    @Override
    public RSyntaxNode visit(Repeat loop) {
        RSyntaxNode body = BlockNode.ensureBlock(loop.getBody().getSource(), loop.getBody().accept(this));
        return matchSources(WhileNode.create(ConstantNode.create(RRuntime.LOGICAL_TRUE), body, true), loop);
    }

    private static RSyntaxNode matchSources(RSyntaxNode truffleNode, ASTNode astNode) {
        truffleNode.asRNode().assignSourceSection(astNode.getSource());
        return truffleNode;
    }

    @Override
    public RSyntaxNode visit(For loop) {
        WriteVariableNode cvar = WriteVariableNode.create(loop.getSource(), loop.getVariable(), null, false);
        RSyntaxNode range = loop.getRange().accept(this);
        RSyntaxNode body = loop.getBody().accept(this);
        return matchSources(ForNode.create(cvar, range, new BlockNode(loop.getBody().getSource(), body)), loop);
    }

    @Override
    public RSyntaxNode visit(FieldAccess access) {
        SourceSection callSource = access.getSource();
        RSyntaxNode lhs = access.getLhs().accept(this);
        ReadVariableNode function = ReadVariableNode.createForced(callSource, access.isAt() ? "@" : "$", RType.Function);
        return RCallNode.createCall(callSource, function, ArgumentsSignature.empty(2), lhs, ConstantNode.create(callSource, access.getFieldName()));
    }

    private static RCallNode createFieldUpdate(SourceSection source, RSyntaxNode receiver, RSyntaxNode rhs, String fieldName, boolean at) {
        ReadVariableNode function = ReadVariableNode.createForced(source, at ? "@<-" : "$<-", RType.Function);
        return RCallNode.createCall(source, function, ArgumentsSignature.empty(3), receiver, ConstantNode.create(source, fieldName), rhs);
    }

    private RCallNode createFunctionUpdate(RSyntaxNode rhs, FunctionCall fun) {
        String funName = null;
        if (fun.isSymbol()) {
            funName = fun.getName() + "<-";
        } else {
            throw RInternalError.unimplemented();
        }
        List<ArgNode> arguments = fun.getArguments();
        String[] names = new String[arguments.size() + 1];
        RSyntaxNode[] argNodes = new RSyntaxNode[arguments.size() + 1];
        for (int i = 0; i < arguments.size(); i++) {
            names[i] = arguments.get(i).getName();
            argNodes[i] = visit(arguments.get(i));
        }
        argNodes[arguments.size()] = rhs;
        ReadVariableNode function = ReadVariableNode.createForced(null, funName, RType.Function);
        return RCallNode.createCall(null, function, ArgumentsSignature.get(names), argNodes);
    }

    private RSyntaxNode doReplacementLeftHandSide(ASTNode receiver, boolean needsSyntaxAST, RSyntaxNode rhs, boolean isSuper, SourceSection source,
                    BiFunction<RSyntaxNode, RSyntaxNode, RCallNode> updateFunction) {
        if (receiver.getClass() == FunctionCall.class) {
            return updateFunction.apply(receiver.accept(this), rhs);
        } else {
            RSyntaxNode result;
            if (receiver instanceof SimpleAccessVariable) {
                SimpleAccessVariable varAST = (SimpleAccessVariable) receiver;
                String vSymbol = varAST.getVariable();
                ReadVariableNode v = createReplacementForVariableUsing(varAST, vSymbol, isSuper);

                String tmpSymbol = createTempName();
                String rhsSymbol = createTempName();
                ReadVariableNode rhsAccess = ReadVariableNode.createAnonymous(rhsSymbol);
                ReadVariableNode tmpVarAccess = ReadVariableNode.createAnonymous(tmpSymbol);

                RSyntaxNode updateOp = updateFunction.apply(tmpVarAccess, rhsAccess);
                RNode assignFromTemp = WriteVariableNode.createAnonymous(vSymbol, updateOp.asRNode(), WriteVariableNode.Mode.INVISIBLE, isSuper);
                result = constructReplacementSuffix(rhs, v, false, assignFromTemp, tmpSymbol, rhsSymbol, source);
            } else if (receiver instanceof AccessVector) {
                AccessVector vecAST = (AccessVector) receiver;
                RCallNode updateOp = updateFunction.apply(vecAST.accept(this), rhs);
                checkAssignSourceSection(updateOp, source);
                result = doReplacementLeftHandSide(vecAST.getVector(), false, updateOp, isSuper, source, (receiver1, rhsAccess1) -> {
                    return createArrayUpdate(vecAST.getIndexes(), vecAST.getIndexes().size(), vecAST.isSubset(), receiver1, rhsAccess1);
                });
            } else if (receiver instanceof FieldAccess) {
                FieldAccess accessAST = (FieldAccess) receiver;
                RCallNode updateOp = updateFunction.apply(accessAST.accept(this), rhs);
                checkAssignSourceSection(updateOp, source);
                result = doReplacementLeftHandSide(accessAST.getLhs(), false, updateOp, isSuper, source, (receiver1, rhsAccess1) -> {
                    return createFieldUpdate(null, receiver1, rhsAccess1, accessAST.getFieldName(), accessAST.isAt());
                });
            } else {
                throw RInternalError.unimplemented();
            }
            if (needsSyntaxAST && result instanceof ReplacementNode) {
                RCallNode update = updateFunction.apply(receiver.accept(this), rhs);
                RSyntaxNode syntaxAST = update;
                checkAssignSourceSection(syntaxAST, source);
                ((ReplacementNode) result).setSyntaxAST(syntaxAST);
            }
            return result;
        }
    }

    /**
     * In some cases syntaxAST has acquired a SourceSection during the pre-processing, so we need to
     * clear it before assigning the "syntactic" source section.
     */
    private static void checkAssignSourceSection(RSyntaxNode node, SourceSection source) {
        node.asRNode().clearSourceSection();
        node.asRNode().assignSourceSection(source);
    }

    @Override
    public RSyntaxNode visit(UpdateField u) {
        resetTempNames();
        FieldAccess a = u.getVector();
        RSyntaxNode rhs = u.getRHS().accept(this);
        return doReplacementLeftHandSide(a.getLhs(), true, rhs, u.isSuper(), u.getSource(), (receiver, rhsAccess) -> {
            return createFieldUpdate(u.getSource(), receiver, rhsAccess, a.getFieldName(), a.isAt());
        });
    }

}
