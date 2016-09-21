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

import static com.oracle.truffle.r.runtime.RBuiltinKind.PRIMITIVE;
import static com.oracle.truffle.r.runtime.RDispatch.INTERNAL_GENERIC;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.utilities.ConditionProfile;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.runtime.RBuiltin;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;

@RBuiltin(name = "dim", kind = PRIMITIVE, parameterNames = {"x"}, dispatch = INTERNAL_GENERIC)
@SuppressWarnings("unused")
public abstract class Dim extends RBuiltinNode {

    @Specialization
    protected RNull dim(RNull vector) {
        controlVisibility();
        return RNull.instance;
    }

    @Specialization
    protected RNull dim(int vector) {
        controlVisibility();
        return RNull.instance;
    }

    @Specialization
    protected RNull dim(double vector) {
        controlVisibility();
        return RNull.instance;
    }

    @Specialization
    protected RNull dim(byte vector) {
        controlVisibility();
        return RNull.instance;
    }

    @Specialization
    protected Object dim(RAbstractContainer container, //
                    @Cached("createBinaryProfile()") ConditionProfile hasDimensionsProfile) {
        controlVisibility();
        if (hasDimensionsProfile.profile(container.hasDimensions())) {
            return RDataFactory.createIntVector(container.getDimensions(), RDataFactory.COMPLETE_VECTOR);
        } else {
            return RNull.instance;
        }
    }
}
