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
package com.oracle.truffle.r.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.r.nodes.unary.CastComplexNode;
import com.oracle.truffle.r.nodes.unary.CastComplexNodeGen;
import com.oracle.truffle.r.nodes.unary.CastDoubleNode;
import com.oracle.truffle.r.nodes.unary.CastDoubleNodeGen;
import com.oracle.truffle.r.nodes.unary.CastIntegerNode;
import com.oracle.truffle.r.nodes.unary.CastIntegerNodeGen;
import com.oracle.truffle.r.nodes.unary.CastListNode;
import com.oracle.truffle.r.nodes.unary.CastListNodeGen;
import com.oracle.truffle.r.nodes.unary.CastLogicalNode;
import com.oracle.truffle.r.nodes.unary.CastLogicalNodeGen;
import com.oracle.truffle.r.nodes.unary.CastRawNode;
import com.oracle.truffle.r.nodes.unary.CastRawNodeGen;
import com.oracle.truffle.r.nodes.unary.CastStringNode;
import com.oracle.truffle.r.nodes.unary.CastStringNodeGen;
import com.oracle.truffle.r.runtime.data.RList;

public abstract class RCastingBuiltinNode extends RBuiltinNode {

    @Child private CastComplexNode castComplex;
    @Child private CastDoubleNode castDouble;
    @Child private CastIntegerNode castInteger;
    @Child private CastLogicalNode castLogical;
    @Child private CastStringNode castString;
    @Child private CastRawNode castRaw;
    @Child private CastListNode castList;

    protected Object castComplex(Object operand, boolean preserveAllAttr) {
        if (castComplex == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castComplex = insert(CastComplexNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castComplex.execute(operand);
    }

    protected Object castDouble(Object operand, boolean preserveAllAttr) {
        if (castDouble == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castDouble = insert(CastDoubleNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castDouble.execute(operand);
    }

    protected Object castInteger(Object operand, boolean preserveAllAttr) {
        if (castInteger == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castInteger = insert(CastIntegerNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castInteger.execute(operand);
    }

    protected Object castLogical(Object operand, boolean preserveAllAttr) {
        if (castLogical == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castLogical = insert(CastLogicalNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castLogical.execute(operand);
    }

    protected Object castString(Object operand, boolean preserveAllAttr) {
        if (castString == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castString = insert(CastStringNodeGen.create(false, true, preserveAllAttr, preserveAllAttr));
        }
        return castString.execute(operand);
    }

    protected Object castRaw(Object operand, boolean preserveAllAttr) {
        if (castRaw == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castRaw = insert(CastRawNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castRaw.execute(operand);
    }

    protected RList castList(Object operand, boolean preserveAllAttr) {
        if (castList == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castList = insert(CastListNodeGen.create(true, preserveAllAttr, preserveAllAttr));
        }
        return castList.executeList(operand);
    }
}
