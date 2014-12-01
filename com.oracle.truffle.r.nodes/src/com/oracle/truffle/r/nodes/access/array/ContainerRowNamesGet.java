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
package com.oracle.truffle.r.nodes.access.array;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;

// TODO: this should not be necessary once data frame access operators are implemented in R
// which likely makes potential refactoring of this code redundant
@NodeChild(value = "op")
public abstract class ContainerRowNamesGet extends RNode {

    public abstract Object execute(VirtualFrame frame, RAbstractContainer container);

    // this is copied from Attr.java - can't use it directly due to dependence circularity and it
    // does not seem worth doing refactoring considering that data frame access code should be
    // ultimately supported in R
    private static Object getFullRowNames(Object a) {
        if (a == RNull.instance) {
            return RNull.instance;
        } else {
            RAbstractVector rowNames = (RAbstractVector) a;
            Object res = rowNames.getElementClass() == RInt.class && rowNames.getLength() == 2 && RRuntime.isNA(((RAbstractIntVector) rowNames).getDataAt(0)) ? RDataFactory.createIntSequence(1, 1,
                            Math.abs(((RAbstractIntVector) rowNames).getDataAt(1))) : a;
            return res;
        }
    }

    @Specialization(guards = "!isDataFrame")
    Object getRowNames(RAbstractContainer container) {
        return container.getRowNames();
    }

    @Specialization(guards = "isDataFrame")
    Object getRowNamesDataFrame(RAbstractContainer container) {
        return getFullRowNames(container.getRowNames());
    }

    protected boolean isDataFrame(RAbstractContainer container) {
        return container.getElementClass() == RDataFrame.class;
    }

}