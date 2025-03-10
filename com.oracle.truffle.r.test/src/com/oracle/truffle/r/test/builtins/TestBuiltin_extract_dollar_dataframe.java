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

public class TestBuiltin_extract_dollar_dataframe extends TestBase {

    @Test
    public void testextract_dollar_dataframe1() {
        assertEval("argv <- structure(list(x = structure(list(distance = c(26, 25,     29, 31, 21.5, 22.5, 23, 26.5, 23, 22.5, 24, 27.5, 25.5, 27.5,     26.5, 27, 20, 23.5, 22.5, 26, 24.5, 25.5, 27, 28.5, 22, 22,     24.5, 26.5, 24, 21.5, 24.5, 25.5, 23, 20.5, 31, 26, 27.5,     28, 31, 31.5, 23, 23, 23.5, 25, 21.5, 23.5, 24, 28, 17, 24.5,     26, 29.5, 22.5, 25.5, 25.5, 26, 23, 24.5, 26, 30, 22, 21.5,     23.5, 25, 21, 20, 21.5, 23, 21, 21.5, 24, 25.5, 20.5, 24,     24.5, 26, 23.5, 24.5, 25, 26.5, 21.5, 23, 22.5, 23.5, 20,     21, 21, 22.5, 21.5, 22.5, 23, 25, 23, 23, 23.5, 24, 20, 21,     22, 21.5, 16.5, 19, 19, 19.5, 24.5, 25, 28, 28), age = c(8,     10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14,     8, 10, 12, 14, 8, 10, 12, 14, 8, 10, 12, 14), Subject = structure(c(15L,     15L, 15L, 15L, 3L, 3L, 3L, 3L, 7L, 7L, 7L, 7L, 14L, 14L,     14L, 14L, 2L, 2L, 2L, 2L, 13L, 13L, 13L, 13L, 5L, 5L, 5L,     5L, 6L, 6L, 6L, 6L, 11L, 11L, 11L, 11L, 16L, 16L, 16L, 16L,     4L, 4L, 4L, 4L, 8L, 8L, 8L, 8L, 9L, 9L, 9L, 9L, 10L, 10L,     10L, 10L, 12L, 12L, 12L, 12L, 1L, 1L, 1L, 1L, 20L, 20L, 20L,     20L, 23L, 23L, 23L, 23L, 25L, 25L, 25L, 25L, 26L, 26L, 26L,     26L, 21L, 21L, 21L, 21L, 19L, 19L, 19L, 19L, 22L, 22L, 22L,     22L, 24L, 24L, 24L, 24L, 18L, 18L, 18L, 18L, 17L, 17L, 17L,     17L, 27L, 27L, 27L, 27L), .Label = c('M16', 'M05', 'M02',     'M11', 'M07', 'M08', 'M03', 'M12', 'M13', 'M14', 'M09', 'M15',     'M06', 'M04', 'M01', 'M10', 'F10', 'F09', 'F06', 'F01', 'F05',     'F07', 'F02', 'F08', 'F03', 'F04', 'F11'), class = c('ordered',     'factor')), Sex = structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L,     1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L,     1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L,     1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L,     1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L,     2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L,     2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L,     2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L), .Label = c('Male',     'Female'), class = 'factor')), .Names = c('distance', 'age',     'Subject', 'Sex'), row.names = c('1', '2', '3', '4', '5',     '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16',     '17', '18', '19', '20', '21', '22', '23', '24', '25', '26',     '27', '28', '29', '30', '31', '32', '33', '34', '35', '36',     '37', '38', '39', '40', '41', '42', '43', '44', '45', '46',     '47', '48', '49', '50', '51', '52', '53', '54', '55', '56',     '57', '58', '59', '60', '61', '62', '63', '64', '65', '66',     '67', '68', '69', '70', '71', '72', '73', '74', '75', '76',     '77', '78', '79', '80', '81', '82', '83', '84', '85', '86',     '87', '88', '89', '90', '91', '92', '93', '94', '95', '96',     '97', '98', '99', '100', '101', '102', '103', '104', '105',     '106', '107', '108'), outer = ~Sex, class = c('nfnGroupedData',     'nfGroupedData', 'groupedData', 'data.frame'), formula = distance ~     age | Subject, labels = structure(list(x = 'Age', y = 'Distance from pituitary to pterygomaxillary fissure'),     .Names = c('x', 'y')), units = structure(list(x = '(yr)',     y = '(mm)'), .Names = c('x', 'y')), FUN = function(x) max(x,     na.rm = TRUE), order.groups = TRUE), name = 'age'), .Names = c('x',     'name'));"
                        + "do.call('$.data.frame', argv)");
    }

}
