/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 * 
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, Oracle and/or its affiliates
 * All rights reserved.
 */
package com.oracle.truffle.r.test.testrgen;

import org.junit.*;

import com.oracle.truffle.r.test.*;

// Checkstyle: stop line length check

                                                                 public class TestrGenBuiltinshQuote extends TestBase {

	@Test
    @Ignore
	public void testshQuote1() {
		assertEval("argv <- structure(list(string = c(\'ABC\', \'\\'123\\'\', \'a'b\'), type = \'cmd\'),     .Names = c(\'string\', \'type\'));"+
			"do.call(\'shQuote\', argv)");
	}

}
