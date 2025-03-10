/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.nodes.builtin.base;

import static com.oracle.truffle.r.runtime.RBuiltinKind.INTERNAL;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.utilities.BranchProfile;
import com.oracle.truffle.api.utilities.ValueProfile;
import com.oracle.truffle.r.nodes.CallInlineCacheNode;
import com.oracle.truffle.r.nodes.CallInlineCacheNodeGen;
import com.oracle.truffle.r.nodes.attributes.TypeFromModeNode;
import com.oracle.truffle.r.nodes.attributes.TypeFromModeNodeGen;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.function.PromiseHelperNode;
import com.oracle.truffle.r.nodes.function.signature.RArgumentsNode;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.RArguments;
import com.oracle.truffle.r.runtime.RBuiltin;
import com.oracle.truffle.r.runtime.RCaller;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.RList;
import com.oracle.truffle.r.runtime.data.RPromise;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractStringVector;
import com.oracle.truffle.r.runtime.env.REnvironment;

/**
 * assert: not expected to be fast even when called as, e.g., {@code get("x")}.
 */
public class GetFunctions {
    public abstract static class Adapter extends RBuiltinNode {
        private final BranchProfile unknownObjectErrorProfile = BranchProfile.create();
        protected final ValueProfile modeProfile = ValueProfile.createIdentityProfile();
        protected final BranchProfile inheritsProfile = BranchProfile.create();
        @Child private PromiseHelperNode promiseHelper = new PromiseHelperNode();

        protected void unknownObject(String x, RType modeType, String modeString) throws RError {
            unknownObjectErrorProfile.enter();
            if (modeType == RType.Any) {
                throw RError.error(this, RError.Message.UNKNOWN_OBJECT, x);
            } else {
                throw RError.error(this, RError.Message.UNKNOWN_OBJECT_MODE, x, modeType == null ? modeString : modeType.getName());
            }
        }

        protected Object checkPromise(VirtualFrame frame, Object r) {
            if (r instanceof RPromise) {
                return promiseHelper.evaluate(frame, (RPromise) r);
            } else {
                return r;
            }
        }

    }

    @RBuiltin(name = "get", kind = INTERNAL, parameterNames = {"x", "envir", "mode", "inherits"})
    public abstract static class Get extends Adapter {

        @Child private TypeFromModeNode typeFromMode = TypeFromModeNodeGen.create();

        public abstract Object execute(VirtualFrame frame, Object name, REnvironment envir, String mode, byte inherits);

        public static boolean isInherits(byte inherits) {
            return inherits == RRuntime.LOGICAL_TRUE;
        }

        @Specialization(guards = "!isInherits(inherits)")
        protected Object getNonInherit(VirtualFrame frame, RAbstractStringVector xv, REnvironment envir, String mode, @SuppressWarnings("unused") byte inherits) {
            controlVisibility();
            RType modeType = typeFromMode.execute(mode);
            return getAndCheck(frame, xv, envir, modeType, true);
        }

        @Specialization(guards = "isInherits(inherits)")
        protected Object getInherit(VirtualFrame frame, RAbstractStringVector xv, REnvironment envir, String mode, @SuppressWarnings("unused") byte inherits) {
            controlVisibility();
            RType modeType = typeFromMode.execute(mode);
            Object r = getAndCheck(frame, xv, envir, modeType, false);
            if (r == null) {
                inheritsProfile.enter();
                String x = xv.getDataAt(0);
                REnvironment env = envir;
                while (env != REnvironment.emptyEnv()) {
                    env = env.getParent();
                    if (env != REnvironment.emptyEnv()) {
                        r = checkPromise(frame, env.get(x));
                        if (r != null && RRuntime.checkType(r, modeType)) {
                            break;
                        }
                    }
                }
                if (r == null) {
                    unknownObject(x, modeType, mode);
                }
            }
            return r;
        }

        protected Object getAndCheck(VirtualFrame frame, RAbstractStringVector xv, REnvironment env, RType modeType, boolean fail) throws RError {
            String x = xv.getDataAt(0);
            Object obj = checkPromise(frame, env.get(x));
            if (obj != null && RRuntime.checkType(obj, modeType)) {
                return obj;
            } else {
                if (fail) {
                    unknownObject(x, modeType, modeType.getName());
                }
                return null;
            }
        }
    }

    @RBuiltin(name = "mget", kind = INTERNAL, parameterNames = {"x", "envir", "mode", "ifnotfound", "inherits"})
    public abstract static class MGet extends Adapter {

        private final RCaller caller = RDataFactory.createCaller(this);
        private final BranchProfile wrongLengthErrorProfile = BranchProfile.create();

        @Child private TypeFromModeNode typeFromMode = TypeFromModeNodeGen.create();
        @Child private CallInlineCacheNode callCache = CallInlineCacheNodeGen.create();
        @Child private RArgumentsNode argsNode;

        @CompilationFinal private boolean needsCallerFrame;

        public static boolean isInherits(byte inherits) {
            return inherits == RRuntime.LOGICAL_TRUE;
        }

        private static class State {
            final int svLength;
            final int modeLength;
            final int ifNotFoundLength;
            final RFunction ifnFunc;
            final Object[] data;
            final String[] names;
            boolean complete = RDataFactory.COMPLETE_VECTOR;

            State(RStringVector xv, RAbstractStringVector mode, RList ifNotFound) {
                this.svLength = xv.getLength();
                this.modeLength = mode.getLength();
                this.ifNotFoundLength = ifNotFound.getLength();
                if (ifNotFoundLength == 1 && ifNotFound.getDataAt(0) instanceof RFunction) {
                    ifnFunc = (RFunction) ifNotFound.getDataAt(0);
                } else {
                    ifnFunc = null;
                }
                data = new Object[svLength];
                names = new String[svLength];
            }

            String checkNA(String x) {
                if (x == RRuntime.STRING_NA) {
                    complete = RDataFactory.INCOMPLETE_VECTOR;
                }
                return x;
            }

            RList getResult() {
                return RDataFactory.createList(data, RDataFactory.createStringVector(names, complete));
            }
        }

        private State checkArgs(RStringVector xv, RAbstractStringVector mode, RList ifNotFound) {
            State state = new State(xv, mode, ifNotFound);
            if (!(state.modeLength == 1 || state.modeLength == state.svLength)) {
                wrongLengthErrorProfile.enter();
                throw RError.error(this, RError.Message.WRONG_LENGTH_ARG, "mode");
            }
            if (!(state.ifNotFoundLength == 1 || state.ifNotFoundLength == state.svLength)) {
                wrongLengthErrorProfile.enter();
                throw RError.error(this, RError.Message.WRONG_LENGTH_ARG, "ifnotfound");
            }
            return state;

        }

        @Specialization(guards = "!isInherits(inherits)")
        protected RList mgetNonInherit(VirtualFrame frame, RStringVector xv, REnvironment env, RAbstractStringVector mode, RList ifNotFound, @SuppressWarnings("unused") byte inherits) {
            controlVisibility();
            State state = checkArgs(xv, mode, ifNotFound);
            for (int i = 0; i < state.svLength; i++) {
                String x = state.checkNA(xv.getDataAt(i));
                state.names[i] = x;
                RType modeType = typeFromMode.execute(mode.getDataAt(state.modeLength == 1 ? 0 : i));
                Object r = checkPromise(frame, env.get(x));
                if (r != null && RRuntime.checkType(r, modeType)) {
                    state.data[i] = r;
                } else {
                    doIfNotFound(frame, state, i, x, ifNotFound);
                }
            }
            return state.getResult();
        }

        @Specialization(guards = "isInherits(inherits)")
        protected RList mgetInherit(VirtualFrame frame, RStringVector xv, REnvironment envir, RAbstractStringVector mode, RList ifNotFound, @SuppressWarnings("unused") byte inherits) {
            controlVisibility();
            State state = checkArgs(xv, mode, ifNotFound);
            for (int i = 0; i < state.svLength; i++) {
                String x = state.checkNA(xv.getDataAt(i));
                state.names[i] = x;
                RType modeType = typeFromMode.execute(mode.getDataAt(state.modeLength == 1 ? 0 : i));
                Object r = envir.get(x);
                if (r == null || !RRuntime.checkType(r, modeType)) {
                    inheritsProfile.enter();
                    REnvironment env = envir;
                    while (env != REnvironment.emptyEnv()) {
                        env = env.getParent();
                        if (env != REnvironment.emptyEnv()) {
                            r = checkPromise(frame, env.get(x));
                            if (r != null && RRuntime.checkType(r, modeType)) {
                                break;
                            }
                        }
                    }
                }
                if (r == null) {
                    doIfNotFound(frame, state, i, x, ifNotFound);
                } else {
                    state.data[i] = r;
                }
            }
            return state.getResult();
        }

        private void doIfNotFound(VirtualFrame frame, State state, int i, String x, RList ifNotFound) {
            if (state.ifnFunc != null) {
                state.data[i] = call(frame, state.ifnFunc, x);
            } else {
                state.data[i] = ifNotFound.getDataAt(state.ifNotFoundLength == 1 ? 0 : i);
            }
        }

        private Object call(VirtualFrame frame, RFunction ifnFunc, String x) {
            if (!needsCallerFrame && ifnFunc.containsDispatch()) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                needsCallerFrame = true;
            }
            if (argsNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                argsNode = insert(RArgumentsNode.create());
            }
            MaterializedFrame callerFrame = needsCallerFrame ? frame.materialize() : null;
            Object[] callArgs = argsNode.execute(ifnFunc, caller, callerFrame, RArguments.getDepth(frame) + 1, new Object[]{x}, ArgumentsSignature.empty(1), null);
            return callCache.execute(frame, ifnFunc.getTarget(), callArgs);
        }

    }

}
