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

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.r.runtime.RBuiltin;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.VirtualEvalFrame;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.env.frame.FrameSlotChangeMonitor;

/**
 * An instance of {@link RFunction} represents a function defined in R. The properties of a function
 * are as follows:
 * <ul>
 * <li>The {@link #name} is optional. It is only set initially for builtins (required).
 * <li>The {@link #target} represents the actually callable entry point to the function.
 * <li>Functions may represent builtins; this is indicated by the {@link #builtin} flag set to the
 * associated {@link RBuiltin} instance.
 * <li>The lexically enclosing environment of this function's definition is referenced by
 * {@link #enclosingFrame}.
 * </ul>
 */
public final class RFunction extends RAttributeStorage implements RTypedValue, TruffleObject {

    public static final String NO_NAME = new String("");
    private String name;
    private final RootCallTarget target;
    private final RBuiltinDescriptor builtin;
    private final boolean containsDispatch;

    private FastPathFactory fastPath;

    @CompilationFinal private MaterializedFrame enclosingFrame;

    RFunction(String name, RootCallTarget target, RBuiltinDescriptor builtin, MaterializedFrame enclosingFrame, FastPathFactory fastPath, boolean containsDispatch) {
        this.target = target;
        this.builtin = builtin;
        this.name = name;
        this.fastPath = fastPath;
        if (!isBuiltin() && name != NO_NAME) {
            // If we have a name, propagate it to the rootnode
            RContext.getRRuntimeASTAccess().setFunctionName(getRootNode(), name);
        }
        this.containsDispatch = containsDispatch;
        this.enclosingFrame = enclosingFrame instanceof VirtualEvalFrame ? ((VirtualEvalFrame) enclosingFrame).getOriginalFrame() : enclosingFrame;
    }

    @Override
    public RType getRType() {
        return isBuiltin() ? RType.Builtin : RType.Closure;
    }

    public boolean isBuiltin() {
        return builtin != null;
    }

    public RBuiltinDescriptor getRBuiltin() {
        return builtin;
    }

    public boolean containsDispatch() {
        return containsDispatch;
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
        return enclosingFrame;
    }

    /**
     * Used by the {@code environment<-} builtin.
     */
    public void setEnclosingFrame(MaterializedFrame enclosingFrame) {
        assert !(enclosingFrame instanceof VirtualEvalFrame);
        this.enclosingFrame = enclosingFrame;
        FrameDescriptor descriptor = target.getRootNode().getFrameDescriptor();
        FrameSlotChangeMonitor.getOrInitializeEnclosingFrameAssumption(null, descriptor, null, enclosingFrame);
        FrameSlotChangeMonitor.getOrInitializeEnclosingFrameDescriptorAssumption(null, descriptor, enclosingFrame.getFrameDescriptor());
    }

    private static final RStringVector implicitClass = RDataFactory.createStringVectorFromScalar(RType.Function.getName());

    @Override
    public RStringVector getImplicitClass() {
        return implicitClass;
    }

    @Override
    public String toString() {
        return target.toString();
    }

    public ForeignAccess getForeignAccess() {
        return RContext.getEngine().getForeignAccess(this);
    }

    public FastPathFactory getFastPath() {
        return fastPath;
    }

    public void setFastPath(FastPathFactory fastPath) {
        this.fastPath = fastPath;
    }
}
