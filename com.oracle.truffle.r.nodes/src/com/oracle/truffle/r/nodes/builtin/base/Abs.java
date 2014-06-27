/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.closures.*;
import com.oracle.truffle.r.runtime.data.model.*;
import com.oracle.truffle.r.runtime.ops.na.*;

@RBuiltin(name = "abs", kind = PRIMITIVE)
public abstract class Abs extends RBuiltinNode {

    private final NACheck check = NACheck.create();

    @SuppressWarnings("unused")
    @Specialization
    public RNull abs(RNull x) {
        controlVisibility();
        CompilerDirectives.transferToInterpreter();
        throw RError.getNonNumericArgumentFunction(this.getEncapsulatingSourceSection());
    }

    @Specialization
    public int abs(int value) {
        controlVisibility();
        check.enable(value);
        return performInt(value);
    }

    @Specialization
    public int abs(byte value) {
        controlVisibility();
        check.enable(value);
        if (check.check(value)) {
            return RRuntime.INT_NA;
        }
        return Math.abs(value);
    }

    @Specialization
    public double abs(double value) {
        controlVisibility();
        check.enable(value);
        return performDouble(value);
    }

    @Specialization
    public double abs(RComplex value) {
        controlVisibility();
        check.enable(value);
        return performComplex(value);
    }

    @SuppressWarnings("unused")
    @Specialization
    public Object abs(RRaw vector) {
        controlVisibility();
        CompilerDirectives.transferToInterpreter();
        throw RError.getNonNumericMath(this.getEncapsulatingSourceSection());
    }

    @SuppressWarnings("unused")
    @Specialization
    public Object abs(String vector) {
        controlVisibility();
        CompilerDirectives.transferToInterpreter();
        throw RError.getNonNumericMath(this.getEncapsulatingSourceSection());
    }

    @Specialization
    public RIntVector abs(RLogicalVector value) {
        controlVisibility();
        check.enable(value);
        return doAbs(RClosures.createLogicalToIntVector(value, check));
    }

    @Specialization
    public RIntVector abs(RIntVector vector) {
        controlVisibility();
        return doAbs(vector);
    }

    private RIntVector doAbs(RAbstractIntVector vector) {
        check.enable(vector);
        int[] intVector = new int[vector.getLength()];
        for (int i = 0; i < vector.getLength(); i++) {
            intVector[i] = performInt(vector.getDataAt(i));
        }
        RIntVector res = RDataFactory.createIntVector(intVector, check.neverSeenNA(), vector.getDimensions(), vector.getNames());
        res.copyRegAttributesFrom(vector);
        return res;
    }

    @Specialization
    public RDoubleVector abs(RDoubleVector vector) {
        controlVisibility();
        check.enable(vector);
        double[] doubleVector = new double[vector.getLength()];
        for (int i = 0; i < vector.getLength(); i++) {
            doubleVector[i] = performDouble(vector.getDataAt(i));
        }
        RDoubleVector res = RDataFactory.createDoubleVector(doubleVector, check.neverSeenNA(), vector.getDimensions(), vector.getNames());
        res.copyRegAttributesFrom(vector);
        return res;
    }

    @Specialization
    public RDoubleVector abs(RComplexVector vector) {
        controlVisibility();
        check.enable(vector);
        double[] doubleVector = new double[vector.getLength()];
        for (int i = 0; i < vector.getLength(); i++) {
            doubleVector[i] = performComplex(vector.getDataAt(i));
        }
        RDoubleVector res = RDataFactory.createDoubleVector(doubleVector, check.neverSeenNA(), vector.getDimensions(), vector.getNames());
        res.copyRegAttributesFrom(vector);
        return res;
    }

    @SuppressWarnings("unused")
    @Specialization
    public Object abs(RStringVector vector) {
        controlVisibility();
        CompilerDirectives.transferToInterpreter();
        throw RError.getNonNumericMath(this.getEncapsulatingSourceSection());
    }

    @SuppressWarnings("unused")
    @Specialization
    public Object abs(RRawVector vector) {
        controlVisibility();
        CompilerDirectives.transferToInterpreter();
        throw RError.getNonNumericMath(this.getEncapsulatingSourceSection());
    }

    private int performInt(int value) {
        if (check.check(value)) {
            return RRuntime.INT_NA;
        }
        return Math.abs(value);
    }

    private double performDouble(double value) {
        if (check.check(value)) {
            return RRuntime.DOUBLE_NA;
        }
        return Math.abs(value);
    }

    private double performComplex(RComplex value) {
        if (check.check(value)) {
            return RRuntime.DOUBLE_NA;
        }
        return value.abs();
    }
}
