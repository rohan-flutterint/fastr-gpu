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
package com.oracle.truffle.r.test.library.base;

import org.junit.*;

import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.test.*;

/**
 * Tests for error handling, i.e., setting error handlers using {@code options(error=...)}. The
 * tests in this class reset the {@code error} option to keep the error handler from sticking around
 * and irritating later-running tests that involve errors.
 */
public class TestSimpleErrorHandling extends TestBase {

    @Override
    protected void afterMicroTest() {
        ROptions.addOption("error", RNull.instance);
    }

    @Test
    public void testError() {
        assertEval("{ options(error=quote(cat(23,'\\n'))) ; v }");
        assertEval("{ x <- 2 ; options(error=quote(cat(x,'\\n'))) ; v }");
        // make sure the error handler has been reset
        assertEval("{ nonExistentVariable }");
    }
}
