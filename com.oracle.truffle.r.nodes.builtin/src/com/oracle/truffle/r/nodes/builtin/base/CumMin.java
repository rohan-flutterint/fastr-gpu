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

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.unary.CastDoubleNode;
import com.oracle.truffle.r.nodes.unary.CastDoubleNodeGen;
import com.oracle.truffle.r.runtime.RBuiltin;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.data.RAttributeProfiles;
import com.oracle.truffle.r.runtime.data.RComplexVector;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RDoubleVector;
import com.oracle.truffle.r.runtime.data.RIntSequence;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.RLogicalVector;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.ops.na.NACheck;

@RBuiltin(name = "cummin", kind = PRIMITIVE, parameterNames = {"x"})
public abstract class CumMin extends RBuiltinNode {

    private final NACheck na = NACheck.create();

    @Child private CastDoubleNode castDouble;

    private final RAttributeProfiles attrProfiles = RAttributeProfiles.create();

    @Specialization
    protected double cummin(double arg) {
        controlVisibility();
        return arg;
    }

    @Specialization
    protected int cummin(int arg) {
        controlVisibility();
        return arg;
    }

    @Specialization
    protected int cummin(byte arg) {
        controlVisibility();
        na.enable(arg);
        if (na.check(arg)) {
            return RRuntime.INT_NA;
        }
        return arg;
    }

    @Specialization
    protected double cummin(String arg) {
        controlVisibility();
        return na.convertStringToDouble(arg);
    }

    @Specialization
    protected RIntVector cummin(RIntSequence v) {
        controlVisibility();
        int[] cminV = new int[v.getLength()];

        if (v.getStride() > 0) {
            // all numbers are bigger than the first one
            Arrays.fill(cminV, v.getStart());
            return RDataFactory.createIntVector(cminV, RDataFactory.COMPLETE_VECTOR, v.getNames(attrProfiles));
        } else {
            cminV[0] = v.getStart();
            for (int i = 1; i < v.getLength(); i++) {
                cminV[i] = cminV[i - 1] + v.getStride();
            }
            return RDataFactory.createIntVector(cminV, RDataFactory.COMPLETE_VECTOR, v.getNames(attrProfiles));
        }
    }

    @Specialization
    protected RDoubleVector cummin(RDoubleVector v) {
        controlVisibility();
        double[] cminV = new double[v.getLength()];
        double min = v.getDataAt(0);
        cminV[0] = min;
        na.enable(v);
        int i;
        for (i = 1; i < v.getLength(); i++) {
            if (v.getDataAt(i) < min) {
                min = v.getDataAt(i);
            }
            if (na.check(v.getDataAt(i))) {
                break;
            }
            cminV[i] = min;
        }
        if (!na.neverSeenNA()) {
            Arrays.fill(cminV, i, cminV.length, RRuntime.DOUBLE_NA);
        }
        return RDataFactory.createDoubleVector(cminV, na.neverSeenNA(), v.getNames(attrProfiles));
    }

    @Specialization
    protected RIntVector cummin(RIntVector v) {
        controlVisibility();
        int[] cminV = new int[v.getLength()];
        int min = v.getDataAt(0);
        cminV[0] = min;
        na.enable(v);
        int i;
        for (i = 1; i < v.getLength(); i++) {
            if (v.getDataAt(i) < min) {
                min = v.getDataAt(i);
            }
            if (na.check(v.getDataAt(i))) {
                break;
            }
            cminV[i] = min;
        }
        if (!na.neverSeenNA()) {
            Arrays.fill(cminV, i, cminV.length, RRuntime.INT_NA);
        }
        return RDataFactory.createIntVector(cminV, na.neverSeenNA(), v.getNames(attrProfiles));
    }

    @Specialization
    protected RIntVector cummin(RLogicalVector v) {
        controlVisibility();
        int[] cminV = new int[v.getLength()];
        int min = v.getDataAt(0);
        cminV[0] = min;
        na.enable(v);
        int i;
        for (i = 1; i < v.getLength(); i++) {
            if (v.getDataAt(i) < min) {
                min = v.getDataAt(i);
            }
            if (na.check(v.getDataAt(i))) {
                break;
            }
            cminV[i] = min;
        }
        if (!na.neverSeenNA()) {
            Arrays.fill(cminV, i, cminV.length, RRuntime.INT_NA);
        }
        return RDataFactory.createIntVector(cminV, na.neverSeenNA(), v.getNames(attrProfiles));
    }

    @Specialization
    protected RDoubleVector cummin(RStringVector v) {
        controlVisibility();
        if (castDouble == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castDouble = insert(CastDoubleNodeGen.create(false, false, false));
        }
        return cummin((RDoubleVector) castDouble.executeDouble(v));
    }

    @Specialization
    protected RComplexVector cummin(@SuppressWarnings("unused") RComplexVector v) {
        controlVisibility();
        throw RError.error(this, RError.Message.CUMMIN_UNDEFINED_FOR_COMPLEX);
    }
}
