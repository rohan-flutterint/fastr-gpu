/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.builtins;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;

// Checkstyle: stop line length check

public class TestBuiltin_provideDimnames extends TestBase {

    @Test
    public void testprovideDimnames1() {
        assertEval("argv <- structure(list(x = structure(logical(0), .Dim = 0:1)),     .Names = 'x');do.call('provideDimnames', argv)");
    }

    @Test
    public void testprovideDimnames2() {
        assertEval("argv <- structure(list(x = structure(integer(0), .Dim = 0L, .Dimnames = structure(list(NULL),     .Names = ''), class = 'table')), .Names = 'x');"
                        + "do.call('provideDimnames', argv)");
    }

}
