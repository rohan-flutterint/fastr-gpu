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
package com.oracle.truffle.r.runtime.data.closures;

import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.RAttributeProfiles;
import com.oracle.truffle.r.runtime.data.RFactor;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractIntVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;

/*
 * This closure is meant to be used only for implementation of the binary operators.
 */
public class RFactorToIntVectorClosure extends RToIntVectorClosure implements RAbstractIntVector {

    private final RAbstractIntVector levels;
    private final boolean withNames;

    public RFactorToIntVectorClosure(RFactor factor, RAbstractIntVector levels, boolean withNames) {
        super(factor.getVector());
        assert levels != null;
        this.levels = levels;
        this.withNames = withNames;
    }

    @Override
    public final RAbstractVector castSafe(RType type) {
        switch (type) {
            case Integer:
                return this;
            case Double:
                return new RIntToDoubleVectorClosure(this);
            case Character:
                return new RIntToStringVectorClosure(this);
            case Complex:
                return new RIntToComplexVectorClosure(this);
            default:
                return null;
        }
    }

    public int getDataAt(int index) {
        int val = ((RIntVector) vector).getDataAt(index);
        if (!vector.isComplete() && RRuntime.isNA(val)) {
            return RRuntime.INT_NA;
        } else {
            return levels.getDataAt(val - 1);
        }
    }

    @Override
    public RStringVector getNames(RAttributeProfiles attrProfiles) {
        return withNames ? super.getNames(attrProfiles) : null;
    }

}
