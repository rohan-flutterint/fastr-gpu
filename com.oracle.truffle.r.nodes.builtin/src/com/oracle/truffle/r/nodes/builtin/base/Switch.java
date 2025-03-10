/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates
 *
 * All rights reserved.
 */

package com.oracle.truffle.r.nodes.builtin.base;

import static com.oracle.truffle.r.runtime.RBuiltinKind.PRIMITIVE;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.utilities.BranchProfile;
import com.oracle.truffle.api.utilities.ConditionProfile;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.function.PromiseHelperNode.PromiseCheckHelperNode;
import com.oracle.truffle.r.nodes.function.RMissingHelper;
import com.oracle.truffle.r.nodes.unary.CastIntegerNode;
import com.oracle.truffle.r.nodes.unary.CastIntegerNodeGen;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.RBuiltin;
import com.oracle.truffle.r.runtime.RDeparse;
import com.oracle.truffle.r.runtime.RDeparse.State;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.data.RArgsValuesAndNames;
import com.oracle.truffle.r.runtime.data.RMissing;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.RPromise;
import com.oracle.truffle.r.runtime.data.model.RAbstractStringVector;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;

/**
 * The {@code switch} builtin. When called directly, the "..." arguments are not evaluated before
 * the call, as the semantics requires that only the matched case is evaluated. However, if called
 * indirectly, e.g., by {@do.call}, the arguments will have been evaluated, regardless of the match,
 * so we have to be prepared for both evaluated and unevaluated args, which is encapsulated in
 * {@link PromiseCheckHelperNode}.
 *
 */
@RBuiltin(name = "switch", kind = PRIMITIVE, parameterNames = {"EXPR", "..."}, nonEvalArgs = 1)
public abstract class Switch extends RBuiltinNode {
    @Child private CastIntegerNode castIntNode;
    @Child private PromiseCheckHelperNode promiseHelper = new PromiseCheckHelperNode();

    private final BranchProfile suppliedArgNameIsEmpty = BranchProfile.create();
    private final BranchProfile suppliedArgNameIsNull = BranchProfile.create();
    private final BranchProfile matchedArgIsMissing = BranchProfile.create();
    private final ConditionProfile currentDefaultProfile = ConditionProfile.createBinaryProfile();
    private final ConditionProfile returnValueProfile = ConditionProfile.createBinaryProfile();
    private final BranchProfile notIntType = BranchProfile.create();

    @Specialization
    protected Object doSwitch(VirtualFrame frame, RAbstractStringVector x, RArgsValuesAndNames optionalArgs) {
        controlVisibility();
        if (x.getLength() != 1) {
            throw RError.error(this, RError.Message.EXPR_NOT_LENGTH_ONE);
        }
        return prepareResult(doSwitchString(frame, x, optionalArgs));
    }

    protected Object doSwitchString(VirtualFrame frame, RAbstractStringVector x, RArgsValuesAndNames optionalArgs) {
        controlVisibility();
        Object[] optionalArgValues = optionalArgs.getArguments();
        final String xStr = x.getDataAt(0);
        ArgumentsSignature signature = optionalArgs.getSignature();
        for (int i = 0; i < signature.getLength(); i++) {
            final String suppliedArgName = signature.getName(i);
            if (suppliedArgName == null) {
                continue;
            } else if (suppliedArgName.length() == 0) {
                suppliedArgNameIsEmpty.enter();
                throw RError.error(this, RError.Message.ZERO_LENGTH_VARIABLE);
            } else if (xStr.equals(suppliedArgName)) {
                // match, evaluate the associated arg
                Object optionalArgValue = promiseHelper.checkEvaluate(frame, optionalArgValues[i]);
                if (RMissingHelper.isMissing(optionalArgValue)) {
                    matchedArgIsMissing.enter();

                    // Fall-through: If the matched value is missing, take the next non-missing
                    for (int j = i + 1; j < optionalArgValues.length; j++) {
                        Object val = promiseHelper.checkEvaluate(frame, optionalArgValues[j]);
                        if (!RMissingHelper.isMissing(val)) {
                            return val;
                        }
                    }

                    // No non-missing value: invisible null
                    return null;
                } else {
                    // Default: Matched name has a value
                    return optionalArgValue;
                }
            }
        }
        // We didn't find a match, so check for default(s)
        Object currentDefault = null;
        for (int i = 0; i < signature.getLength(); i++) {
            final String suppliedArgName = signature.getName(i);
            if (suppliedArgName == null) {
                suppliedArgNameIsNull.enter();
                Object optionalArg = optionalArgValues[i];
                if (currentDefaultProfile.profile(currentDefault != null)) {
                    throw RError.error(this, RError.Message.DUPLICATE_SWITCH_DEFAULT, deparseDefault(currentDefault), deparseDefault(optionalArg));
                } else {
                    currentDefault = optionalArg;
                }
            }
        }
        if (returnValueProfile.profile(currentDefault != null)) {
            return promiseHelper.checkEvaluate(frame, currentDefault);
        } else {
            return null;
        }
    }

    private static String deparseDefault(Object arg) {
        if (arg instanceof RPromise) {
            // We do not want to evaluate the promise,just display the rep
            RPromise p = (RPromise) arg;
            RBaseNode node = p.getRep();
            State state = State.createPrintableState();
            node.deparse(state);
            return state.toString();
        } else {
            return RDeparse.deparseForPrint(arg);
        }
    }

    @Specialization
    protected Object doSwitch(VirtualFrame frame, int x, RArgsValuesAndNames optionalArgs) {
        return prepareResult(doSwitchInt(frame, x, optionalArgs));
    }

    @Specialization
    protected Object doSwitch(VirtualFrame frame, Object x, RArgsValuesAndNames optionalArgs) {
        if (castIntNode == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castIntNode = insert(CastIntegerNodeGen.create(false, false, false));
        }
        Object objIndex = castIntNode.execute(x);
        if (!(objIndex instanceof Integer)) {
            notIntType.enter();
            return null;
        }
        return prepareResult(doSwitchInt(frame, (int) objIndex, optionalArgs));
    }

    @SuppressWarnings("unused")
    @Specialization
    protected Object doSwitch(RMissing x, RMissing optionalArgs) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(this, RError.Message.EXPR_MISSING);
    }

    private Object doSwitchInt(VirtualFrame frame, int index, RArgsValuesAndNames optionalArgs) {
        Object[] optionalArgValues = optionalArgs.getArguments();
        if (index >= 1 && index <= optionalArgValues.length) {
            Object value = promiseHelper.checkEvaluate(frame, optionalArgValues[index - 1]);
            if (value != null) {
                return value;
            }
            throw RError.error(this, RError.Message.NO_ALTERNATIVE_IN_SWITCH);
        }
        return null;
    }

    protected boolean isLengthOne(RAbstractStringVector x) {
        return x.getLength() == 1;
    }

    private Object prepareResult(Object value) {
        if (returnValueProfile.profile(value != null)) {
            forceVisibility(true);
            return value;
        } else {
            forceVisibility(false);
            return RNull.instance;
        }
    }

}
