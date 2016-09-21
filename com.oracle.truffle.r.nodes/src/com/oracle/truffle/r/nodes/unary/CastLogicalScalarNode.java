/*
 * Copyright (c) 2015, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.nodes.unary;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.data.RComplexVector;
import com.oracle.truffle.r.runtime.data.RLogicalVector;
import com.oracle.truffle.r.runtime.data.RRawVector;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractDoubleVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractIntVector;

public abstract class CastLogicalScalarNode extends CastLogicalBaseNode {

    public abstract byte executeByte(Object o);

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doLogicalVector(RLogicalVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doIntVector(RAbstractIntVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doDoubleVector(RAbstractDoubleVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doStringVector(RStringVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doComplexVector(RComplexVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Specialization(guards = "operand.getLength() > 0")
    protected byte doRawVectorDims(RRawVector operand) {
        return (byte) castLogicalRecursive(operand.getDataAt(0));
    }

    @Fallback
    protected byte castLogical(@SuppressWarnings("unused") Object o) {
        // for non-atomic structures and vectors of length 0
        return RRuntime.LOGICAL_NA;
    }

    public static CastLogicalScalarNode create() {
        return CastLogicalScalarNodeGen.create(false, false, false);
    }
}
