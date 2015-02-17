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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.r.runtime.*;

/**
 * An instance of {@link RFunction} represents a function defined in R. The properties of a function
 * are as follows:
 * <ul>
 * <li>The {@link #name} is optional. It is given only for builtins.
 * <li>The {@link #target} represents the actually callable entry point to the function.
 * <li>Functions may represent builtins; this is indicated by the {@link #builtin} flag set to the
 * associated {@link RBuiltin} instance.
 * <li>The lexically enclosing environment of this function's definition is referenced by
 * {@link #enclosingFrame}.
 * </ul>
 */
public final class RFunction extends RScalar implements RAttributable {

    private final String name;
    private final RootCallTarget target;
    private final RBuiltin builtin;
    @CompilationFinal private StableValue<MaterializedFrame> enclosingFrame;
    protected RAttributes attributes;

    RFunction(String name, RootCallTarget target, RBuiltin builtin, MaterializedFrame enclosingFrame) {
        this.name = name;
        this.target = target;
        this.builtin = builtin;
        this.enclosingFrame = new StableValue<>(enclosingFrame, "RFunction enclosing frame");
    }

    public boolean isBuiltin() {
        return builtin != null;
    }

    public RBuiltin getRBuiltin() {
        return builtin;
    }

    public String getName() {
        return name;
    }

    public RootCallTarget getTarget() {
        return target;
    }

    public RootNode getRootNode() {
        return target.getRootNode();
    }

    public MaterializedFrame getEnclosingFrame() {
        while (true) {
            StableValue<MaterializedFrame> value = enclosingFrame;
            try {
                value.getAssumption().check();
            } catch (InvalidAssumptionException e) {
                // in this case execution fell back to the interpreter
                continue;
            }
            return value.getValue();
        }
    }

    public void setEnclosingFrame(MaterializedFrame frame) {
        if (enclosingFrame.getValue() != frame) {
            enclosingFrame.getAssumption().invalidate();
            enclosingFrame = new StableValue<>(frame, "RFunction enclosing frame");
        }
    }

    public RFunction copy() {
        return new RFunction(name, target, builtin, enclosingFrame.getValue());
    }

    public RAttributes initAttributes() {
        if (attributes == null) {
            attributes = RAttributes.create();
        }
        return attributes;
    }

    public RAttributes getAttributes() {
        return attributes;
    }

    private static final RStringVector FUNCTION = RDataFactory.createStringVectorFromScalar(RType.Function.getName());

    @Override
    public RStringVector getClassAttr() {
        RStringVector v = RAttributable.super.getClassAttr();
        if (v == null) {
            return FUNCTION;
        } else {
            return v;
        }
    }

}
