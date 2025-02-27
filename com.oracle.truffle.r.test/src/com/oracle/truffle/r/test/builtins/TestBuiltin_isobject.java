/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2012-2014, Purdue University
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.builtins;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;

// Checkstyle: stop line length check
public class TestBuiltin_isobject extends TestBase {

    @Test
    public void testisobject1() {
        assertEval("argv <- list(c(45.0314849918875, 57.5361690778799, 61.7043971065441, 61.7043971065441, 128.39604556517, 45.0314849918875, 36.6950289345592, 45.0314849918875, 228.43351825311, 36.6950289345592, 74.2090811925365, 45.0314849918875, 32.5268009058951, 78.3773092212007, 111.723133450514, 20.0221168199027, 32.5268009058951, 40.8632569632234, 20.0221168199027, 24.1903448485668, 45.0314849918875, 11.6856607625744, 20.0221168199027, 28.3585728772309, 45.0314849918875, 36.6950289345592, 49.1997130205517, 36.6950289345592, 78.3773092212007, 45.0314849918875, 145.068957679827, 32.5268009058951, 161.741869794484));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject2() {
        assertEval("argv <- list(structure(c(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L), .Label = c('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59'), class = 'factor'));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject3() {
        assertEval("argv <- list(c(-2.97525100835805, -2.82075793382799, -2.66593061478436, -2.51078653265898, -2.35534314690423, -2.19961777085284, -2.04362765822923, -1.88739011884942, -1.73092311663886, -1.57424503752904, -1.41737428587374, -1.26032952797003, -1.10312969205829, -0.945793720579289, -0.788340724666015, -0.630790076924443, -0.473161172617641, -0.315473462875692, -0.157746535777026, -4.9960036108132e-16, 0.157746535777027, 0.315473462875696, 0.473161172617647, 0.630790076924451, 0.788340724666025, 0.9457937205793, 1.1031296920583, 1.26032952797003, 1.41737428587374, 1.57424503752905, 1.73092311663887, 1.88739011884943, 2.04362765822924, 2.19961777085286, 2.35534314690424, 2.510786532659, 2.66593061478437, 2.82075793382801, 2.97525100835807));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject4() {
        assertEval("argv <- list(structure(c(-0.266501885293689, -3.8931863588937, -0.272681427089914, 0.685830427280619, 0.146697567632144, -0.178509228122189, -0.827954133483467, -0.00847153748452917, -0.439077730128206, -0.100627762490786, 1.90697806298419, 0.534191446603769, 0.118116488305486, 0.266107216595585, 1.09677961111435, 0.294604712451767, 1.26527267402907, -1.37468346842381, -0.501152044022612, 0.277514706049866, 0.080458897112638, 0.0436443543043109, -0.480973816036986, 1.25647294144768, 0.371150285558408), .Dim = 25L, .Dimnames = list(    c('1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'))));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject5() {
        assertEval("argv <- list(c(0.3814, 1.0281, 0.3814, 0.1202, -0.3385, 0.6757, 0.6757, -0.5422, -1.0914, 1.0281, -0.3385, 0.3814, 0.3814, -1.0914, -0.7326, 0.6757, 1.5128, 0.6757, 1.0281, 0.6757, 1.0281, -1.0914, -0.7326, 0.1202, -0.3385, 0.3814, 0.6757, 1.5128, 0.1202, -0.7326, 1.5128, -1.0914, 0.6757, 2.4608, 0.3814, 0.6757, -0.8176, 1.0281, 0.1202, 2.4404, -0.5422, 0.1202, -0.1182, 0.3814, -0.5422, -0.5422, 0.6757, -0.3385, -1.6508, 0.6757, -0.7326, -0.1182, -1.0914, 0.3814, 0.3814, -0.1182, 0.3814, 0.1202, 0.6757, 0.6757, 0.1202, -0.5422, 1.5128, 0.3814, -1.2692, 1.0281, 0.6757, 0.1202, -0.5422, 0.3814, -0.3385, 1.0281, -0.1182, 0.1202, 1.5128, 2.4608, 0.6757, -1.0914, -0.9144, -0.3385, 0.1202, 0.1202, 0.1202, 0.3814, -0.13, -0.5422, 0.1202, -0.1182, 0.3814, 1.0281, 0.6757, 1.0281, -1.2692, 0.1202, 0.3814, 1.5128, -0.1182, 0.3814, 1.0281, 0.3814, 0.1202, -0.3385, 0.6757, 0.6757, -0.5422, -1.0914, 1.0281, -0.3385, 0.3814, 0.3814, -1.0914, -0.7326, 0.6757, 1.5128, 0.6757, 1.0281, 0.6757, 1.0281, -1.0914, -0.7326, 0.1202, -0.3385, 0.3814, 0.6757, 1.5128, 0.1202, -0.7326, 1.5128, -1.0914, 0.6757, 2.4608, 0.3814, 0.6757, -0.8176, 1.0281, 0.1202, 2.4404, -0.5422, 0.1202, -0.1182, 0.3814, -0.5422, -0.5422, 0.6757, -0.3385, -1.6508, 0.6757, -0.7326, -0.1182, -1.0914, 0.3814, 0.3814, -0.1182, 0.3814, 0.1202, 0.6757, 0.6757, 0.1202, -0.5422, 1.5128, 0.3814, -1.2692, 1.0281, 0.6757, 0.1202, -0.5422, 0.3814, -0.3385, 1.0281, -0.1182, 0.1202, 1.5128, 2.4608, 0.6757, -1.0914, -0.9144, -0.3385, 0.1202, 0.1202, 0.1202, 0.3814, -0.13, -0.5422, 0.1202, -0.1182, 0.3814, 1.0281, 0.6757, 1.0281, -1.2692, 0.1202, 0.3814, 1.5128, -0.1182, 0.3814, 1.0281, 0.3814, 0.1202, -0.3385, 0.6757, 0.6757, -0.5422, -1.0914, 1.0281, -0.3385, 0.3814, 0.3814, -1.0914, -0.7326, 0.6757, 1.5128, 0.6757, 1.0281, 0.6757, 1.0281, -1.0914, -0.7326, 0.1202, -0.3385, 0.3814, 0.6757, 1.5128, 0.1202, -0.7326, 1.5128, -1.0914, 0.6757, 2.4608, 0.3814, 0.6757, -0.8176, 1.0281, 0.1202, 2.4404, -0.5422, 0.1202, -0.1182, 0.3814, -0.5422, -0.5422, 0.6757, -0.3385, -1.6508, 0.6757, -0.7326, -0.1182, -1.0914, 0.3814, 0.3814, -0.1182, 0.3814, 0.1202, 0.6757, 0.6757, 0.1202, -0.5422, 1.5128, 0.3814, -1.2692, 1.0281, 0.6757, 0.1202, -0.5422, 0.3814, -0.3385, 1.0281, -0.1182, 0.1202, 1.5128, 2.4608, 0.6757, -1.0914, -0.9144, -0.3385, 0.1202, 0.1202, 0.1202, 0.3814, -0.13, -0.5422, 0.1202, -0.1182, 0.3814, 1.0281, 0.6757, 1.0281, -1.2692, 0.1202, 0.3814, 1.5128, -0.1182, NA, NA, NA));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject6() {
        assertEval("argv <- list(structure(3.14159265358979, class = structure('3.14159265358979', class = 'testit')));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject7() {
        assertEval("argv <- list(structure(c(-0.00544194018731062, -0.00542949133552226, -1.20718999105839e-05, -0.00505497198006266, -0.827687885653788, -0.00315385274195005, -0.0023164952286401, -0.00117183915211372, -2.09167441982205, -0.00193959227691399, -0.00358084102808485, -3.39138861812986e-05, -0.00163051710052444, -0.00168735925488057, -0.0167253073891896, -0.237074502262169, -0.0118967636015583, -0.00307437031103621, -0.00114371252369823, -0.000860763872820255, -0.00028432076263802, -0.00329557354736053, -0.000123683950933913, -0.00026114238659798, -0.00471892942651347, -0.00317288091968884, -6.76955217513137e-05, -0.0119061189538054, -0.00233356124758579, -0.00672098496026968, -0.134965372025281, -0.00102115420103838, -0.00114816901125044), .Names = c('Craig Dunain', 'Ben Rha', 'Ben Lomond', 'Goatfell', 'Bens of Jura', 'Cairnpapple', 'Scolty', 'Traprain', 'Lairig Ghru', 'Dollar', 'Lomonds', 'Cairn Table', 'Eildon Two', 'Cairngorm', 'Seven Hills', 'Knock Hill', 'Black Hill', 'Creag Beag', 'Kildcon Hill', 'Meall Ant-Suidhe', 'Half Ben Nevis', 'Cow Hill', 'N Berwick Law', 'Creag Dubh', 'Burnswark', 'Largo Law', 'Criffel', 'Acmony', 'Ben Nevis', 'Knockfarrel', 'Two Breweries', 'Cockleroi', 'Moffat Chase')));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject8() {
        assertEval("argv <- list(c(FALSE, FALSE));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject9() {
        assertEval("argv <- list(structure(2L, .Label = c('McNeil', 'R Core', 'Ripley', 'Tierney', 'Tukey', 'Venables'), class = 'factor'));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject10() {
        assertEval("argv <- list(structure(c(-0.562441486309934, -0.588967592535822, 0.0277608937997097, 0.568074124752969, 3.89980510825846, -0.428174866497729, -0.343990813420242, -0.260996370058754, -2.31774610938305, 0.314764947225063, -0.455124436264437, -0.0444006414474544, -0.27748974692001, -0.303134023269405, -0.670168347915028, 2.92643313367, -0.749546667806845, -0.410394401887929, -0.203261263063707, 0.1847365997012, 0.128559671155683, 0.313558179929332, -0.0668425264405297, -0.106427678524531, -0.523747793519006, -0.402585404761851, 0.0642079595716389, -0.779859286629166, 0.356484381211739, -0.625053119472271, 1.31547628490512, -0.21959878152752, -0.102402088986461), .Names = c('Craig Dunain', 'Ben Rha', 'Ben Lomond', 'Goatfell', 'Bens of Jura', 'Cairnpapple', 'Scolty', 'Traprain', 'Lairig Ghru', 'Dollar', 'Lomonds', 'Cairn Table', 'Eildon Two', 'Cairngorm', 'Seven Hills', 'Knock Hill', 'Black Hill', 'Creag Beag', 'Kildcon Hill', 'Meall Ant-Suidhe', 'Half Ben Nevis', 'Cow Hill', 'N Berwick Law', 'Creag Dubh', 'Burnswark', 'Largo Law', 'Criffel', 'Acmony', 'Ben Nevis', 'Knockfarrel', 'Two Breweries', 'Cockleroi', 'Moffat Chase')));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject11() {
        assertEval("argv <- list(integer(0));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject12() {
        assertEval("argv <- list(c(25, 50, 100, 250, 500, 1e+05));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject13() {
        assertEval("argv <- list(numeric(0));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject14() {
        assertEval("argv <- list(c(TRUE, FALSE));is.object(argv[[1]]);");
    }

    @Test
    public void testisobject16() {
        assertEval("argv <- list(1:3);do.call('is.object', argv)");
    }

    @Test
    public void testIsObject() {
        assertEval("{ is.object(1) }");
        assertEval("{ is.object(1L) }");
        assertEval("{ is.object(1:3) }");
        assertEval("{ is.object(c(1,2,3)) }");
        assertEval("{ is.object(NA) }");
        assertEval("{ is.object(NULL) }");
    }
}
