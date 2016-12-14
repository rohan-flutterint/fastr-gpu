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
package com.oracle.truffle.r.runtime.data;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.closures.RClosures;
import com.oracle.truffle.r.runtime.data.model.RAbstractDoubleVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;

public final class RDoubleSequence extends RSequence implements RAbstractDoubleVector {

    private final double start;
    private final double stride;
    private final int max;
    @CompilationFinal private final int repetitions;
    @CompilationFinal private final TypeOfSequence typeOfSequence;

    RDoubleSequence(double start, double stride, int length) {
        super(length);
        assert length > 0;
        this.repetitions = 0;
        this.max = length;
        this.typeOfSequence = TypeOfSequence.Basic;
        this.start = start;
        this.stride = stride;
    }

    RDoubleSequence(double start, double stride, int length, int repetitions, TypeOfSequence type) {
        super(length);
        assert length > 0;
        this.max = length;
        this.start = start;
        this.stride = stride;
        this.repetitions = repetitions;
        this.typeOfSequence = type;
    }

    RDoubleSequence(double start, double stride, int length, int repetitions, int max, TypeOfSequence type) {
        super(length);
        assert length > 0;
        this.start = start;
        this.stride = stride;
        this.repetitions = repetitions;
        this.typeOfSequence = type;
        this.max = max;
    }

    public TypeOfSequence getType() {
        return this.typeOfSequence;
    }

    public int getRepetitions() {
        return this.repetitions;
    }

    public int getMax() {
        return this.max;
    }

    public double start() {
        return start;
    }

    public double stride() {
        return stride;
    }

    @Override
    public double getDataAt(int index) {
        assert index >= 0 && index < getLength();
        if (repetitions != 0) {
            // repetitions is compilation final
            if (this.typeOfSequence == TypeOfSequence.Flag) {
                int m = index / repetitions;
                return start + stride * m;
            } else if (this.typeOfSequence == TypeOfSequence.Compass) {
                int m = index % max;
                return start + stride * m;
            } else {
                return -1;
            }
        } else {
            return start + stride * index;
        }
    }

    public double getStart() {
        return start;
    }

    public double getStride() {
        return stride;
    }

    @Override
    public Object getStartObject() {
        return getStart();
    }

    @Override
    public Object getStrideObject() {
        return getStride();
    }

    public double getEnd() {
        return start + (getLength() - 1) * stride;
    }

    public RAbstractVector castSafe(RType type) {
        // TODO might be possible to implement some of these without closures
        switch (type) {
            case Double:
            case Numeric:
                return this;
            case Integer:
                return RClosures.createDoubleToIntVector(this);
            case Complex:
                return RClosures.createDoubleToComplexVector(this);
            case Character:
                return RClosures.createDoubleToStringVector(this);
            case List:
                return RClosures.createAbstractVectorToListVector(this);
            default:
                return null;
        }
    }

    private RDoubleVector populateVectorData(double[] result) {
        double current = start;
        for (int i = 0; i < getLength(); i++) {
            result[i] = current;
            current += stride;
        }
        return RDataFactory.createDoubleVector(result, RDataFactory.COMPLETE_VECTOR);
    }

    @Override
    protected RDoubleVector internalCreateVector() {
        return populateVectorData(new double[getLength()]);
    }

    @Override
    public RDoubleVector materialize() {
        return this.internalCreateVector();
    }

    @Override
    public RStringVector getImplicitClass() {
        return RDoubleVector.implicitClassHeader;
    }

    @Override
    public RDoubleVector copyResized(int size, boolean fillNA) {
        double[] data = new double[size];
        populateVectorData(data);
        RDoubleVector.resizeData(data, data, getLength(), fillNA);
        return RDataFactory.createDoubleVector(data, !(fillNA && size > getLength()));
    }

    @Override
    public RDoubleVector createEmptySameType(int newLength, boolean newIsComplete) {
        return RDataFactory.createDoubleVector(new double[newLength], newIsComplete);
    }

    @Override
    public String toString() {
        CompilerAsserts.neverPartOfCompilation();
        return "[" + start + " - " + getEnd() + "]";
    }
}
