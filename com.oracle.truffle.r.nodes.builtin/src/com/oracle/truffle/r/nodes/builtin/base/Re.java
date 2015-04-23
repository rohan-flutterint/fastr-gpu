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
package com.oracle.truffle.r.nodes.builtin.base;

import static com.oracle.truffle.r.runtime.RBuiltinKind.*;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode.RWrapperBuiltinNode;
import com.oracle.truffle.r.nodes.builtin.base.ReFactory.ReNodeGen;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;
import com.oracle.truffle.r.runtime.ops.na.*;

@RBuiltin(name = "Re", kind = PRIMITIVE, parameterNames = {"z"})
public final class Re extends RWrapperBuiltinNode {

    public Re(RNode[] arguments, RBuiltinFactory builtin, ArgumentsSignature suppliedSignature) {
        super(arguments, builtin, suppliedSignature);
    }

    @Override
    protected RNode createDelegate() {
        return ReNodeGen.create(getArguments()[0]);
    }

    @NodeChild(value = "value", type = RNode.class)
    public abstract static class ReNode extends RNode {

        public abstract Object executeRDoubleVector(RAbstractComplexVector vector);

        private NACheck check = NACheck.create();

        @Specialization
        protected RDoubleVector re(RAbstractComplexVector vector) {
            double[] result = new double[vector.getLength()];
            check.enable(vector);
            for (int i = 0; i < vector.getLength(); i++) {
                result[i] = vector.getDataAt(i).getRealPart();
                check.check(result[i]);
            }
            return RDataFactory.createDoubleVector(result, check.neverSeenNA());
        }

        @Specialization
        protected RDoubleVector re(RAbstractDoubleVector vector) {
            return (RDoubleVector) vector.copy();
        }
    }
}
