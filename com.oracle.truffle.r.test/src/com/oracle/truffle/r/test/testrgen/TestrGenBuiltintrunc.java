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

public class TestrGenBuiltintrunc extends TestBase {

    @Test
    public void testtrunc1() {
        assertEval("argv <- list(8.5);trunc(argv[[1]]);");
    }

    @Test
    public void testtrunc2() {
        assertEval("argv <- list(2819.50000004);trunc(argv[[1]]);");
    }

    @Test
    public void testtrunc3() {
        assertEval("argv <- list(c(4.71622523386031, 1.10082330182195, 1.6348679328803, 1.0848926147446, 1.90544273355044, 0.359849020605907, 3.11383110354654, 0.867268502479419, 0.162947811186314, 0.450064289616421, 4.9159701296594, 4.6105394908227, 3.44404035480693, 1.26481729443185, 1.04007117450237, 2.98928162781522, 0.598357603885233, 3.89095719670877, 2.72964489413425, 2.9838975192979, 0.972001742338762, 3.45619874307886, 3.40639955131337, 1.64102643262595, 2.35728174913675, 0.473953454056755, 4.98228391283192, 4.91887083626352, 0.210240299347788, 2.26199432276189, 3.70630375458859, 3.81391524686478, 0.606464599259198, 4.40811770269647, 4.44661358138546, 2.80862170271575, 3.86814354802482, 3.88661664212123, 2.99017415847629, 2.74575827643275, 0.309161052573472, 3.80168808856979, 0.44893383863382, 4.61025935830548, 0.267928446410224, 2.24770340253599, 2.89542144862935, 0.558472302509472, 1.01390165626071, 4.68876871396787, 3.99585635983385, 0.118613908998668, 0.0555002887267619, 3.01412270753644, 1.23142387834378, 1.36282247491181, 4.64942723163404, 0.578164426842704, 2.22724793478847, 1.08748292084783, 1.14620470674708, 4.12017436814494, 0.320054858457297, 2.23438119865023, 4.76558442227542, 3.10512124677189, 1.74187473836355, 0.650008224183694, 3.97324822610244, 1.69624235597439, 4.7321886930149, 2.33042042935267, 0.96714960061945, 0.195004806155339, 0.781808936735615, 0.248751927865669, 1.19189711171202, 1.64329304476269, 4.17560710804537, 3.12169580138288, 4.66810682089999, 1.36349227512255, 0.602594048250467, 2.95277393539436, 3.86122465948574, 2.54265206633136, 4.36057312530465, 0.599795600865036, 0.397377072367817, 3.41722437064163, 0.29663014691323, 2.19461180153303, 4.06796077964827, 0.185917691560462, 2.69324880791828, 1.27729995292611, 2.07541133742779, 2.81013442203403, 0.629334823461249, 2.81195943942294));trunc(argv[[1]]);");
    }

    @Test
    public void testtrunc4() {
        assertEval("argv <- list(c(-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4));trunc(argv[[1]]);");
    }
}