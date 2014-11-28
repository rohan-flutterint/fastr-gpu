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

                                                                 public class TestrGenBuiltinnrow extends TestBase {

	@Test
	public void testnrow1() {
		assertEval("argv <- structure(list(x = structure(c(0, 3313, 2963, 3175, 3339,     2762, 3276, 2610, 4485, 2977, 3030, 4532, 2753, 3949, 2865,     2282, 2179, 3000, 817, 3927, 1991, 3313, 0, 1318, 1326, 1294,     1498, 2218, 803, 1172, 2018, 1490, 1305, 645, 636, 521, 1014,     1365, 1033, 1460, 2868, 1802, 2963, 1318, 0, 204, 583, 206,     966, 677, 2256, 597, 172, 2084, 690, 1558, 1011, 925, 747,     285, 1511, 1616, 1175, 3175, 1326, 204, 0, 460, 409, 1136,     747, 2224, 714, 330, 2052, 739, 1550, 1059, 1077, 977, 280,     1662, 1786, 1381, 3339, 1294, 583, 460, 0, 785, 1545, 853,     2047, 1115, 731, 1827, 789, 1347, 1101, 1209, 1160, 340,     1794, 2196, 1588, 2762, 1498, 206, 409, 785, 0, 760, 1662,     2436, 460, 269, 2290, 714, 1764, 1035, 911, 583, 465, 1497,     1403, 937, 3276, 2218, 966, 1136, 1545, 760, 0, 1418, 3196,     460, 269, 2971, 1458, 2498, 1778, 1537, 1104, 1176, 2050,     650, 1455, 2610, 803, 677, 747, 853, 1662, 1418, 0, 1975,     1118, 895, 1936, 158, 1439, 425, 328, 591, 513, 995, 2068,     1019, 4485, 1172, 2256, 2224, 2047, 2436, 3196, 1975, 0,     2897, 2428, 676, 1817, 698, 1693, 2185, 2565, 1971, 2631,     3886, 2974, 2977, 2018, 597, 714, 1115, 460, 460, 1118, 2897,     0, 550, 2671, 1159, 2198, 1479, 1238, 805, 877, 1751, 949,     1155, 3030, 1490, 172, 330, 731, 269, 269, 895, 2428, 550,     0, 2280, 863, 1730, 1183, 1098, 851, 457, 1683, 1500, 1205,     4532, 1305, 2084, 2052, 1827, 2290, 2971, 1936, 676, 2671,     2280, 0, 1178, 668, 1762, 2250, 2507, 1799, 2700, 3231, 2937,     2753, 645, 690, 739, 789, 714, 1458, 158, 1817, 1159, 863,     1178, 0, 1281, 320, 328, 724, 471, 1048, 2108, 1157, 3949,     636, 1558, 1550, 1347, 1764, 2498, 1439, 698, 2198, 1730,     668, 1281, 0, 1157, 1724, 2010, 1273, 2097, 3188, 2409, 2865,     521, 1011, 1059, 1101, 1035, 1778, 425, 1693, 1479, 1183,     1762, 320, 1157, 0, 618, 1109, 792, 1011, 2428, 1363, 2282,     1014, 925, 1077, 1209, 911, 1537, 328, 2185, 1238, 1098,     2250, 328, 1724, 618, 0, 331, 856, 586, 2187, 898, 2179,     1365, 747, 977, 1160, 583, 1104, 591, 2565, 805, 851, 2507,     724, 2010, 1109, 331, 0, 821, 946, 1754, 428, 3000, 1033,     285, 280, 340, 465, 1176, 513, 1971, 877, 457, 1799, 471,     1273, 792, 856, 821, 0, 1476, 1827, 1249, 817, 1460, 1511,     1662, 1794, 1497, 2050, 995, 2631, 1751, 1683, 2700, 1048,     2097, 1011, 586, 946, 1476, 0, 2707, 1209, 3927, 2868, 1616,     1786, 2196, 1403, 650, 2068, 3886, 949, 1500, 3231, 2108,     3188, 2428, 2187, 1754, 1827, 2707, 0, 2105, 1991, 1802,     1175, 1381, 1588, 937, 1455, 1019, 2974, 1155, 1205, 2937,     1157, 2409, 1363, 898, 428, 1249, 1209, 2105, 0), .Dim = c(21L,     21L), .Dimnames = list(c(\'Athens\', \'Barcelona\', \'Brussels\',     \'Calais\', \'Cherbourg\', \'Cologne\', \'Copenhagen\', \'Geneva\',     \'Gibraltar\', \'Hamburg\', \'Hook of Holland\', \'Lisbon\', \'Lyons\',     \'Madrid\', \'Marseilles\', \'Milan\', \'Munich\', \'Paris\', \'Rome\',     \'Stockholm\', \'Vienna\'), c(\'Athens\', \'Barcelona\', \'Brussels\',     \'Calais\', \'Cherbourg\', \'Cologne\', \'Copenhagen\', \'Geneva\',     \'Gibraltar\', \'Hamburg\', \'Hook of Holland\', \'Lisbon\', \'Lyons\',     \'Madrid\', \'Marseilles\', \'Milan\', \'Munich\', \'Paris\', \'Rome\',     \'Stockholm\', \'Vienna\')))), .Names = \'x\');"+
			"do.call(\'NROW\', argv)");
	}

}

