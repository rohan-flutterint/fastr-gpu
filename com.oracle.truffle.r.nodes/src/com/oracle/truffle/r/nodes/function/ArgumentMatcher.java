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
package com.oracle.truffle.r.nodes.function;

import java.util.*;
import java.util.function.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.source.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.RPromise.Closure;
import com.oracle.truffle.r.runtime.data.RPromise.EvalPolicy;
import com.oracle.truffle.r.runtime.data.RPromise.PromiseType;
import com.oracle.truffle.r.runtime.data.RPromise.RPromiseFactory;

/**
 * <p>
 * {@link ArgumentMatcher} serves the purpose of matching {@link CallArgumentsNode} to
 * {@link FormalArguments} of a specific function, see
 * {@link #matchArguments(RFunction, UnmatchedArguments, SourceSection, SourceSection, boolean)} .
 * The other match functions are used for special cases, where builtins make it necessary to
 * re-match parameters, e.g.:
 * {@link #matchArgumentsEvaluated(VirtualFrame, RFunction, EvaluatedArguments, SourceSection, PromiseHelperNode, boolean)}
 * for 'UseMethod' and
 * {@link #matchArgumentsInlined(RFunction, UnmatchedArguments, SourceSection, SourceSection)} for
 * builtins which are implemented in Java ( @see {@link RBuiltinNode#inline(InlinedArguments)}
 * </p>
 *
 * <p>
 * Here are some details on how the argument processing and matching works. The formal arguments
 * list is constructed at the point when the {@link FunctionDefinitionNode} object is constructed.
 * The supplied (actual) arguments list is constructed at the point when the
 * {@link CallArgumentsNode} is constructed. At the point of executing the actual function call, the
 * matching procedure takes a list of formal arguments and a list of supplied (actual) arguments
 * (which actually has to be constructed by flattening "...", as it obviously may contain more then
 * one argument, all of which might have a name, see below) and applies the actual matching
 * algorithm taking into consideration the names and positions of arguments as well as their number.
 * After that, the resulting arguments (potentially reordered and eventually wrapped into "...") are
 * wrapped into additional {@link PromiseNode}s which are basically an abstraction layer for normal
 * and {@link EvalPolicy#INLINED} functions.<br/>
 * The resulting {@link RNode} are cached inside {@link RCallNode} and executed every call (the
 * cache is not invalidated): Depending on whether the function to be called is a normal or
 * {@link EvalPolicy#INLINED} function, or a separate argument needs special treatment, the
 * {@link PromiseNode} returns either a {@link RPromise} OR the directly evaluated value.<br/>
 * The final step of the function call execution is packaging of the resulting values
 * {@code Object[]} into an {@link RArguments} object that is stored in the callee's frame.
 * </p>
 *
 * <p>
 * One caveat here is related to the S3 dispatch procedure. In this case, we have in fact two
 * function calls, one to the "dispatch" function (the one containing the UseMethod call) and one to
 * the function that is ultimately selected. Both functions can have a different list of formal
 * arguments and may require running a separate argument matching procedure. For example, in the
 * following piece of R code, the name of argument b must be available when executing the call to
 * g() for proper argument reordering:
 *
 * f<-function(a,b) { UseMethod("f") }; f.numeric<-function(b,a) { a - b }; f(b=1,2)
 *
 * Consequently, argument names passed to the "dispatch" function are preserved as part of the
 * {@link RArguments} object and made this way available when executing the selected function.
 * </p>
 *
 * <p>
 * Another caveat is related to matching arguments for variadic functions (functions containing the
 * ... argument). On the caller's side, multiple supplied arguments (with their own names) can be
 * encapsulated as a single formal ... argument on the callee's side. In this case, however, R still
 * requires that the names of arguments encapsulated as ... are available to the callee for use in
 * the argument matching procedures down the call chain. For example, in the following piece of R
 * code, argument b is encapsulated as ... when executing the call to f() and yet its name has to be
 * available when executing the call to g() for proper argument reordering:
 *
 * f <- function(...) g(...); g <- function(a,b) { a - b }; f(b=1,2)
 *
 * Consequently, "non-executed" ... arguments are represented as VarArgsNodes (inheriting from
 * {@link RNode}) and "executed" .. arguments are represented as a language level value of type
 * {@link RArgsValuesAndNames}, which can be passes directly in the {@link RArguments} object and
 * whose type is understood by the language's builtins (both representations are name-preserving).
 * </p>
 */
public class ArgumentMatcher {

    /**
     * Match arguments supplied for a specific function call to the formal arguments and wraps them
     * in {@link PromiseNode}s. Used for calls to all functions parsed from R code
     *
     * @param function The function which is to be called
     * @param suppliedArgs The arguments supplied to the call
     * @param callSrc The source of the function call currently executed
     * @param argsSrc The source code encapsulating the arguments, for debugging purposes
     *
     * @return A fresh {@link MatchedArguments} containing the arguments in correct order and
     *         wrapped in {@link PromiseNode}s
     * @see #matchNodes(RFunction, RNode[], String[], SourceSection, SourceSection, boolean,
     *      ClosureCache, boolean)
     */
    public static MatchedArguments matchArguments(RFunction function, UnmatchedArguments suppliedArgs, SourceSection callSrc, SourceSection argsSrc, boolean noOpt) {
        RNode[] wrappedArgs = matchNodes(function, suppliedArgs.getArguments(), suppliedArgs.getNames(), callSrc, argsSrc, false, suppliedArgs, noOpt);
        FormalArguments formals = ((RRootNode) function.getTarget().getRootNode()).getFormalArguments();
        return MatchedArguments.create(wrappedArgs, formals.getNames());
    }

    /**
     * Match arguments supplied for a specific function call to the formal arguments and wraps them
     * in special {@link PromiseNode}s. Used for calls to builtins which are built into FastR and
     * thus are implemented in Java
     *
     * @param function The function which is to be called
     * @param suppliedArgs The arguments supplied to the call
     * @param callSrc The source of the function call currently executed
     * @param argsSrc The source code encapsulating the arguments, for debugging purposes
     *
     * @return A fresh {@link InlinedArguments} containing the arguments in correct order and
     *         wrapped in special {@link PromiseNode}s
     * @see #matchNodes(RFunction, RNode[], String[], SourceSection, SourceSection, boolean,
     *      ClosureCache, boolean)
     */
    public static InlinedArguments matchArgumentsInlined(RFunction function, UnmatchedArguments suppliedArgs, SourceSection callSrc, SourceSection argsSrc) {
        RNode[] wrappedArgs = matchNodes(function, suppliedArgs.getArguments(), suppliedArgs.getNames(), callSrc, argsSrc, true, suppliedArgs, false);
        return new InlinedArguments(wrappedArgs, suppliedArgs.getNames());
    }

    /**
     * Used for the implementation of the 'UseMethod' builtin. Reorders the arguments passed into
     * the called, generic function and prepares them to be passed into the specific function
     *
     * @param frame Needed for eventual promise reduction
     * @param function The 'Method' which is going to be 'Use'd
     * @param evaluatedArgs The arguments which are already in evaluated form (as they are directly
     *            taken from the stack)
     * @param callSrc The source code of the call
     * @param forNextMethod matching when evaluating NextMethod
     *
     * @return A Fresh {@link EvaluatedArguments} containing the arguments rearranged and stuffed
     *         with default values (in the form of {@link RPromise}s where needed)
     */
    public static EvaluatedArguments matchArgumentsEvaluated(VirtualFrame frame, RFunction function, EvaluatedArguments evaluatedArgs, SourceSection callSrc, PromiseHelperNode promiseHelper,
                    boolean forNextMethod) {
        RRootNode rootNode = (RRootNode) function.getTarget().getRootNode();
        FormalArguments formals = rootNode.getFormalArguments();
        MatchPermutation match = permuteArguments(function, evaluatedArgs.getNames(), formals, callSrc, null, forNextMethod, index -> {
            throw Utils.nyi("S3Dispatch should not have arg length mismatch");
        }, index -> evaluatedArgs.getNames()[index]);

        Object[] evaledArgs = new Object[match.resultPermutation.length];

        for (int formalIndex = 0; formalIndex < match.resultPermutation.length; formalIndex++) {
            int suppliedIndex = match.resultPermutation[formalIndex];

            // Has varargs? Unfold!
            if (suppliedIndex == VARARGS) {
                int varArgsLen = match.varargsPermutation.length;
                Object[] newVarArgs = new Object[varArgsLen];
                boolean nonNull = false;
                for (int i = 0; i < varArgsLen; i++) {
                    newVarArgs[i] = evaluatedArgs.arguments[match.varargsPermutation[i]];
                    nonNull |= newVarArgs[i] != null;
                }
                if (nonNull) {
                    evaledArgs[formalIndex] = new RArgsValuesAndNames(newVarArgs, match.varargsNames);
                } else {
                    evaledArgs[formalIndex] = RArgsValuesAndNames.EMPTY;
                }
            } else if (suppliedIndex == UNMATCHED) {
                // nothing to do... (resArgs[formalIndex] == null)
            } else {
                evaledArgs[formalIndex] = evaluatedArgs.arguments[suppliedIndex];
            }
        }

        // Replace RMissing with default value!
        RNode[] defaultArgs = formals.getDefaultArgs();
        for (int fi = 0; fi < defaultArgs.length; fi++) {
            Object evaledArg = evaledArgs[fi];
            if (evaledArg == null) {
                // This is the case whenever there is a new parameter introduced in front of a
                // vararg in the specific version of a generic
                RNode defaultArg = formals.getDefaultArg(fi);
                if (defaultArg == null) {
                    // If neither supplied nor default argument

                    if (formals.getVarArgIndex() == fi) {
                        // "...", but empty
                        evaledArgs[fi] = RArgsValuesAndNames.EMPTY;
                    } else {
                        evaledArgs[fi] = RMissing.instance;
                    }
                } else {
                    // <null> for environment leads to it being fitted with the REnvironment on the
                    // callee side
                    Closure defaultClosure = formals.getOrCreateClosure(defaultArg);
                    evaledArgs[fi] = RDataFactory.createPromise(function.isBuiltin() ? EvalPolicy.INLINED : EvalPolicy.PROMISED, PromiseType.ARG_DEFAULT, null, defaultClosure);
                }
            } else if (function.isBuiltin() && evaledArg instanceof RPromise) {
                RPromise promise = (RPromise) evaledArg;
                evaledArgs[fi] = promiseHelper.evaluate(frame, promise);
            }
        }
        for (int i = 0; i < evaledArgs.length; ++i) {
            if (evaledArgs[i] == null) {
                evaledArgs[i] = RMissing.instance;
            }
        }
        return new EvaluatedArguments(evaledArgs, formals.getNames());
    }

    /**
     * Matches the supplied arguments to the formal ones and returns them as consolidated
     * {@code RNode[]}. Handles named args and varargs.<br/>
     * <strong>Does not</strong> alter the given {@link CallArgumentsNode}
     *
     * @param function The function which is to be called
     * @param suppliedArgs The arguments supplied to the call
     * @param suppliedNames The names for the arguments supplied to the call
     * @param callSrc The source of the function call currently executed
     * @param argsSrc The source code encapsulating the arguments, for debugging purposes
     * @param isForInlinedBuiltin Whether the arguments are passed into an inlined builtin and need
     *            special treatment
     * @param closureCache The {@link ClosureCache} for the supplied arguments
     *
     * @return A list of {@link RNode}s which consist of the given arguments in the correct order
     *         and wrapped into the proper {@link PromiseNode}s
     */
    private static RNode[] matchNodes(RFunction function, RNode[] suppliedArgs, String[] suppliedNames, SourceSection callSrc, SourceSection argsSrc, boolean isForInlinedBuiltin,
                    ClosureCache closureCache, boolean noOpt) {
        assert suppliedArgs.length == suppliedNames.length;

        FormalArguments formals = ((RRootNode) function.getTarget().getRootNode()).getFormalArguments();

        // Rearrange arguments
        MatchPermutation match = permuteArguments(function, suppliedNames, formals, callSrc, argsSrc, false, index -> ArgumentsTrait.isVarArg(RMissingHelper.unwrapName(suppliedArgs[index])),
                        index -> suppliedArgs[index].getSourceSection().getCode());

        RNode[] defaultArgs = formals.getDefaultArgs();
        RNode[] resArgs = new RNode[match.resultPermutation.length];

        /**
         * Walks a list of given arguments ({@link RNode}s) and wraps them in {@link PromiseNode}s
         * individually by using promiseWrapper (unfolds varargs, too!) if necessary.
         *
         * @param function The function which is to be called
         * @param arguments The arguments passed to the function call, already in correct order
         * @param formals The {@link FormalArguments} for the given function
         * @param promiseWrapper The {@link PromiseWrapper} implementation which handles the
         *            wrapping of individual arguments
         * @param closureCache The {@link ClosureCache} for the supplied arguments
         * @return A list of {@link RNode} wrapped in {@link PromiseNode}s
         */

        // Check whether this is a builtin
        RootNode rootNode = function.getTarget().getRootNode();
        RBuiltinRootNode builtinRootNode = rootNode instanceof RBuiltinRootNode ? (RBuiltinRootNode) rootNode : null;

        // int logicalIndex = 0; As our builtin's 'evalsArgs' is meant for FastR arguments (which
        // take "..." as one), we don't need a logicalIndex
        for (int formalIndex = 0; formalIndex < match.resultPermutation.length; formalIndex++) {
            int suppliedIndex = match.resultPermutation[formalIndex];

            // Has varargs? Unfold!
            if (suppliedIndex == VARARGS) {
                int varArgsLen = match.varargsPermutation.length;
                String[] newNames = match.varargsNames;
                RNode[] newVarArgs = new RNode[varArgsLen];
                int index = 0;
                for (int i = 0; i < varArgsLen; i++) {
                    RNode varArg = suppliedArgs[match.varargsPermutation[i]];
                    if (varArg == null) {
                        if (newNames[i] == null) {
                            // Skip all missing values (important for detection of emtpy "...",
                            // which consequently collapse
                            continue;
                        } else {
                            // But do not skip parameters ala "[...], builtins =, [...]"
                            varArg = ConstantNode.create(RMissing.instance);
                        }
                    }
                    newNames[index] = newNames[i];
                    newVarArgs[index] = varArg;
                    index++;
                }

                // "Delete and shrink": Shrink only if necessary
                int newLength = index;
                if (newLength == 0) {
                    // Corner case: "f <- function(...) g(...); g <- function(...)"
                    // Insert correct "missing"!
                    resArgs[formalIndex] = wrap(formals, builtinRootNode, closureCache, null, null, formalIndex, isForInlinedBuiltin, noOpt);
                    continue;
                }
                if (newNames.length > newLength) {
                    newNames = Arrays.copyOf(newNames, newLength);
                    newVarArgs = Arrays.copyOf(newVarArgs, newLength);
                }

                EvalPolicy evalPolicy = getEvalPolicy(builtinRootNode, formalIndex);
                resArgs[formalIndex] = PromiseNode.createVarArgs(null, evalPolicy, newVarArgs, newNames, closureCache, callSrc);
            } else {
                RNode defaultArg = formalIndex < defaultArgs.length ? defaultArgs[formalIndex] : null;
                RNode suppliedArg = suppliedIndex == UNMATCHED ? null : suppliedArgs[suppliedIndex];
                resArgs[formalIndex] = wrap(formals, builtinRootNode, closureCache, suppliedArg, defaultArg, formalIndex, isForInlinedBuiltin, noOpt);
            }
        }
        return resArgs;
    }

    private static final class MatchPermutation {
        private final int[] resultPermutation;
        private final int[] varargsPermutation;
        private final String[] varargsNames;

        public MatchPermutation(int[] resultPermutation, int[] varargsPermutation, String[] varargsNames) {
            this.resultPermutation = resultPermutation;
            this.varargsPermutation = varargsPermutation;
            this.varargsNames = varargsNames;
        }
    }

    private static final int UNMATCHED = -1;
    private static final int VARARGS = -2;

    /**
     * /** This method does the heavy lifting of re-arranging arguments by their names and position,
     * also handling varargs.
     *
     * @param function The function which should be called
     * @param suppliedNames The names the arguments might have
     * @param formals The {@link FormalArguments} this function has
     * @param callSrc The source of the function call currently executed
     * @param argsSrc The source code encapsulating the arguments, for debugging purposes
     * @param forNextMethod matching when evaluating NextMethod
     *
     * @return An array of type <T> with the supplied arguments in the correct order
     */
    @TruffleBoundary
    private static MatchPermutation permuteArguments(RFunction function, String[] suppliedNames, FormalArguments formals, SourceSection callSrc, SourceSection argsSrc, boolean forNextMethod,
                    IntPredicate isVarSuppliedVarargs, IntFunction<String> errorString) {
        // assert Arrays.stream(suppliedNames).allMatch(name -> name == null || !name.isEmpty());

        // Preparations
        int varArgIndex = formals.getVarArgIndex();
        boolean hasVarArgs = varArgIndex != FormalArguments.NO_VARARG;

        // MATCH by exact name
        int[] resultPermutation = new int[formals.getNames().length];
        Arrays.fill(resultPermutation, UNMATCHED);

        boolean[] matchedSuppliedArgs = new boolean[suppliedNames.length];
        for (int suppliedIndex = 0; suppliedIndex < suppliedNames.length; suppliedIndex++) {
            if (suppliedNames[suppliedIndex] == null || suppliedNames[suppliedIndex].isEmpty()) {
                continue;
            }

            // Search for argument name inside formal arguments
            int formalIndex = findParameterPosition(formals.getNames(), suppliedNames[suppliedIndex], resultPermutation, suppliedIndex, hasVarArgs, callSrc, argsSrc, varArgIndex, forNextMethod,
                            errorString);
            if (formalIndex != UNMATCHED) {
                resultPermutation[formalIndex] = suppliedIndex;
                matchedSuppliedArgs[suppliedIndex] = true;
            }
        }

        // TODO MATCH by partial name (up to the vararg, which consumes all non-exact matches)

        // MATCH by position
        int suppliedIndex = -1;
        int regularArgumentCount = hasVarArgs ? varArgIndex : formals.getNames().length;
        outer: for (int formalIndex = 0; formalIndex < regularArgumentCount; formalIndex++) {
            // Unmatched?
            if (resultPermutation[formalIndex] == UNMATCHED) {
                while (true) {
                    suppliedIndex++;
                    if (suppliedIndex == suppliedNames.length) {
                        // no more unmatched supplied arguments
                        break outer;
                    }
                    if (!matchedSuppliedArgs[suppliedIndex]) {
                        if (forNextMethod) {
                            // for NextMethod, unused parameters are matched even when named
                            break;
                        }
                        if (suppliedNames[suppliedIndex] == null || suppliedNames[suppliedIndex].isEmpty()) {
                            // unnamed parameter, match by position
                            break;
                        }
                    }
                }
                resultPermutation[formalIndex] = suppliedIndex;

                // set formal status AND "remove" supplied arg from list
                matchedSuppliedArgs[suppliedIndex] = true;
            }
        }

        // MATCH rest to vararg "..."
        if (hasVarArgs) {
            int varArgCount = suppliedNames.length - cardinality(matchedSuppliedArgs);

            // Create vararg array
            int[] varArgsPermutation = new int[varArgCount];
            String[] namesArray = new String[varArgCount];

            // Add every supplied argument that has not been matched
            int pos = 0;
            for (suppliedIndex = 0; suppliedIndex < suppliedNames.length; suppliedIndex++) {
                if (!matchedSuppliedArgs[suppliedIndex]) {
                    matchedSuppliedArgs[suppliedIndex] = true;
                    varArgsPermutation[pos] = suppliedIndex;
                    namesArray[pos] = suppliedNames[suppliedIndex];
                    pos++;
                }
            }

            resultPermutation[varArgIndex] = VARARGS;
            return new MatchPermutation(resultPermutation, varArgsPermutation, namesArray);
        } else {
            // Error check: Unused argument? (can only happen when there are no varargs)

            suppliedIndex = 0;
            while (suppliedIndex < suppliedNames.length && matchedSuppliedArgs[suppliedIndex]) {
                suppliedIndex++;
            }

            if (suppliedIndex < suppliedNames.length) {
                int leftoverCount = suppliedNames.length - cardinality(matchedSuppliedArgs);
                if (leftoverCount == 1) {
                    if (isVarSuppliedVarargs.test(suppliedIndex)) {
                        return new MatchPermutation(resultPermutation, null, null);
                    }

                    // one unused argument
                    CompilerDirectives.transferToInterpreter();
                    throw RError.error(callSrc, RError.Message.UNUSED_ARGUMENT, errorString.apply(suppliedIndex));
                }

                CompilerDirectives.transferToInterpreter();
                // multiple unused arguments
                StringBuilder str = new StringBuilder();
                int cnt = 0;
                for (; suppliedIndex < suppliedNames.length; suppliedIndex++) {
                    if (!matchedSuppliedArgs[suppliedIndex]) {
                        if (cnt++ > 0) {
                            str.append(", ");
                        }
                        str.append(errorString.apply(suppliedIndex));
                    }
                }
                throw RError.error(callSrc, RError.Message.UNUSED_ARGUMENTS, str);
            }
            return new MatchPermutation(resultPermutation, null, null);
        }
    }

    private static int cardinality(boolean[] array) {
        int sum = 0;
        for (boolean b : array) {
            if (b) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * Searches for suppliedName inside formalNames and returns its (formal) index.
     *
     * @return The position of the given suppliedName inside the formalNames. Throws errors if the
     *         argument has been matched before
     */
    private static <T> int findParameterPosition(String[] formalNames, String suppliedName, int[] resultPermutation, int suppliedIndex, boolean hasVarArgs, SourceSection callSrc,
                    SourceSection argsSrc, int varArgIndex, boolean forNextMethod, IntFunction<String> errorString) {
        int found = UNMATCHED;
        for (int i = 0; i < formalNames.length; i++) {
            if (formalNames[i] == null) {
                continue;
            }

            String formalName = formalNames[i];
            if (formalName.equals(suppliedName)) {
                found = i;
                if (resultPermutation[found] != UNMATCHED) {
                    // Has already been matched: Error!
                    throw RError.error(argsSrc, RError.Message.FORMAL_MATCHED_MULTIPLE, formalName);
                }
                break;
            } else if (!suppliedName.isEmpty() && formalName.startsWith(suppliedName) && ((varArgIndex != FormalArguments.NO_VARARG && i < varArgIndex) || varArgIndex == FormalArguments.NO_VARARG)) {
                // partial-match only if the formal argument is positioned before ...
                if (found >= 0) {
                    throw RError.error(argsSrc, RError.Message.ARGUMENT_MATCHES_MULTIPLE, 1 + suppliedIndex);
                }
                found = i;
                if (resultPermutation[found] != UNMATCHED) {
                    throw RError.error(argsSrc, RError.Message.FORMAL_MATCHED_MULTIPLE, formalName);
                }
            }
        }
        if (found >= 0 || hasVarArgs || forNextMethod) {
            return found;
        }
        throw RError.error(callSrc, RError.Message.UNUSED_ARGUMENT, errorString.apply(suppliedIndex));
    }

    /**
     * @param builtinRootNode The {@link RBuiltinRootNode} of the function
     * @param formalIndex The formalIndex of this argument
     * @return A single suppliedArg and its corresponding defaultValue wrapped up into a
     *         {@link PromiseNode}
     */
    public static EvalPolicy getEvalPolicy(RBuiltinRootNode builtinRootNode, int formalIndex) {
        // This is for actual function calls. However, if the arguments are meant for a
        // builtin, we have to consider whether they should be forced or not!
        return builtinRootNode != null && builtinRootNode.evaluatesArg(formalIndex) ? EvalPolicy.INLINED : EvalPolicy.PROMISED;
    }

    /**
     * @param formals {@link FormalArguments} as {@link ClosureCache}
     * @param builtinRootNode The {@link RBuiltinRootNode} of the function
     * @param closureCache {@link ClosureCache}
     * @param suppliedArg The argument supplied for this parameter
     * @param defaultValue The default value for this argument
     * @param formalIndex The logicalIndex of this argument, also counting individual arguments in
     *            varargs
     * @param isBuiltin
     * @param noOpt
     * @return Either suppliedArg or its defaultValue wrapped up into a {@link PromiseNode} (or
     *         {@link RMissing} in case neither is present!
     */
    @TruffleBoundary
    public static RNode wrap(FormalArguments formals, RBuiltinRootNode builtinRootNode, ClosureCache closureCache, RNode suppliedArg, RNode defaultValue, int formalIndex, boolean isBuiltin,
                    boolean noOpt) {
        // Determine whether to choose supplied argument or default value
        RNode expr = null;
        PromiseType promiseType = null;
        if (suppliedArg != null) {
            // Supplied arg
            expr = suppliedArg;
            promiseType = PromiseType.ARG_SUPPLIED;
        } else {
            // Default value
            if (isBuiltin && defaultValue != null) {
                expr = defaultValue;
                promiseType = PromiseType.ARG_DEFAULT;
            } else {
                if (formals.getVarArgIndex() == formalIndex) {
                    // "...", but empty
                    return ConstantNode.create(RArgsValuesAndNames.EMPTY);
                } else {
                    // In this case, we simply return RMissing (like R)
                    return ConstantNode.create(RMissing.instance);
                }
            }
        }

        // Create promise
        EvalPolicy evalPolicy = getEvalPolicy(builtinRootNode, formalIndex);
        Closure closure = closureCache.getOrCreateClosure(expr);
        Closure defaultClosure = formals.getOrCreateClosure(defaultValue);
        return PromiseNode.create(expr.getSourceSection(), RPromiseFactory.create(evalPolicy, promiseType, closure, defaultClosure), noOpt);
    }
}
