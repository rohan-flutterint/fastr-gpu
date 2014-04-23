/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.util.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.ReadVariableNodeFactory.BuiltinFunctionVariableNodeFactory;
import com.oracle.truffle.r.nodes.access.ReadVariableNodeFactory.ReadAndCopySuperVariableNodeFactory;
import com.oracle.truffle.r.nodes.access.ReadVariableNodeFactory.ReadLocalVariableNodeFactory;
import com.oracle.truffle.r.nodes.access.ReadVariableNodeFactory.ReadSuperVariableNodeFactory;
import com.oracle.truffle.r.nodes.access.ReadVariableNodeFactory.UnknownVariableNodeFactory;
import com.oracle.truffle.r.nodes.access.FrameSlotNode.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;

public abstract class ReadVariableNode extends RNode implements VisibilityController {

    public abstract Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame);

    public static ReadVariableNode create(Object symbol, boolean shouldCopyValue) {
        return create(symbol, RRuntime.TYPE_ANY, shouldCopyValue);
    }

    public static ReadVariableNode create(Object symbol, boolean shouldCopyValue, boolean isSuper) {
        return create(symbol, RRuntime.TYPE_ANY, shouldCopyValue, isSuper);
    }

    public static ReadVariableNode create(SourceSection src, Object symbol, boolean shouldCopyValue) {
        return create(src, symbol, RRuntime.TYPE_ANY, shouldCopyValue);
    }

    public static ReadVariableNode create(SourceSection src, Object symbol, boolean shouldCopyValue, boolean isSuper) {
        return create(src, symbol, RRuntime.TYPE_ANY, shouldCopyValue, isSuper);
    }

    public static ReadVariableNode create(Object symbol, String mode, boolean shouldCopyValue) {
        return create(symbol, mode, shouldCopyValue, true);
    }

    public static ReadVariableNode create(Object symbol, String mode, boolean shouldCopyValue, boolean isSuper) {
        if (isSuper) {
            return new UnresolvedReadVariableNode(symbol, mode, shouldCopyValue);
        }
        return new UnResolvedReadLocalVariableNode(symbol, mode);
    }

    public static ReadVariableNode create(SourceSection src, Object symbol, String mode, boolean shouldCopyValue) {
        ReadVariableNode rvn = create(symbol, mode, shouldCopyValue);
        rvn.assignSourceSection(src);
        return rvn;
    }

    public static ReadVariableNode create(SourceSection src, Object symbol, String mode, boolean shouldCopyValue, boolean isSuper) {
        ReadVariableNode rvn = create(symbol, mode, shouldCopyValue, isSuper);
        rvn.assignSourceSection(src);
        return rvn;
    }

    protected boolean checkType(Object obj, String type) {
        if (type.equals(RRuntime.TYPE_ANY)) {
            return true;
        }
        if (type.equals(RRuntime.TYPE_FUNCTION) || type.equals(RRuntime.TYPE_CLOSURE) || type.equals(RRuntime.TYPE_BUILTIN) || type.equals(RRuntime.TYPE_SPECIAL)) {
            return obj instanceof RFunction;
        }
        if (type.equals(RRuntime.TYPE_CHARACTER)) {
            return obj instanceof String;
        }
        if (type.equals(RRuntime.TYPE_LOGICAL)) {
            return obj instanceof Byte;
        }
        if (type.equals(RRuntime.TYPE_INTEGER) || type.equals(RRuntime.TYPE_DOUBLE) || type.equals(RRuntime.TYPE_NUMERIC)) {
            return obj instanceof Integer || obj instanceof Double;
        }
        return false;
    }

    public static final class UnresolvedReadVariableNode extends ReadVariableNode {

        private final Object symbol;
        private final String mode;

        /**
         * In case this read operation is the one used to read a vector prior to updating one of its
         * elements, the vector must be copied to the local frame if it is found in an enclosing
         * frame.
         */
        @CompilationFinal private boolean copyValue;

        public void setCopyValue(boolean c) {
            copyValue = c;
        }

        public UnresolvedReadVariableNode(Object symbol, String mode, boolean copyValue) {
            this.symbol = symbol;
            this.mode = mode;
            this.copyValue = copyValue;
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            if (enclosingFrame != null) {
                ReadSuperVariableNode readSuper = copyValue ? ReadAndCopySuperVariableNodeFactory.create(null, new FrameSlotNode.UnresolvedFrameSlotNode(symbol))
                                : ReadSuperVariableNodeFactory.create(null, new FrameSlotNode.UnresolvedFrameSlotNode(symbol));
                ReadVariableMaterializedNode readNode = new ReadVariableMaterializedNode(readSuper, new UnresolvedReadVariableNode(symbol, mode, copyValue), mode);
                return replace(readNode).execute(frame, enclosingFrame);
            } else {
                return replace(resolveNonFrame()).execute(frame);
            }
        }

        private ReadVariableNode resolveNonFrame() {
            RFunction lookupResult = RContext.getLookup().lookup(RRuntime.toString(symbol));
            if (lookupResult != null) {
                return BuiltinFunctionVariableNodeFactory.create(lookupResult);
            } else {
                return UnknownVariableNodeFactory.create(RRuntime.toString(symbol));
            }
        }

        @Override
        public Object execute(VirtualFrame frame) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            ArrayList<Assumption> assumptions = allMissingAssumptions(frame);
            ReadVariableNode readNode;
            if (assumptions == null) {
                // Found variable in one of the frames; build inline cache.
                ReadLocalVariableNode actualReadNode = ReadLocalVariableNodeFactory.create(new FrameSlotNode.UnresolvedFrameSlotNode(symbol));
                readNode = new ReadVariableVirtualNode(actualReadNode, new UnresolvedReadVariableNode(symbol, mode, copyValue), mode);
            } else {
                // Symbol is missing in all frames; bundle assumption checks and access builtin.
                readNode = new ReadVariableNonFrameNode(assumptions, resolveNonFrame(), new UnresolvedReadVariableNode(symbol, mode, copyValue), symbol);
            }
            return replace(readNode).execute(frame);
        }

        private ArrayList<Assumption> allMissingAssumptions(VirtualFrame frame) {
            ArrayList<Assumption> assumptions = new ArrayList<>();
            Frame currentFrame = frame;
            do {
                FrameSlot frameSlot = FrameSlotNode.findFrameSlot(currentFrame, RRuntime.toString(symbol));
                if (frameSlot != null) {
                    assumptions = null;
                    break;
                }
                assumptions.add(FrameSlotNode.getAssumption(currentFrame, symbol));
                currentFrame = RArguments.getEnclosingFrame(currentFrame);
            } while (currentFrame != null);
            return assumptions;
        }
    }

    public static final class ReadVariableNonFrameNode extends ReadVariableNode {

        @Child private ReadVariableNode readNode;
        @Child private UnresolvedReadVariableNode unresolvedNode;
        @Children private final AbsentFrameSlotNode[] absentFrameSlotNodes;
        private final Object symbol;

        ReadVariableNonFrameNode(List<Assumption> assumptions, ReadVariableNode readNode, UnresolvedReadVariableNode unresolvedNode, Object symbol) {
            this.readNode = readNode;
            this.unresolvedNode = unresolvedNode;
            this.absentFrameSlotNodes = wrapAssumptions(assumptions);
            this.symbol = symbol;
        }

        private AbsentFrameSlotNode[] wrapAssumptions(List<Assumption> assumptions) {
            AbsentFrameSlotNode[] nodes = new AbsentFrameSlotNode[assumptions.size()];
            for (int i = 0; i < assumptions.size(); i++) {
                nodes[i] = new AbsentFrameSlotNode(assumptions.get(i), symbol);
            }
            return nodes;
        }

        @ExplodeLoop
        @Override
        public Object execute(VirtualFrame frame) {
            controlVisibility();
            try {
                for (int i = 0; i < absentFrameSlotNodes.length; i++) {
                    absentFrameSlotNodes[i].getAssumption().check();
                }
            } catch (InvalidAssumptionException e) {
                return replace(unresolvedNode).execute(frame);
            }
            return readNode.execute(frame);
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            controlVisibility();
            throw new UnsupportedOperationException();
        }
    }

    public static final class ReadVariableVirtualNode extends ReadVariableNode {

        @Child private ReadLocalVariableNode readNode;
        @Child private ReadVariableNode nextNode;
        private final String mode;

        ReadVariableVirtualNode(ReadLocalVariableNode readNode, ReadVariableNode nextNode, String mode) {
            this.readNode = readNode;
            this.nextNode = nextNode;
            this.mode = mode;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            controlVisibility();
            if (readNode.getFrameSlotNode().hasValue(frame, frame)) {
                Object result = readNode.execute(frame);
                if (checkType(result, mode)) {
                    return result;
                }
            }
            return nextNode.execute(frame, RArguments.getEnclosingFrame(frame));
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            controlVisibility();
            throw new UnsupportedOperationException();
        }
    }

    public static final class UnResolvedReadLocalVariableNode extends ReadVariableNode {

        private final Object symbol;
        private final String mode;
        @Child ReadLocalVariableNode node;

        UnResolvedReadLocalVariableNode(final Object symbol, final String mode) {
            this.symbol = symbol;
            this.mode = mode;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            controlVisibility();
            node = insert(ReadLocalVariableNodeFactory.create(new FrameSlotNode.UnresolvedFrameSlotNode(symbol)));
            if (node.getFrameSlotNode().hasValue(frame, frame)) {
                Object result = node.execute(frame);
                if (checkType(result, mode)) {
                    replace(node);
                    return result;
                }
            }
            return replace(UnknownVariableNodeFactory.create(RRuntime.toString(symbol))).execute(frame);
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            controlVisibility();
            throw new UnsupportedOperationException();
        }
    }

    public static final class ReadVariableSuperMaterializedNode extends ReadVariableNode {
        @Child private ReadVariableNode readNode;

        public ReadVariableSuperMaterializedNode(ReadVariableNode readNode) {
            this.readNode = readNode;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            controlVisibility();
            return readNode.execute(frame, RArguments.get(frame).getEnclosingFrame());
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            controlVisibility();
            throw new UnsupportedOperationException();
        }

        public static ReadVariableNode create(SourceSection src, Object symbol, String mode) {
            return new ReadVariableSuperMaterializedNode(ReadVariableNode.create(src, symbol, mode, false));
        }

    }

    public static final class ReadVariableMaterializedNode extends ReadVariableNode {

        @Child private ReadSuperVariableNode readNode;
        @Child private ReadVariableNode nextNode;
        private final String mode;

        ReadVariableMaterializedNode(ReadSuperVariableNode readNode, ReadVariableNode nextNode, String mode) {
            this.readNode = readNode;
            this.nextNode = nextNode;
            this.mode = mode;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            controlVisibility();
            throw new UnsupportedOperationException();
        }

        @Override
        public Object execute(VirtualFrame frame, MaterializedFrame enclosingFrame) {
            controlVisibility();
            if (readNode.getFrameSlotNode().hasValue(frame, enclosingFrame)) {
                Object result = readNode.execute(frame, enclosingFrame);
                if (checkType(result, mode)) {
                    return result;
                }
            }
            return nextNode.execute(frame, RArguments.getEnclosingFrame(enclosingFrame));
        }
    }

    @NodeChild(value = "frameSlotNode", type = FrameSlotNode.class)
    public abstract static class ReadLocalVariableNode extends ReadVariableNode {

        protected abstract FrameSlotNode getFrameSlotNode();

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public byte doLogical(VirtualFrame frame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return frame.getByte(frameSlot);
        }

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public int doInteger(VirtualFrame frame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return frame.getInt(frameSlot);
        }

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public double doDouble(VirtualFrame frame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return frame.getDouble(frameSlot);
        }

        @Specialization
        public Object doObject(VirtualFrame frame, FrameSlot frameSlot) {
            controlVisibility();
            try {
                return frame.getObject(frameSlot);
            } catch (FrameSlotTypeException e) {
                throw new IllegalStateException();
            }
        }
    }

    @SuppressWarnings("unused")
    @NodeChildren({@NodeChild(value = "enclosingFrame", type = AccessEnclosingFrameNode.class), @NodeChild(value = "frameSlotNode", type = FrameSlotNode.class)})
    public abstract static class ReadSuperVariableNode extends ReadVariableNode {

        protected abstract FrameSlotNode getFrameSlotNode();

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public byte doLogical(VirtualFrame frame, MaterializedFrame enclosingFrame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return enclosingFrame.getByte(frameSlot);
        }

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public int doInteger(VirtualFrame frame, MaterializedFrame enclosingFrame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return enclosingFrame.getInt(frameSlot);
        }

        @Specialization(rewriteOn = FrameSlotTypeException.class)
        public double doDouble(VirtualFrame frame, MaterializedFrame enclosingFrame, FrameSlot frameSlot) throws FrameSlotTypeException {
            controlVisibility();
            return enclosingFrame.getDouble(frameSlot);
        }

        @Specialization
        public Object doObject(VirtualFrame frame, MaterializedFrame enclosingFrame, FrameSlot frameSlot) {
            controlVisibility();
            try {
                return enclosingFrame.getObject(frameSlot);
            } catch (FrameSlotTypeException e) {
                throw new IllegalStateException();
            }
        }
    }

    public abstract static class ReadAndCopySuperVariableNode extends ReadSuperVariableNode {

        @Override
        @Specialization
        public Object doObject(VirtualFrame frame, MaterializedFrame enclosingFrame, FrameSlot frameSlot) {
            controlVisibility();
            try {
                Object result = enclosingFrame.getObject(frameSlot);
                if (result instanceof RAbstractVector) {
                    return ((RAbstractVector) result).copy();
                }
                return result;
            } catch (FrameSlotTypeException e) {
                throw new IllegalStateException();
            }
        }

    }

    @NodeField(name = "function", type = RFunction.class)
    public abstract static class BuiltinFunctionVariableNode extends ReadVariableNode {

        protected abstract RFunction getFunction();

        @Specialization
        public Object doObject(@SuppressWarnings("unused") VirtualFrame frame) {
            controlVisibility();
            return getFunction();
        }
    }

    @NodeField(name = "symbol", type = String.class)
    public abstract static class UnknownVariableNode extends ReadVariableNode {

        protected abstract String getSymbol();

        @Specialization
        public Object doObject(@SuppressWarnings("unused") VirtualFrame frame) {
            controlVisibility();
            throw RError.getUnknownVariable(null, getSymbol());
        }
    }
}
