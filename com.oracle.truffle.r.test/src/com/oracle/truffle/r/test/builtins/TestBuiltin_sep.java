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
public class TestBuiltin_sep extends TestBase {

    @Test
    public void testsep1() {
        assertEval("argv <- list(1, 1);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep2() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(-3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L), c(-3L, -3L, -3L, -3L, -3L, -3L, -3L, -3L, -3L, -2L, -2L, -2L, -2L, -2L, -2L, -2L, -2L, -2L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep3() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(NA, 17.4716236802524, 0.424429400003, -2.45474630431729, -8.6855922903657, -11.7956139807344, -8.08147081196715, -13.3123167980156, -1.24650334752019, 21.281002075072, -5.32311940332657, 0.621869751489083, -19.2022951076469, -0.543162784063959, NA, NA, 15.344649382745, -9.74060313555005, 0.149375174081257, -5.85062482591874, -6.90563567110309, -9.96064651628744, 5.6326723568001, -8.78481137542338, -6.01565736147178, -15.543162784064, 2.34681552556734, -13.2465033475202, -3.82901961529671, 1.5226506664314, NA, -5.9777558474085, 22.7534966524798, 15.5010454558094, 4.13857256877024, -11.6855922903657, 11.6768805966734, -7.38893285382193, 10.8527157375375, -11.3889328538219, 14.1493751740813, -0.388932853821931, 13.0835617235859, -1.98225172690947, 5.96273742790618, -1.50975714950164, -1.38893285382193, 9.90772658272184, 7.3144077096343, -12.9822517269095, 2.02855087840155, -4.7956139807344, 3.14937517408126, -10.3231194033266, -2.25730595283121, 2.56685890630474, 4.27019946976097, 5.14937517408126, 0.0285508784015471, 5.85271573753749, 6.73189144185778, -6.38893285382193, 0.0285508784015471, -3.14728426246251, 15.1493751740813, 13.7869022870421, -7.27891116345324, 9.61106714617807, 4.84191313222647, -3.98225172690947, -6.38893285382193, 13.0285508784015, 5.13857256877024, -8.50975714950164, -0.619778839870337, -3.97144912159845, 23.1493751740813, -2.80641658604541, -1.03726257209382, 2.25939686444995, 4.25939686444995, -4.38893285382193, 6.38022116012966, -4.74060313555005, 2.02855087840155, -15.7956139807344, 8.21518862457662, -12.0264599667828, -2.1364816571515, 5.8635183428485, -14.729800530239, 4.80850749766416, -11.7848113754234, 9.45683721593604, -15.2573059528312, 5.28100207507198, 12.8635183428485, 6.50104545580937, 1.55605630099372, -7.44394369900628, 9.9735400332172, -11.2681085581422, 7.44603461062503, -8.14728426246251, -1.72980053023903, -3.90563567110309, 4.56685890630474, -5.37813024851092, -1.25730595283121, 10.7426940471688, NA, NA, 6.24343998511081, -21.9164382764141, -6.1364816571515, -15.8398222206077, -4.12567905184048, -7.94984391097642, -6.4773493335686, -5.65318447443266), .Tsp = c(1945, 1974.75, 4), class = 'ts'), 9.24492052298191);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep4() {
        assertEval("argv <- list(structure(-0.437222043740988, .Names = 'Var2'), 6.28318530717959);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep5() {
        assertEval(Output.ContainsWarning, "argv <- list(structure(integer(0), .Label = character(0), class = 'factor'));`/`(argv[[1]]);");
    }

    @Test
    public void testsep6() {
        assertEval("argv <- list(structure(c(25.1597633136098, 12.8284023668648), .Dim = 2L, .Dimnames = list(c('1', '2'))), c(13L, 13L));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep7() {
        assertEval("argv <- list(1e+05, 3);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep8() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1L, 1L, 1L, 1L, 6L, 2L, 4L, 3L, 7L, 2L, 8L, 4L, 2L, 2L, 1L, 3L, 3L, 4L, 3L, 2L, 1L, 2L, 3L, 1L, 1L, 2L, 1L, 3L, 1L, 2L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 1L, 1L, 1L, 2L, 2L, 4L, 1L, 1L, 1L, 1L, 2L, 1L, 5L, 2L, 1L, 3L, 2L, 1L, 1L, 6L, 2L, 1L, 2L, 5L, 2L, 2L, 2L, 4L, 4L, 1L, 1L, 3L, 4L, 2L, 2L, 2L, 1L, 5L, 4L, 1L, 3L, 1L, 1L, 4L, 2L, 3L, 2L, 1L, 8L, 1L, 5L, 1L, 3L, 4L, 4L, 1L, 3L, 1L, 2L, 6L, 1L, 1L, 1L, 1L, 1L, 6L, 2L, 2L, 1L, 1L, 2L, 3L, 1L, 1L, 1L, 1L), .Dim = 126L, .Dimnames = structure(list(fe = c('1.6', '1.667', '1.7', '1.733', '1.75', '1.783', '1.8', '1.817', '1.833', '1.85', '1.867', '1.883', '1.917', '1.933', '1.95', '1.967', '1.983', '2', '2.017', '2.033', '2.067', '2.083', '2.1', '2.133', '2.15', '2.167', '2.183', '2.2', '2.217', '2.233', '2.25', '2.267', '2.283', '2.3', '2.317', '2.333', '2.35', '2.367', '2.383', '2.4', '2.417', '2.483', '2.617', '2.633', '2.8', '2.883', '2.9', '3.067', '3.317', '3.333', '3.367', '3.417', '3.45', '3.5', '3.567', '3.6', '3.683', '3.717', '3.733', '3.75', '3.767', '3.817', '3.833', '3.85', '3.883', '3.917', '3.95', '3.966', '3.967', '4', '4.033', '4.05', '4.067', '4.083', '4.1', '4.117', '4.133', '4.15', '4.167', '4.183', '4.2', '4.233', '4.25', '4.267', '4.283', '4.3', '4.317', '4.333', '4.35', '4.366', '4.367', '4.383', '4.4', '4.417', '4.433', '4.45', '4.467', '4.483', '4.5', '4.517', '4.533', '4.55', '4.567', '4.583', '4.6', '4.617', '4.633', '4.65', '4.667', '4.7', '4.716', '4.733', '4.75', '4.767', '4.783', '4.8', '4.817', '4.833', '4.85', '4.883', '4.9', '4.933', '5', '5.033', '5.067', '5.1')), .Names = 'fe'), class = 'table'), 272L);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep9() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(-20.7752893729399+0i, -22.2629231778254+0i, 30.2366932497517-0i, -17.7609104766206+0i, -12.009450871146+0i, -20.6744466063748+0i, -16.2509653806178-0i, 14.8872572302678-0i, -2.41214022512376e+00+5e-15i, 30.1945691318138-0i, -14.86107358966-0i, -75.7334659810725-0i, -31.7348183989382+0i, 33.742775143777-0i, 26.1570616797447-0i, 37.7317903854624+0i, -7.20820970337446-0i, 38.6698755921621-0i, -26.4295844393936-0i, 26.3000016960339+0i, -16.3754767271763+0i, -7.29593605495242-0i, 9.19886724090888+0i, -35.3925832738897+0i, 21.0943018303757+0i, 4.90714440628349-0i), 26L);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep10() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(0, 9.88131291682493e-324, 1.03753785626662e-322, 1.02271588689138e-321, 1.02320995253722e-320, 1.02330876566639e-319, 1.02329394369701e-318, 1.02329295556572e-317, 1.02329300497229e-316, 1.02329299015032e-315, 1.02329299212658e-314, 1.0232929922748e-313, 1.02329299227974e-312, 1.02329299228073e-311, 1.02329299228073e-310, 1.02329299228073e-309, 1.02329299228073e-308, 1.02329299228073e-307), 1.02329299228075);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep11() {
        assertEval("argv <- list(structure(c(29.35, 23.32, 23.8, 41.89, 42.19, 31.72, 39.74, 44.75, 46.64, 47.64, 24.42, 46.31, 27.84, 25.06, 23.31, 25.62, 46.05, 47.32, 34.03, 41.31, 31.16, 24.52, 27.01, 41.74, 21.8, 32.54, 25.95, 24.71, 32.61, 45.04, 43.56, 41.18, 44.19, 46.26, 28.96, 31.94, 31.92, 27.74, 21.44, 23.49, 43.42, 46.12, 23.27, 29.81, 46.4, 45.25, 41.12, 28.13, 43.69, 47.2, 2.86999999999999, 4.41, 4.43, 1.67, 0.829999999999999, 2.85, 1.34, 0.669999999999999, 1.06, 1.14, 3.93, 1.19, 2.37, 4.7, 3.35, 3.1, 0.869999999999999, 0.579999999999999, 3.08, 0.959999999999999, 4.19, 3.48, 1.91, 0.909999999999999, 3.73, 2.47, 3.67, 3.25, 3.17, 1.21, 1.2, 1.05, 1.28, 1.12, 2.85, 2.28, 1.52, 2.87, 4.54, 3.73, 1.08, 1.21, 4.46, 3.43, 0.899999999999999, 0.559999999999999, 1.73, 2.72, 2.07, 0.659999999999999, 2329.68000000001, 1507.99, 2108.47, 189.130000000001, 728.470000000001, 2982.88, 662.860000000001, 289.520000000001, 276.650000000001, 471.240000000001, 2496.53, 287.770000000001, 1681.25, 2213.82, 2457.12, 870.85, 289.710000000001, 232.440000000001, 1900.1, 88.940000000001, 1139.95, 1390, 1257.28, 207.680000000001, 2449.39, 601.050000000001, 2231.03, 1740.7, 1487.52, 325.540000000001, 568.560000000001, 220.560000000001, 400.060000000001, 152.010000000001, 579.51, 651.110000000001, 250.960000000001, 768.79, 3299.49, 2630.96, 389.660000000001, 249.870000000001, 1813.93, 4001.89, 813.390000000001, 138.330000000001, 380.470000000001, 766.54, 123.580000000001, 242.690000000001, 2.87000000000001, 3.93, 3.82, 0.219999999999998, 4.56, 2.43, 2.67, 6.51, 3.08, 2.8, 3.99, 2.19, 4.32, 4.52, 3.44, 6.28, 1.48, 3.19, 1.12, 1.54, 2.99, 3.54, 8.21, 5.81, 1.57, 8.12, 3.62, 7.66, 1.76, 2.48, 3.61, 1.03, 0.670000000000002, 2, 7.48, 2.19, 2, 4.35, 3.01, 2.7, 2.96, 1.13, 2.01, 2.45, 0.530000000000004, 5.14, 10.23, 1.88, 16.71, 5.08), .Dim = c(50L, 4L), .Dimnames = list(c('Australia', 'Austria', 'Belgium', 'Bolivia', 'Brazil', 'Canada', 'Chile', 'China', 'Colombia', 'Costa Rica', 'Denmark', 'Ecuador', 'Finland', 'France', 'Germany', 'Greece', 'Guatamala', 'Honduras', 'Iceland', 'India', 'Ireland', 'Italy', 'Japan', 'Korea', 'Luxembourg', 'Malta', 'Norway', 'Netherlands', 'New Zealand', 'Nicaragua', 'Panama', 'Paraguay', 'Peru', 'Philippines', 'Portugal', 'South Africa', 'South Rhodesia', 'Spain', 'Sweden', 'Switzerland', 'Turkey', 'Tunisia', 'United Kingdom', 'United States', 'Venezuela', 'Zambia', 'Jamaica', 'Uruguay', 'Libya', 'Malaysia'), c('pop15', 'pop75', 'dpi', 'ddpi'))), structure(c(29.35, 23.32, 23.8, 41.89, 42.19, 31.72, 39.74, 44.75, 46.64, 47.64, 24.42, 46.31, 27.84, 25.06, 23.31, 25.62, 46.05, 47.32, 34.03, 41.31, 31.16, 24.52, 27.01, 41.74, 21.8, 32.54, 25.95, 24.71, 32.61, 45.04, 43.56, 41.18, 44.19, 46.26, 28.96, 31.94, 31.92, 27.74, 21.44, 23.49, 43.42, 46.12, 23.27, 29.81, 46.4, 45.25, 41.12, 28.13, 43.69, 47.2, 2.87, 4.41, 4.43, 1.67, 0.83, 2.85, 1.34, 0.67, 1.06, 1.14, 3.93, 1.19, 2.37, 4.7, 3.35, 3.1, 0.87, 0.58, 3.08, 0.96, 4.19, 3.48, 1.91, 0.91, 3.73, 2.47, 3.67, 3.25, 3.17, 1.21, 1.2, 1.05, 1.28, 1.12, 2.85, 2.28, 1.52, 2.87, 4.54, 3.73, 1.08, 1.21, 4.46, 3.43, 0.9, 0.56, 1.73, 2.72, 2.07, 0.66, 2329.68, 1507.99, 2108.47, 189.130000000001, 728.470000000001, 2982.88, 662.860000000001, 289.520000000001, 276.650000000001, 471.240000000001, 2496.53, 287.770000000001, 1681.25, 2213.82, 2457.12, 870.85, 289.710000000001, 232.440000000001, 1900.1, 88.9400000000012, 1139.95, 1390, 1257.28, 207.680000000001, 2449.39, 601.050000000001, 2231.03, 1740.7, 1487.52, 325.540000000001, 568.560000000001, 220.560000000001, 400.060000000001, 152.010000000001, 579.51, 651.11, 250.960000000001, 768.79, 3299.49, 2630.96, 389.660000000001, 249.870000000001, 1813.93, 4001.89, 813.390000000001, 138.330000000001, 380.470000000001, 766.54, 123.580000000001, 242.690000000001, 2.87, 3.92999999999999, 3.82, 0.22, 4.56, 2.43, 2.67, 6.51, 3.08, 2.8, 3.99, 2.19, 4.32, 4.52, 3.44, 6.28, 1.48, 3.19, 1.12, 1.54, 2.99, 3.54, 8.21, 5.81, 1.57, 8.12, 3.62, 7.66, 1.76, 2.48, 3.61, 1.03, 0.670000000000002, 2, 7.48, 2.19, 2, 4.35, 3.01, 2.7, 2.96, 1.13, 2.01, 2.45, 0.530000000000002, 5.14, 10.23, 1.88, 16.71, 5.08), .Dim = c(50L, 4L), .Dimnames = list(NULL, c('pop15', 'pop75', 'dpi', 'ddpi'))));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep12() {
        assertEval("argv <- list(1, c(6.25, 36, 36, 56.25, 64, 64, 256, 36, 25, 36, 784, 25, 90.25, 36, 20.25, 100, 196, 9, 20.25, 30.25, 9, 12.25, 36, 4, 9, 16, 36, 25, 42.25, 25, 100, 36, 324, 20.25, 400));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep13() {
        assertEval(Output.ContainsWarning,
                        "argv <- list(structure(list(c0 = structure(integer(0), .Label = character(0), class = 'factor')), .Names = 'c0', row.names = character(0), class = 'data.frame'), structure(list(c0 = structure(integer(0), .Label = character(0), class = 'factor')), .Names = 'c0', row.names = character(0), class = 'data.frame'));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep14() {
        assertEval("argv <- list(structure(list(A = c(52L, 52L, 47L, 45L, 40L, 37L, 27L, 27L, 23L, 22L, 21L, 25L, 24L, 22L, 22L, 20L, 16L, 17L, 14L, 13L, 13L, 14L, 24L), F = c(42L, 44L, 48L, 49L, 50L, 54L, 58L, 54L, 59L, 59L, 60L, 53L, 54L, 55L, 56L, 58L, 62L, 57L, 54L, 55L, 52L, 47L, 56L), M = c(6L, 4L, 5L, 6L, 10L, 9L, 15L, 19L, 18L, 19L, 19L, 22L, 22L, 23L, 22L, 22L, 22L, 26L, 32L, 32L, 35L, 39L, 20L)), .Names = c('A', 'F', 'M'), class = 'data.frame', row.names = c(NA, 23L)), 100);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep15() {
        assertEval("argv <- list(c(1, 0), structure(c(1, 0, 0, 1), .Dim = c(2L, 2L)));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep16() {
        assertEval("argv <- list(structure(c(126.49966838981, 123.340306958365, 124.994330270046, 129.001056705356, 131.639025779016, 124.408594738421, 125.475982014377, 125.929559340094, 126.630542479839, 127.249057014908, 127.661400038288, 128.403617480371, 129.888052364537, 131.702361667407, 133.516670970277, 135.001105854443, 135.990729110554, 137.310226785368, 144.127817178676, 144.648818160919, 147.114894939813, 142.21953431078, 139.936534657354, 152.343274976711), .Tsp = c(1949, 1950.91666666667, 12), class = 'ts'), structure(c(NA, NA, NA, NA, NA, NA, 126.791666666667, 127.25, 127.958333333333, 128.583333333333, 129, 129.75, 131.25, 133.083333333333, 134.916666666667, 136.416666666667, 137.416666666667, 138.75, NA, NA, NA, NA, NA, NA), .Tsp = c(1949, 1950.91666666667, 12), class = 'ts'));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep17() {
        assertEval("argv <- list(structure(c(17L, 29L, 17L, 20L, 1L, 15L, 0L, 1L), .Names = c('1', '2', '3', '4', '5', '6', '7', '8')), structure(c(24L, 29L, 27L, 20L, 12L, 16L, 28L, 4L), .Names = c('1', '2', '3', '4', '5', '6', '7', '8')));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep18() {
        assertEval("argv <- list(c(2, 11, 14, 5, 5, 13, 20, 22, 6, 6, 15, 7, 14, 6, 32, 53, 57, 14, 16, 16, 17, 40, 43, 46, 8, 23, 23, 28, 34, 36, 38, 3, 5, 11, 24, 45, 5, 6, 6, 9, 13, 23, 25, 32, 53, 54, 5, 5, 11, 17, 19, 8, 13, 14, 20, 47, 48, 60, 81, 2, 1, 2, 3, 5, 10, 14, 21, 36, 40, 6, 17, 67, 1, 1, 2, 7, 11, 12, 1, 1, 5, 5, 5, 11, 17, 3, 4, 22, 30, 36, 8, 1, 1, 5, 7, 16, 27, 1, 30, 10, 14, 27, 41, 69, 25, 10, 11, 20, 33, 5, 7, 1, 1, 5, 5, 5, 5, 7, 11, 15, 5, 14, 6, 6, 7, 28, 1, 5, 14, 2, 2, 3, 8, 10, 12, 1, 1, 9, 22, 3, 3, 5, 15, 18, 22, 37), structure(c(7.89366449903379, 7.89366449903379, 7.89366449903379, 14.0829622414182, 14.0829622414182, 14.0829622414182, 14.0829622414182, 14.0829622414182, 9.97467561511911, 9.97467561511911, 9.97467561511911, 9.14061221811198, 9.14061221811198, 37.9214773068363, 37.9214773068363, 37.9214773068363, 37.9214773068363, 27.0457930153774, 27.0457930153774, 27.0457930153774, 27.0457930153774, 27.0457930153774, 27.0457930153774, 27.0457930153774, 27.142857142857, 27.142857142857, 27.142857142857, 27.142857142857, 27.142857142857, 27.142857142857, 27.142857142857, 5.51287534554956, 19.4425820919426, 19.4425820919426, 19.4425820919426, 19.4425820919426, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 22.0114035979981, 12.0888548389297, 12.0888548389297, 12.0888548389297, 12.0888548389297, 12.0888548389297, 35.9372148648542, 35.9372148648542, 35.9372148648542, 35.9372148648542, 35.9372148648542, 35.9372148648542, 35.9372148648542, 35.9372148648542, 2.39853649771773, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 14.5555555555555, 34.0597534472672, 34.0597534472672, 34.0597534472672, 4.94649227492536, 4.94649227492536, 4.94649227492536, 4.94649227492536, 4.94649227492536, 4.94649227492536, 5.86945220571486, 5.86945220571486, 5.86945220571486, 5.86945220571486, 5.86945220571486, 5.86945220571486, 5.86945220571486, 4.2587413846383, 4.2587413846383, 28.3984573891039, 28.3984573891039, 28.3984573891039, 9.29091029891263, 9.29091029891263, 9.29091029891263, 9.29091029891263, 9.29091029891263, 9.29091029891263, 9.29091029891263, 27.2857142857143, 27.2857142857143, 27.2857142857143, 27.2857142857143, 27.2857142857143, 27.2857142857143, 27.2857142857143, 18.193214018835, 20.387398919001, 20.387398919001, 20.387398919001, 20.387398919001, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 6.18095346485027, 10.4914673267343, 10.4914673267343, 10.4914673267343, 10.4914673267343, 10.4914673267343, 10.4914673267343, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 6.30668950814617, 0.75368270571509, 13.5, 13.5, 13.5, 13.5, 13.5, 13.5, 13.5, 13.5, 13.5, 13.5), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100', '101', '102', '103', '104', '105', '106', '107', '108', '109', '110', '111', '112', '113', '114', '115', '116', '117', '118', '119', '120', '121', '122', '123', '124', '125', '126', '127', '128', '129', '130', '131', '132', '133', '134', '135', '136', '137', '138', '139', '140', '141', '142', '143', '144', '145', '146')));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep19() {
        assertEval("argv <- list(0.5, c(576.899196412178, 48.2726847449981, 4.88037826224117, 1.31852084431627, 1.02614578306738));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep20() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42')), 6);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep21() {
        assertEval("argv <- list(structure(c(18, 17, 15, 20, 10, 20, 25, 13, 12), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9')), structure(c(18.3712737525013, 13.3333333333333, 18.2953929141735, 18.3712737525013, 13.3333333333333, 18.2953929141735, 18.3712737525013, 13.3333333333333, 18.2953929141735), .Dim = c(9L, 1L), .Dimnames = list(c('1', '2', '3', '4', '5', '6', '7', '8', '9'), NULL)));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep22() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(0+6.28318530717959i, 0+12.5663706143592i, 0+18.8495559215388i, 0+25.1327412287183i, 0+31.4159265358979i, 0+37.6991118430775i, 0+43.9822971502571i, 0+50.2654824574367i, 0+56.5486677646163i, 0+62.8318530717959i, 0+69.1150383789754i, 0+75.398223686155i, 0+81.6814089933346i, 0+87.9645943005142i, 0+94.2477796076938i, 0+100.530964914873i), 16);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep23() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1, 0, -1, 0.5, -0.5, NA, NA, NA, 0), .Dim = c(3L, 3L)), structure(c(1, 1, 1, 0.707106781186548, 0.707106781186548, 0.707106781186548, 0, 0, 0), .Dim = c(3L, 3L)));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep24() {
        assertEval("argv <- list(structure(c(32, 53, 10, 3, 11, 50, 10, 30, 10, 25, 7, 5, 3, 15, 7, 8, 36, 66, 16, 4, 9, 34, 7, 64, 5, 29, 7, 5, 2, 14, 7, 8), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32')), 18.5);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep25() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')), structure(c(0.570290675249213, 0.61905127646042, 0.531618474288601, 0.790554080720526, 0.767014480135805, 0.550113874748024, 0.17684306547603, 0.582060475541574, 0.82418208155584, 0.508078873703881, 0.235692066937831, 0.434097271866188, 0.494627673369755, 0.72161667900813, 0.573653475332745, 0.56356507508215, 0.69807707842341, 0.78887268067876, 0.566927875165682, 0.661086277504564, 0.469406672743269, 0.365159870153793, 0.474450872868566, 0.63082107675278, 0.131445264348354, 0.640909477003375, 0.503034673578584, 0.565246475123916, 0.403832071114405, 0.577016275416276, 0.543388274580962, 0.239054867021362, 0.573653475332745, 0.514804473870944, 0.674537477838689, 0.0709148628447877, 0.536662674413898, 0.772058680261102, 0.274364267898443, 0.116312663972463, 0.439141471991485, 0.60728147616806, 0.400469271030873, 0.497990473453286, 0.514804473870944, 0.55179527478979, 0.455955472409143, 0.506397473662115, 0.321443469067883, 0.565246475123916, 0.772058680261102, 0.869579882683515, 0.494627673369755, 0.457636872450909, 0.398787870989108, 0.753563279801679, 0.518167273954475, 0.326487669193181, 0.351708669819667, 0.479495072993863, 0.397106470947342, 0.439141471991485, 0.37020407027909, 0.627458276669249, 0.402150671072639, 0.63082107675278, 0.543388274580962, 0.587104675666871, 0.587104675666871, 0.311355068817289, 0.730023679216959, 0.534981274372133, 0.450911272283846, 0.427371671699125, 0.432415871824422, 0.2911782683161, 0.339938869527307, 0.708165478674004, 0.76533308009404, 0.455955472409143, 0.509760273745647, 0.412239071323234, 0.464362472617972, 0.481176473035629, 0.331531869318478, 0.622414076543951, 0.392062270822045, 0.827544881639372, 0.487902073202692, 0.479495072993863, 0.652679277295735, 0.585423275625105, 0.735067879342256, 0.477813672952097, 0.435778671907954, 0.756926079885211, 0.679581677963987, 0.339938869527307, 0.625776876627483, 0.652679277295735), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep26() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(0L, 1L, 1L, 7L, 7L, 2L, 0L, 3L, 9L, 0L, 0L, 0L, 0L, 6L, 0L, 1L, 5L, 8L, 2L, 2L, 0L, 0L, 0L, 9L, 0L, 3L, 0L, 1L, 0L, 2L, 3L, 0L, 0L, 0L, 0L, 0L, 0L, 4L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 2L, 2L, 0L, 0L, 0L, 4L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 2L, 0L, 1L, 0L, 1L, 0L, 0L, 8L, 0L, 0L, 0L, 0L, 0L, 0L, 3L, 4L, 0L, 0L, 0L, 0L, 0L, 0L, 4L, 0L, 4L, 0L, 0L, 4L, 0L, 5L, 0L, 0L, 3L, 3L, 0L, 5L, 2L), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')), structure(c(1.25194092864295, 1.37418430529572, 1.15498928509075, 1.80414376800547, 1.74512972410413, 1.20135746244181, 0.265563337720594, 1.28144795059362, 1.88844954500738, 1.09597524118942, 0.413098447473938, 0.910502531785213, 1.06225293038865, 1.63131692515155, 1.26037150634314, 1.23507977324257, 1.57230288125021, 1.79992847915537, 1.24351035094276, 1.47956652654811, 0.999023597637219, 0.737675688931295, 1.01166946418751, 1.40369132724639, 0.151750538768014, 1.42898306034697, 1.08332937463913, 1.23929506209267, 0.834627332483493, 1.26880208404333, 1.18449630704142, 0.421529025174129, 1.26037150634314, 1.1128363965898, 1.51328883734888, 1.40164574169432e-07, 1.16763515164104, 1.75777559065442, 0.510050091026136, 0.113812939117154, 0.9231483983355, 1.34467728334505, 0.826196754783302, 1.07068350808884, 1.1128363965898, 1.2055727512919, 0.965301286836455, 1.09175995233932, 0.628078178828811, 1.23929506209267, 1.75777559065442, 2.00226234395996, 1.06225293038865, 0.969516575686551, 0.821981465933206, 1.71140741330337, 1.12126697428999, 0.640724045379098, 0.703953378130531, 1.02431533073779, 0.817766177083111, 0.9231483983355, 0.750321555481582, 1.3952607495462, 0.830412043633397, 1.40369132724639, 1.18449630704142, 1.29409381714391, 1.29409381714391, 0.602786445728238, 1.65239336940203, 1.16341986279095, 0.952655420286168, 0.893641376384831, 0.906287242935117, 0.552202979527091, 0.674446356179862, 1.59759461435079, 1.74091443525404, 0.965301286836455, 1.10019053003951, 0.855703776733971, 0.986377731086933, 1.02853061958789, 0.653369911929384, 1.38261488299591, 0.805120310532824, 1.89688012270757, 1.04539177498827, 1.02431533073779, 1.45849008229763, 1.28987852829381, 1.66503923595232, 1.0201000418877, 0.914717820635308, 1.71983799100356, 1.52593470389916, 0.674446356179862, 1.39104546069611, 1.45849008229763), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')));`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep27() {
        assertEval("argv <- list(998.602763134667, 78L);`/`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testsep28() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(0L, 1L, 1L, 7L, 7L, 2L, 0L, 3L, 9L, 0L, 0L, 0L, 0L, 6L, 0L, 1L, 5L, 8L, 2L, 2L, 0L, 0L, 0L, 9L, 0L, 3L, 0L, 1L, 0L, 2L, 3L, 0L, 0L, 0L, 0L, 0L, 0L, 4L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 2L, 2L, 0L, 0L, 0L, 4L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 2L, 0L, 1L, 0L, 1L, 0L, 0L, 8L, 0L, 0L, 0L, 0L, 0L, 0L, 3L, 4L, 0L, 0L, 0L, 0L, 0L, 0L, 4L, 0L, 4L, 0L, 0L, 4L, 0L, 5L, 0L, 0L, 3L, 3L, 0L, 5L, 2L), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')), structure(c(1.501181611548, 1.64776358195565, 1.38492694536262, 2.16332775373429, 2.09256404388232, 1.44052700310346, 0.318416746879368, 1.53656346647399, 2.26441876780853, 1.31416323551065, 0.495326021509292, 1.09176300454732, 1.27372682988096, 1.95609117488209, 1.51129071295543, 1.48096340873315, 1.88532746503012, 2.15827320303057, 1.49107251014058, 1.77412734954845, 1.19790856932527, 0.884526425695124, 1.21307222143641, 1.68314543688164, 0.181943877879142, 1.71347274110391, 1.29899958339952, 1.48601795943686, 1.0007810918805, 1.52139981436285, 1.42030880028861, 0.505435122916716, 1.51129071295543, 1.3343814383255, 1.81456375517815, -1.99474544941847e-05, 1.40009059747376, 2.10772769599345, 0.611580687694671, 0.136452921545733, 1.10692665665846, 1.61238172702967, 0.990671990473078, 1.28383593128838, 1.3343814383255, 1.44558155380717, 1.15747216369558, 1.30910868480694, 0.753108107398609, 1.48601795943686, 2.10772769599345, 2.40089163680876, 1.27372682988096, 1.16252671439929, 0.985617439769366, 2.05212763825262, 1.34449053973293, 0.768271759509746, 0.844090020065427, 1.22823587354755, 0.980562889065654, 1.10692665665846, 0.899690077806261, 1.67303633547421, 0.99572654117679, 1.68314543688164, 1.42030880028861, 1.55172711858512, 1.55172711858512, 0.722780803176337, 1.98136392840065, 1.39503604677005, 1.14230851158444, 1.07154480173247, 1.08670845384361, 0.662126194731792, 0.808708165139443, 1.91565476925239, 2.08750949317861, 1.15747216369558, 1.31921778621437, 1.02605384539906, 1.18274491721414, 1.23329042425126, 0.783435411620882, 1.65787268336308, 0.965399236954518, 2.27452786921595, 1.25350862706611, 1.22823587354755, 1.74885459602989, 1.54667256788141, 1.99652758051179, 1.22318132284384, 1.09681755525103, 2.06223673966004, 1.82972740728929, 0.808708165139443, 1.6679817847705, 1.74885459602989), .Names = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100')));`/`(argv[[1]],argv[[2]]);");
    }
}
