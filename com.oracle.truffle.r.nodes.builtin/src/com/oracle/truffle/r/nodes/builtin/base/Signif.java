/*
 * Copyright (c) 2014, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.math.*;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.utilities.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.nodes.unary.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;
import com.oracle.truffle.r.runtime.ops.na.*;

@RBuiltin(name = "signif", kind = PRIMITIVE, parameterNames = {"x", "digits"})
public abstract class Signif extends RBuiltinNode {

    @Override
    public RNode[] getParameterValues() {
        return new RNode[]{ConstantNode.create(RMissing.instance), ConstantNode.create(6)};
    }

    private final NACheck naCheck = NACheck.create();
    private final BranchProfile identity = BranchProfile.create();
    private final ConditionProfile infProfile = ConditionProfile.createBinaryProfile();

    @CreateCast("arguments")
    protected RNode[] castStatusArgument(RNode[] arguments) {
        // digits argument is at index 1
        arguments[1] = CastIntegerNodeFactory.create(arguments[1], false, false, false);
        return arguments;
    }

    // TODO: consider porting signif implementation from GNU R

    @Specialization(guards = "digitsVecLengthOne")
    protected RAbstractDoubleVector signif(RAbstractDoubleVector x, RAbstractIntVector digitsVec) {
        controlVisibility();
        int digits = digitsVec.getDataAt(0) <= 0 ? 1 : digitsVec.getDataAt(0);
        if (digits > 22) {
            identity.enter();
            return x;
        }
        double[] data = new double[x.getLength()];
        naCheck.enable(x);
        for (int i = 0; i < x.getLength(); ++i) {
            double val = x.getDataAt(i);
            if (naCheck.check(val)) {
                data[i] = RRuntime.DOUBLE_NA;
            } else {
                if (infProfile.profile(Double.isInfinite(val))) {
                    data[i] = Double.POSITIVE_INFINITY;
                } else {
                    BigDecimal bigDecimalVal = new BigDecimal(val, new MathContext(digits, RoundingMode.HALF_UP));
                    data[i] = bigDecimalVal.doubleValue();
                }
            }
        }
        RDoubleVector ret = RDataFactory.createDoubleVector(data, naCheck.neverSeenNA());
        ret.copyAttributesFrom(x);
        return ret;
    }

    @Specialization(guards = "digitsVecLengthOne")
    protected RAbstractIntVector roundDigits(RAbstractIntVector x, @SuppressWarnings("unused") RAbstractIntVector digits) {
        controlVisibility();
        return x;
    }

    // TODO: add support for digit vectors of length different than 1

    protected boolean digitsVecLengthOne(@SuppressWarnings("unused") RAbstractVector x, RAbstractIntVector digitsVec) {
        return digitsVec.getLength() == 1;
    }

}
