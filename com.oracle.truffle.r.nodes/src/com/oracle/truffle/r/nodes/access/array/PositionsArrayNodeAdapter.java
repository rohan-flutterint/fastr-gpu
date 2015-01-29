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
package com.oracle.truffle.r.nodes.access.array;

import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.array.ArrayPositionCast.*;
import com.oracle.truffle.r.runtime.RDeparse.*;
import com.oracle.truffle.r.runtime.env.*;

public abstract class PositionsArrayNodeAdapter extends PositionsArrayConversionNodeAdapter {

    @Children protected final RNode[] positions;

    @Override
    public void deparse(State state) {
        for (int i = 0; i < positions.length; i++) {
            positions[i].deparse(state);
            if (i != positions.length - 1) {
                state.append(", ");
            }
        }
    }

    protected static class SubstitutedNodes {
        public final ArrayPositionCast[] elements;
        public final RNode[] positions;
        public final OperatorConverterNode[] operatorConverters;

        SubstitutedNodes(ArrayPositionCast[] elements, RNode[] positions, OperatorConverterNode[] operatorConverters) {
            this.elements = elements;
            this.positions = positions;
            this.operatorConverters = operatorConverters;
        }
    }

    protected SubstitutedNodes substituteComponents(REnvironment env) {
        RNode[] subPositions = new RNode[positions.length];
        for (int i = 0; i < positions.length; i++) {
            subPositions[i] = positions[i].substitute(env);
        }
        // TODO elements and operatorConverters may require change based on what happened to
        // positions
        return new SubstitutedNodes(elements, subPositions, operatorConverters);
    }

    public PositionsArrayNodeAdapter(ArrayPositionCast[] elements, RNode[] positions, OperatorConverterNode[] operatorConverters) {
        super(elements, operatorConverters);
        this.positions = positions;
    }
}
