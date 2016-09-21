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
public class TestBuiltin_range extends TestBase {

    @Test
    public void testrange1() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(0.0303542455381287, 0.030376780241572, 0.030376780241572, 0.0317964665585001, 0.0332612222823148, 0.0332612222823148, 0.0332612222823148, 0.0332612222823148, 0.0332612222823148, 0.0332612222823148, 0.0332612222823148, 0.0334189652064179, 0.0352217414818821, 0.0354245538128718, 0.0354245538128718, 0.0376780241572021, 0.0376780241572021, 0.0376780241572021, 0.0376780241572021, 0.0406300703082748, 0.0406300703082748, 0.0406300703082748, 0.0440778799351001, 0.048021453037678, 0.0524607896160087, 0.0524607896160087, 0.0524607896160087, 0.0628267531999279, 0.0693167477915991, 0.0981611681990265, 0.134937804218497, 0.179646655850009, 0.437804218496485), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange2() {
        assertEval("argv <- list(structure(c(52L, 52L), .Names = c('y', 'x')), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange3() {
        assertEval("argv <- list(c(-2.92498527625946, 2.46253591019012), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange4() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(1.4615016373309e+48, 5.70899077082384e+45, 2.23007451985306e+43, 8.71122859317602e+40, 3.40282366920938e+38, 1.32922799578492e+36, 5.19229685853483e+33, 2.02824096036517e+31, 7.92281625142643e+28, 3.09485009821345e+26, 1.20892581961463e+24, 4.72236648286965e+21, 18446744073709551616, 72057594037927936, 281474976710656, 1099511627776, 4294967296, 16777216, 65536, 256, 1, 0.00390625, 1.52587890625e-05, 5.96046447753906e-08, 2.3283064365387e-10, 9.09494701772928e-13, 3.5527136788005e-15, 1.38777878078145e-17, 5.42101086242752e-20, 2.11758236813575e-22, 8.27180612553028e-25), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange5() {
        assertEval("argv <- list(1:3, finite = TRUE, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange6() {
        assertEval("argv <- list(c(1L, 3L, 7L, 14L, 21L, 20L, 19L, 9L, 4L, 2L), 0, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange7() {
        assertEval("argv <- list(c(8.2, 9.7, 12.25, 16.5, 21.5, 14.5, 20, 23.45, 25.8, 27.3, 22.4, 24.5, 25.95, 27.3, 30.9), numeric(0), NULL, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]],argv[[4]]);");
    }

    @Test
    public void testrange8() {
        assertEval(Output.ContainsError, "argv <- list(structure(c(3L, 2L, 1L), .Label = c('A', 'B', 'C'), class = c('ordered', 'factor')), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange9() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(list(sec = c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), min = c(40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L, 50L, 0L, 10L, 20L, 30L, 40L), hour = c(8L, 8L, 9L, 9L, 9L, 9L, 9L, 9L, 10L, 10L, 10L, 10L, 10L, 10L, 11L, 11L, 11L, 11L, 11L, 11L, 12L, 12L, 12L, 12L, 12L, 12L, 13L, 13L, 13L, 13L, 13L, 13L, 14L, 14L, 14L, 14L, 14L, 14L, 15L, 15L, 15L, 15L, 15L, 15L, 16L, 16L, 16L, 16L, 16L, 16L, 17L, 17L, 17L, 17L, 17L, 17L, 18L, 18L, 18L, 18L, 18L, 18L, 19L, 19L, 19L, 19L, 19L, 19L, 20L, 20L, 20L, 20L, 20L, 20L, 21L, 21L, 21L, 21L, 21L, 21L, 22L, 22L, 22L, 22L, 22L, 23L, 23L, 23L, 23L, 23L, 23L, 0L, 0L, 0L, 0L, 0L, 0L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L, 2L, 2L, 2L, 3L, 3L, 3L, 3L, 3L), mday = c(12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 12L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 13L), mon = c(11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L), year = c(90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L, 90L), wday = c(3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L), yday = c(345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 345L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L, 346L), isdst = c(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)), .Names = c('sec', 'min', 'hour', 'mday', 'mon', 'year', 'wday', 'yday', 'isdst'), class = c('POSIXlt', 'POSIXt')), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange10() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(-3.5527136788005e-14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6.21724893790088e-15, -1.77635683940025e-15, -8.88178419700125e-16, -1.33226762955019e-15, -1.22124532708767e-15, -8.88178419700125e-16, -1.33226762955019e-15, -1.22124532708767e-15, -1.33226762955019e-15, -1.11022302462516e-15, -8.88178419700125e-16, -1.33226762955019e-15, -8.88178419700125e-16, -8.88178419700125e-16, -4.44089209850063e-16, -4.44089209850063e-16, -1.22124532708767e-15, -1.22124532708767e-15, -8.88178419700125e-16, -1.33226762955019e-15, -8.88178419700125e-16, -4.44089209850063e-16, -6.66133814775094e-16, -7.7715611723761e-16, -4.44089209850063e-16, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -1.33226762955019e-15, -1.33226762955019e-15, -1.33226762955019e-15, -8.88178419700125e-16, -1.11022302462516e-15, -4.44089209850063e-16, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -1.33226762955019e-15, -1.33226762955019e-15, -8.88178419700125e-16, -4.44089209850063e-16, -1.22124532708767e-15, -8.88178419700125e-16, -8.88178419700125e-16, -8.88178419700125e-16, -1.33226762955019e-15, -1.22124532708767e-15, 5.91171556152403e-12, -1.59161572810262e-12, 4.54747350886464e-13, 1.02318153949454e-12, 1.13686837721616e-12, 4.54747350886464e-13, 9.09494701772928e-13, 9.09494701772928e-13, 1.02318153949454e-12, 9.09494701772928e-13, 0, 9.09494701772928e-13, 4.54747350886464e-13, 0, 0, 3.41060513164848e-13, 9.66338120633736e-13, 1.19371179607697e-12, 2.27373675443232e-13, 9.66338120633736e-13, 2.27373675443232e-13, 0, 4.54747350886464e-13, 9.66338120633736e-13, 0, 6.82121026329696e-13, 4.54747350886464e-13, 2.27373675443232e-13, 4.54747350886464e-13, 1.08002495835535e-12, 1.02318153949454e-12, 8.5265128291212e-13, 7.38964445190504e-13, 9.09494701772928e-13, 4.54747350886464e-13, 5.6843418860808e-13, 7.105427357601e-13, 3.41060513164848e-13, 0, 0, 9.66338120633736e-13, 1.02318153949454e-12, 0, 4.54747350886464e-13, 1.13686837721616e-12, 1.05160324892495e-12, 9.09494701772928e-13, 3.41060513164848e-13, 8.38440428196918e-13, 1.4210854715202e-12, 1.15463194561016e-14, -8.88178419700125e-16, -1.33226762955019e-15, -2.02615701994091e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.77635683940025e-15, 8.88178419700125e-16, 2.22044604925031e-15, 1.77635683940025e-15, 0, 1.33226762955019e-15, 8.88178419700125e-16, 2.22044604925031e-15, 2.22044604925031e-15, 1.77635683940025e-15, 1.77635683940025e-15, 8.88178419700125e-16, 4.44089209850063e-16, 0, 1.77635683940025e-15, 8.88178419700125e-16, 1.77635683940025e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.11022302462516e-15, 1.77635683940025e-15, 1.77635683940025e-15, 1.55431223447522e-15, 1.66533453693773e-15, 2.66453525910038e-15, 8.88178419700125e-16, 8.88178419700125e-16, 8.88178419700125e-16, 8.88178419700125e-16, 8.88178419700125e-16, 8.88178419700125e-16, 1.77635683940025e-15, 2.22044604925031e-15, 8.88178419700125e-16, 2.22044604925031e-15, 3.77475828372553e-15, 1.77635683940025e-15, 0, 1.33226762955019e-15, 0, 2.66453525910038e-15), .Dim = c(50L, 4L), .Dimnames = list(c('Australia', 'Austria', 'Belgium', 'Bolivia', 'Brazil', 'Canada', 'Chile', 'China', 'Colombia', 'Costa Rica', 'Denmark', 'Ecuador', 'Finland', 'France', 'Germany', 'Greece', 'Guatamala', 'Honduras', 'Iceland', 'India', 'Ireland', 'Italy', 'Japan', 'Korea', 'Luxembourg', 'Malta', 'Norway', 'Netherlands', 'New Zealand', 'Nicaragua', 'Panama', 'Paraguay', 'Peru', 'Philippines', 'Portugal', 'South Africa', 'South Rhodesia', 'Spain', 'Sweden', 'Switzerland', 'Turkey', 'Tunisia', 'United Kingdom', 'United States', 'Venezuela', 'Zambia', 'Jamaica', 'Uruguay', 'Libya', 'Malaysia'), c('pop15', 'pop75', 'dpi', 'ddpi'))), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange11() {
        assertEval(Output.ContainsError,
                        "argv <- list(structure(c(4L, 5L, 1L, 5L, 3L, 4L, 5L, 3L, 2L, 4L), .Label = c('a', 'c', 'i', 's', 't'), class = c('ordered', 'factor')), structure(c(4L, 2L, 3L, 5L, 4L, 3L, 5L, 1L, 5L, 4L), .Label = c('a', 'c', 'i', 's', 't'), class = c('ordered', 'factor')), na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange12() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101), .Tsp = c(1, 101, 1), class = 'ts'), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange13() {
        assertEval("argv <- list(c(NA, 1, 2, 3, -Inf, NaN, Inf), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange14() {
        assertEval("argv <- list(c(14.43333, 65.7667), finite = TRUE, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange15() {
        assertEval("argv <- list(structure(c(1, 13, 31), .Dim = 3L, .Dimnames = list(c('1st', '2nd', '3rd'))), finite = TRUE, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange16() {
        assertEval(Ignored.Unknown, "argv <- list(list(), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange17() {
        assertEval(Ignored.Unknown, "argv <- list(structure(c(1012633320L, 1012633620L), class = c('POSIXct', 'POSIXt'), tzone = ''), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange18() {
        assertEval("argv <- list(structure(numeric(0), .Dim = c(0L, 0L)), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange19() {
        assertEval("argv <- list(structure(logical(0), .Dim = c(0L, 0L), .Dimnames = list(NULL, NULL)), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange20() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(2.00256647265648e-308, 2.22284878464869e-308, 2.22507363599982e-308, 2.2250738585072e-308, 2.22507408101459e-308, 2.22729893236571e-308, 2.44758124435792e-308), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange21() {
        assertEval(Ignored.Unknown, "argv <- list(structure(c(12053, 12054, 12055, 12056, 12057, 12058, 12059, 12060, 12061, 12062), class = 'Date'), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange22() {
        assertEval("argv <- list(structure(c(1L, 1L, 2L, 1L, 2L, 2L, 2L, 2L, 2L, 1L, 1L, 1L, 2L, 1L, 1L, 2L, 2L, 1L, 1L, 2L, 1L, 2L, 1L, 1L, 1L, 1L, 2L, 2L, 1L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 1L, 2L, 2L, 2L, 2L, 2L, 1L, 2L, 2L, 2L, 2L, 1L, 2L, 2L, 2L, 1L, 2L, 1L, 1L, 1L, 1L, 2L, 1L, 1L, 2L, 2L, 1L, 2L, 1L, NA, 1L, 1L, 2L, 1L, 1L, NA, 2L, 2L, 2L, 1L, 1L, 2L, 2L, 2L, 1L, 2L, 1L, NA, 2L, 2L, 1L, NA, 2L, 2L, NA, 1L, 2L, 1L, 2L, 2L, 2L, 2L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 1L, 1L, 2L, 2L, 1L, 2L, 1L), .Dim = c(20L, 6L), .Dimnames = list(c('ant', 'bee', 'cat', 'cpl', 'chi', 'cow', 'duc', 'eag', 'ele', 'fly', 'fro', 'her', 'lio', 'liz', 'lob', 'man', 'rab', 'sal', 'spi', 'wha'), c('war', 'fly', 'ver', 'end', 'gro', 'hai'))), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange23() {
        assertEval(Ignored.Unknown, "argv <- list(structure(c(13823, NA), class = 'Date'), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange24() {
        assertEval(Output.ContainsWarning, "range( );");
    }

    @Test
    public void testrange25() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1949, 1949.08333333333, 1949.16666666667, 1949.25, 1949.33333333333, 1949.41666666667, 1949.5, 1949.58333333333, 1949.66666666667, 1949.75, 1949.83333333333, 1949.91666666667, 1950, 1950.08333333333, 1950.16666666667, 1950.25, 1950.33333333333, 1950.41666666667, 1950.5, 1950.58333333333, 1950.66666666667, 1950.75, 1950.83333333333, 1950.91666666667, 1951, 1951.08333333333, 1951.16666666667, 1951.25, 1951.33333333333, 1951.41666666667, 1951.5, 1951.58333333333, 1951.66666666667, 1951.75, 1951.83333333333, 1951.91666666667, 1952, 1952.08333333333, 1952.16666666667, 1952.25, 1952.33333333333, 1952.41666666667, 1952.5, 1952.58333333333, 1952.66666666667, 1952.75, 1952.83333333333, 1952.91666666667, 1953, 1953.08333333333, 1953.16666666667, 1953.25, 1953.33333333333, 1953.41666666667, 1953.5, 1953.58333333333, 1953.66666666667, 1953.75, 1953.83333333333, 1953.91666666667, 1954, 1954.08333333333, 1954.16666666667, 1954.25, 1954.33333333333, 1954.41666666667, 1954.5, 1954.58333333333, 1954.66666666667, 1954.75, 1954.83333333333, 1954.91666666667, 1955, 1955.08333333333, 1955.16666666667, 1955.25, 1955.33333333333, 1955.41666666667, 1955.5, 1955.58333333333, 1955.66666666667, 1955.75, 1955.83333333333, 1955.91666666667, 1956, 1956.08333333333, 1956.16666666667, 1956.25, 1956.33333333333, 1956.41666666667, 1956.5, 1956.58333333333, 1956.66666666667, 1956.75, 1956.83333333333, 1956.91666666667, 1957, 1957.08333333333, 1957.16666666667, 1957.25, 1957.33333333333, 1957.41666666667, 1957.5, 1957.58333333333, 1957.66666666667, 1957.75, 1957.83333333333, 1957.91666666667, 1958, 1958.08333333333, 1958.16666666667, 1958.25, 1958.33333333333, 1958.41666666667, 1958.5, 1958.58333333333, 1958.66666666667, 1958.75, 1958.83333333333, 1958.91666666667, 1959, 1959.08333333333, 1959.16666666667, 1959.25, 1959.33333333333, 1959.41666666667, 1959.5, 1959.58333333333, 1959.66666666667, 1959.75, 1959.83333333333, 1959.91666666667, 1960, 1960.08333333333, 1960.16666666667, 1960.25, 1960.33333333333, 1960.41666666667, 1960.5, 1960.58333333333, 1960.66666666667, 1960.75, 1960.83333333333, 1960.91666666667, 1961, 1961.08333333333, 1961.16666666667, 1961.25, 1961.33333333333, 1961.41666666667, 1961.5, 1961.58333333333, 1961.66666666667, 1961.75, 1961.83333333333, 1961.91666666667, 1962, 1962.08333333333, 1962.16666666667, 1962.25, 1962.33333333333, 1962.41666666667, 1962.5, 1962.58333333333, 1962.66666666667, 1962.75, 1962.83333333333, 1962.91666666667), .Tsp = c(1949, 1962.91666666667, 12), class = 'ts'), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange26() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(1.47191076131574, 0.586694550701453, 0.258706725324317, 0.948371836939988, 0.396080061109718, 0.350912037541581), finite = TRUE, na.rm = FALSE);range(argv[[1]],argv[[2]],argv[[3]]);");
    }

    @Test
    public void testrange27() {
        assertEval("argv <- list(structure(c(-11.3814849918875, -11.9361690778798, 0.562602893455921, 11.5126028934559, 76.2209544348296, -8.66448499188751, -6.94502893455923, -5.28148499188751, -35.7665182531098, 6.35497106544077, -9.20908119253651, -0.898484991887508, -5.59380090589508, -6.12730922120065, -13.3061334505138, 58.6278831800973, -15.1098009058951, -8.29625696322337, -4.07211681990265, 3.7096551514332, 2.60151500811249, 6.24733923742563, -1.33911681990266, -2.14157287723094, -10.5984849918875, -8.12802893455923, 1.30028697944835, -15.7450289345592, 7.20569077879935, -12.6484849918875, 25.1810423201731, -4.42680090589508, -1.90886979448351), .Names = c('Craig Dunain', 'Ben Rha', 'Ben Lomond', 'Goatfell', 'Bens of Jura', 'Cairnpapple', 'Scolty', 'Traprain', 'Lairig Ghru', 'Dollar', 'Lomonds', 'Cairn Table', 'Eildon Two', 'Cairngorm', 'Seven Hills', 'Knock Hill', 'Black Hill', 'Creag Beag', 'Kildcon Hill', 'Meall Ant-Suidhe', 'Half Ben Nevis', 'Cow Hill', 'N Berwick Law', 'Creag Dubh', 'Burnswark', 'Largo Law', 'Criffel', 'Acmony', 'Ben Nevis', 'Knockfarrel', 'Two Breweries', 'Cockleroi', 'Moffat Chase')), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange28() {
        assertEval("argv <- list(c(9.5367431640625e-07, 1.9073486328125e-06, 3.814697265625e-06, 7.62939453125e-06, 1.52587890625e-05, 3.0517578125e-05, 6.103515625e-05, 0.0001220703125, 0.000244140625, 0.00048828125, 0.0009765625, 0.001953125, 0.00390625, 0.0078125, 0.015625, 0.03125, 0.0625, 0.125, 0.25, 0.5, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange29() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(c(1208822400, 1209168000, 1208822400, 1209168000), class = c('POSIXct', 'POSIXt'), tzone = 'GMT'), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange30() {
        assertEval(Ignored.Unknown,
                        "argv <- list(c(1.86606598307361, 339033474310168, 6.15968019059533e+28, 1.11911250438065e+43, 2.03324321833028e+57, 3.69406826275609e+71, 6.71151400229846e+85, 1.21937162496937e+100, 2.2153975381282e+114, 4.02501267984465e+128, 7.31278553581751e+142, 1.32861276588395e+157, 2.41387071044804e+171, 4.38560576593759e+185, 7.96792382084694e+199, 1.44764060891943e+214, 2.63012470966353e+228, 4.77850368783602e+242, 8.6817546752692e+256, 1.57733192575377e+271), na.rm = FALSE);range(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testrange31() {
        assertEval("argv <- list(structure(c(1, 0.666666666666667, 0.333333333333333, 0, -0.333333333333333, -0.666666666666667, -1, -1.33333333333333, -1.66666666666667, 1.5, 1, 0.5, 0, -0.5, -1, -1.5, -2, -2.5, 3, 2, 1, 0, -1, -2, -3, -4, -5, -Inf, -Inf, -Inf, NaN, Inf, Inf, Inf, Inf, Inf, -3, -2, -1, 0, 1, 2, 3, 4, 5, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2, 2.5, -1, -0.666666666666667, -0.333333333333333, 0, 0.333333333333333, 0.666666666666667, 1, 1.33333333333333, 1.66666666666667, -0.75, -0.5, -0.25, 0, 0.25, 0.5, 0.75, 1, 1.25, -0.6, -0.4, -0.2, 0, 0.2, 0.4, 0.6, 0.8, 1), .Dim = c(9L, 9L)), na.rm = TRUE);range(argv[[1]],argv[[2]]);");
    }
}
