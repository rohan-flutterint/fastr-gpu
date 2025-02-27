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
package com.oracle.truffle.r.nodes.profile;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.ReplaceObserver;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;

/**
 * Base class for nodes that are solely executed behind a {@link TruffleBoundary} to ensure that
 * replaces or insertions below this node do not trigger invalidation of compiled code.
 *
 * TODO this is a candidate for Truffle standardization in the future.
 */
public abstract class TruffleBoundaryNode extends RBaseNode implements ReplaceObserver {

    @Override
    public final boolean nodeReplaced(Node oldNode, Node newNode, CharSequence reason) {
        return true;
    }

}
