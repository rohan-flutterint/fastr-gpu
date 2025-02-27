/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.library.gpu;

import uk.ac.ed.accelerator.ocl.OCLRuntimeUtils;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.r.nodes.builtin.RExternalBuiltinNode;

public abstract class MarawaccInitilization extends RExternalBuiltinNode.Arg0 {

    private static boolean initializated = false;

    private static void communicateToMarawaccInit() {
        try {
            OCLRuntimeUtils.waitForTheOpenCLInitialization();
            initializated = true;
        } catch (InterruptedException e) {
            System.err.println("Error during Marawacc Initialization");
            e.printStackTrace();
        }
    }

    public static void marawaccInitialization() {
        if (!initializated) {
            communicateToMarawaccInit();
        }
    }

    /**
     * <code>
     * marawac.init()
     * </code>
     */
    @Specialization
    public int doInitialization() {
        marawaccInitialization();
        MethodInstallation.Installation.installMApply();
        return 0;
    }
}