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
package com.oracle.truffle.r.nodes.access;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrument.WrapperNode;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.r.nodes.instrument.wrappers.WriteSuperVariableNodeWrapper;
import com.oracle.truffle.r.runtime.RDeparse.State;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.RSerialize;
import com.oracle.truffle.r.runtime.VisibilityController;
import com.oracle.truffle.r.runtime.env.REnvironment;
import com.oracle.truffle.r.runtime.nodes.NeedsWrapper;
import com.oracle.truffle.r.runtime.nodes.RNode;
import com.oracle.truffle.r.runtime.nodes.RSyntaxNode;

/**
 * The "syntax" variant corresponding to {@code x <<- y} in the source.
 */
@NodeInfo(cost = NodeCost.NONE)
@NeedsWrapper
public class WriteSuperVariableNode extends WriteVariableNodeSyntaxHelper implements RSyntaxNode, VisibilityController {

    @Child WriteVariableNode writeSuperFrameVariableNode;

    public static WriteSuperVariableNode create(SourceSection src, String name, RNode rhs) {
        WriteSuperVariableNode result = new WriteSuperVariableNode();
        result.assignSourceSection(src);
        result.writeSuperFrameVariableNode = result.insert(WriteSuperFrameVariableNode.create(name, rhs, Mode.REGULAR));
        return result;
    }

    @Override
    @NeedsWrapper
    public Object getName() {
        return writeSuperFrameVariableNode.getName();
    }

    @Override
    @NeedsWrapper
    public RNode getRhs() {
        return writeSuperFrameVariableNode.getRhs();
    }

    @Override
    @NeedsWrapper
    public Object execute(VirtualFrame frame) {
        Object result = writeSuperFrameVariableNode.execute(frame);
        forceVisibility(false);
        return result;
    }

    @Override
    @NeedsWrapper
    public void execute(VirtualFrame frame, Object value) {
        writeSuperFrameVariableNode.execute(frame, value);
    }

    @Override
    public void deparseImpl(State state) {
        state.startNodeDeparse(this);
        deparseHelper(state, " <<- ");
        state.endNodeDeparse(this);
    }

    @Override
    public void serializeImpl(RSerialize.State state) {
        serializeHelper(state, "<<-");
    }

    public RSyntaxNode substituteImpl(REnvironment env) {
        throw RInternalError.unimplemented();
    }

    public int getRlengthImpl() {
        return 3;
    }

    @Override
    public Object getRelementImpl(int index) {
        return getRelementHelper("<<-", index);
    }

    @Override
    public boolean getRequalsImpl(RSyntaxNode other) {
        throw RInternalError.unimplemented();
    }

    @Override
    public WrapperNode createRWrapperNode() {
        return new WriteSuperVariableNodeWrapper(this);
    }

}
