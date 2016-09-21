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
public class TestBuiltin_islanguage extends TestBase {

    @Test
    public void testislanguage1() {
        assertEval("argv <- list(c(3.14159265358988, 3.14159265358988, 3.14159265358983, 3.14159265358982, 3.14159265358974, 3.14159265358989, 3.14159265358976, 3.14159265358993, 3.14159265358997, 3.14159265358984, 3.14159265358969, 3.14159265358989, 3.14159265358977, 3.14159265358964, 3.14159265358982, 3.14159265358969, 3.14159265358968, 3.1415926535898, 3.14159265358961, 3.14159265358967, 3.14159265358983, 3.14159265358997, 3.14159265358987, 3.14159265358995, 3.14159265358992, 3.14159265358996, 3.14159265358965, 3.14159265358964, 3.14159265358997, 3.14159265358968, 3.14159265358995, 3.14159265358961, 3.14159265358993, 3.14159265358985, 3.14159265358996, 3.14159265358964, 3.1415926535898, 3.1415926535896, 3.14159265358964, 3.14159265358994, 3.14159265358964, 3.14159265358962, 3.14159265358985, 3.14159265358962, 3.14159265358977, 3.14159265358973, 3.14159265358969, 3.14159265358987, 3.14159265358978, 3.14159265358965, 3.14159265358991, 3.14159265358997, 3.14159265358979, 3.1415926535897, 3.14159265358974, 3.14159265358977, 3.14159265358985, 3.14159265358982, 3.14159265358981, 3.14159265358984, 3.14159265358991, 3.14159265358989, 3.14159265358978, 3.14159265358967, 3.1415926535899, 3.14159265358998, 3.14159265358992, 3.14159265358972, 3.14159265358984, 3.14159265358974, 3.14159265358969, 3.14159265358984, 3.14159265358983, 3.14159265358995, 3.14159265358963, 3.14159265358996, 3.14159265358976, 3.14159265358973, 3.14159265358995, 3.14159265358965, 3.14159265358966, 3.1415926535898, 3.14159265358965, 3.14159265358992, 3.14159265358959, 3.14159265358988, 3.14159265358988, 3.14159265358974, 3.14159265358994, 3.14159265358996, 3.1415926535897, 3.14159265358973, 3.14159265358971, 3.14159265358986, 3.14159265358998, 3.14159265358984, 3.14159265358988, 3.1415926535896, 3.1415926535897, 3.14159265358985, 3.14159265358983));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage2() {
        assertEval("argv <- list(structure(c(FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE), .Dim = 16:17));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage3() {
        assertEval("argv <- list(structure(c(-5.96781464124519, -6.49437440734601, -3.09795335180399, -6.0516983940436, 2.94181419227242, 1.32243907887975, -6.14000748997388, -1.17705131190311), .Dim = c(4L, 2L), .Dimnames = list(c('Murder', 'Assault', 'UrbanPop', 'Rape'), c('PC1', 'PC2'))));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage4() {
        assertEval("argv <- list(structure(3.14159265358979, class = structure('3.14159265358979', class = 'testit')));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage6() {
        assertEval("argv <- list(1.79769313486232e+308);is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage7() {
        assertEval("argv <- list(c('2001-01-01', NA, NA, '2004-10-26'));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage8() {
        assertEval("argv <- list(c(0+0i, 0.00809016994374947-0.00587785252292473i, 0.0161803398874989-0.0117557050458495i, 0.0242705098312484-0.0176335575687742i, 0.0323606797749979-0.0235114100916989i, 0.0404508497187474-0.0293892626146237i, 0.0485410196624968-0.0352671151375484i, 0.0566311896062463-0.0411449676604731i, 0.0647213595499958-0.0470228201833979i, 0.0728115294937453-0.0529006727063226i, 0.0809016994374947-0.0587785252292473i, 0.0889918693812442-0.0646563777521721i, 0.0970820393249937-0.0705342302750968i, 0.105172209268743-0.076412082798022i, 0.113262379212493-0.082289935320946i, 0.121352549156242-0.088167787843871i, 0.129442719099992-0.094045640366796i, 0.137532889043741-0.09992349288972i, 0.14562305898749-0.105801345412645i, 0.15371322893124-0.11167919793557i, 0.16180339887499-0.117557050458495i, 0.169893568818739-0.123434902981419i, 0.177983738762488-0.129312755504344i, 0.186073908706238-0.135190608027269i, 0.194164078649987-0.141068460550194i, 0.202254248593737-0.146946313073118i, 0.210344418537486-0.152824165596043i, 0.218434588481236-0.158702018118968i, 0.226524758424985-0.164579870641893i, 0.234614928368735-0.170457723164817i, 0.242705098312484-0.176335575687742i, 0.250795268256234-0.182213428210667i, 0.258885438199983-0.188091280733591i, 0.266975608143733-0.193969133256516i, 0.275065778087482-0.199846985779441i, 0.283155948031232-0.205724838302366i, 0.291246117974981-0.21160269082529i, 0.29933628791873-0.217480543348215i, 0.30742645786248-0.22335839587114i, 0.315516627806229-0.229236248394065i, 0.323606797749979-0.235114100916989i, 0.331696967693728-0.240991953439914i, 0.339787137637478-0.246869805962839i, 0.347877307581227-0.252747658485764i, 0.355967477524977-0.258625511008688i, 0.364057647468726-0.264503363531613i, 0.372147817412476-0.270381216054538i, 0.380237987356225-0.276259068577462i, 0.388328157299975-0.282136921100387i, 0.396418327243724-0.288014773623312i, 0.404508497187474-0.293892626146237i, 0.412598667131223-0.299770478669161i, 0.420688837074973-0.305648331192086i, 0.428779007018722-0.311526183715011i, 0.436869176962472-0.317404036237936i, 0.444959346906221-0.32328188876086i, 0.453049516849971-0.329159741283785i, 0.46113968679372-0.33503759380671i, 0.469229856737469-0.340915446329634i, 0.477320026681219-0.346793298852559i, 0.485410196624968-0.352671151375484i, 0.493500366568718-0.358549003898409i, 0.501590536512467-0.364426856421333i, 0.509680706456217-0.370304708944258i, 0.517770876399966-0.376182561467183i, 0.525861046343716-0.382060413990108i, 0.533951216287465-0.387938266513032i, 0.542041386231215-0.393816119035957i, 0.550131556174964-0.399693971558882i, 0.558221726118714-0.405571824081807i, 0.566311896062463-0.411449676604731i, 0.574402066006213-0.417327529127656i, 0.582492235949962-0.423205381650581i, 0.590582405893712-0.429083234173506i, 0.598672575837461-0.43496108669643i, 0.60676274578121-0.440838939219355i, 0.61485291572496-0.44671679174228i, 0.62294308566871-0.452594644265205i, 0.631033255612459-0.458472496788129i, 0.639123425556208-0.464350349311054i, 0.647213595499958-0.470228201833979i, 0.655303765443707-0.476106054356903i, 0.663393935387457-0.481983906879828i, 0.671484105331206-0.487861759402753i, 0.679574275274956-0.493739611925678i, 0.687664445218705-0.499617464448602i, 0.695754615162455-0.505495316971527i, 0.703844785106204-0.511373169494452i, 0.711934955049954-0.517251022017377i, 0.720025124993703-0.523128874540301i, 0.728115294937453-0.529006727063226i, 0.736205464881202-0.534884579586151i, 0.744295634824952-0.540762432109075i, 0.752385804768701-0.546640284632i, 0.76047597471245-0.552518137154925i, 0.7685661446562-0.55839598967785i, 0.77665631459995-0.564273842200774i, 0.784746484543699-0.570151694723699i, 0.792836654487448-0.576029547246624i, 0.800926824431198-0.581907399769549i, 0.809016994374947-0.587785252292473i));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage9() {
        assertEval("argv <- list(structure(c(-0.148741651280925, -0.200659450546418, -0.0705810742857073, -0.356547323513813, -0.214670164989233, -0.161150909262745, -0.0362121726544447, -0.259637310505756, -0.142667503568732, -0.113509274827518, -0.0362121726544447, -0.221848749616356, -0.0809219076239261, -0.0969100130080564, 0, -0.113509274827518, -0.0362121726544447, 0, 0.0934216851622351, 0, 0.0644579892269184, 0.113943352306837, 0.161368002234975, 0.0969100130080564, 0.100370545117563, 0.139879086401236, 0.269512944217916, 0.193124598354462, 0.184691430817599, 0.201397124320452, 0.262451089730429, 0.269512944217916, 0.184691430817599, 0.315970345456918, 0.369215857410143, 0.352182518111362, 0.334453751150931, 0.385606273598312, 0.431363764158987, 0.352182518111362, 0.445604203273598, 0.534026106056135, 0.56702636615906, 0.556302500767287, 0.556302500767287, 0.635483746814912, 0.635483746814912, 0.607455023214668, 0.686636269262293, 0.702430536445525, 0.702430536445525, 0.644438589467839, 0.746634198937579, 0.76715586608218, 0.817565369559781, 0.725094521081469, 0.780317312140151, 0.8055008581584, 0.840733234611807, 0.76715586608218, 0.840733234611807, 0.888740960682893, 0.893761762057943, 0.786751422145561, 0.888740960682893, 0.949877704036875, 0.91803033678488, 0.835056101720116, 0.979548374704095, 1.0111473607758, 0.979548374704095, 0.94101424370557, 1.07481644064517, 1.08134730780413, 1.08457627793433, 0.949877704036875, 1.14736710779379, 1.11260500153457, 1.17172645365323, 0.999565488225982, 1.20951501454263, 1.16643011384328, 1.20466251174822, 1.06483221973857), .Tsp = c(1960, 1980.75, 4), class = 'ts'));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage10() {
        assertEval("argv <- list(c(0, 0, 0, 0, 0, 1.75368801162502e-134, 0, 0, 0, 2.60477585273833e-251, 1.16485035372295e-260, 0, 1.53160350210786e-322, 0.333331382328728, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.44161262707711e-123, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.968811545398e-173, 0, 8.2359965384697e-150, 0, 0, 0, 0, 6.51733217171341e-10, 0, 2.36840184577368e-67, 0, 9.4348408357524e-307, 0, 1.59959906013771e-89, 0, 8.73836857865034e-286, 7.09716190970992e-54, 0, 0, 0, 1.530425353017e-274, 8.57590058044551e-14, 0.333333106397154, 0, 0, 1.36895217898448e-199, 2.0226102635783e-177, 5.50445388209462e-42, 0, 0, 0, 0, 1.07846402051283e-44, 1.88605464411243e-186, 1.09156111051203e-26, 0, 3.0702877273237e-124, 0.333333209689785, 0, 0, 0, 0, 0, 0, 3.09816093866831e-94, 0, 0, 4.7522727332095e-272, 0, 0, 2.30093251441394e-06, 0, 0, 1.27082826644707e-274, 0, 0, 0, 0, 0, 0, 0, 4.5662025456054e-65, 0, 2.77995853978268e-149, 0, 0, 0));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage11() {
        assertEval("argv <- list(numeric(0));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage12() {
        assertEval("argv <- list(expression(sqrt(abs(`Standardized residuals`))));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage13() {
        assertEval("argv <- list(structure(c(1+2i, 5+0i, 3-4i, -6+0i), .Dim = c(2L, 2L)));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage14() {
        assertEval("argv <- list(c(NA, NA, 0, NA, NA, NA, 0, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, 0, NA, NA, NA, NA, NA, 0, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, 0, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage15() {
        assertEval("argv <- list(c(3L, 3L, NA, 3L, 4L, 3L, NA, 3L, 3L, 3L, 3L, 2L, 3L, 3L, 4L, 3L, 2L, 2L, 3L, 5L, 2L, 2L, 2L, 4L, 3L, 3L, 3L, 3L, 4L, 4L, 3L, 3L, 4L, 3L, 4L, 3L, 3L, 4L, 3L, 1L, 3L, 3L, 5L, 3L, NA, 2L, 4L, 1L, 3L, 3L, NA, 2L, 5L, 3L, 4L, 4L, 5L, 4L, 4L, 3L, 5L, 4L, 4L, NA, 3L, 5L, 5L, 5L, 5L, 4L, 5L, 4L, 4L, 5L));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage16() {
        assertEval("argv <- list(structure(list(nationality = structure(c(1L, 2L, 2L, 3L, 3L, 1L), .Label = c('Australia', 'UK', 'US'), class = 'factor'), deceased = structure(c(1L, 1L, 1L, 1L, 2L, 1L), .Label = c('no', 'yes'), class = 'factor'), title = structure(c(3L, 6L, 7L, 4L, 2L, 5L), .Label = c('An Introduction to R', 'Exploratory Data Analysis', 'Interactive Data Analysis', 'LISP-STAT', 'Modern Applied Statistics ...', 'Spatial Statistics', 'Stochastic Simulation'), class = 'factor'), other.author = structure(c(NA, NA, NA, NA, NA, 1L), .Label = c('Ripley', 'Venables & Smith'), class = 'factor')), .Names = c('nationality', 'deceased', 'title', 'other.author'), class = 'data.frame', row.names = c(NA, -6L)));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage17() {
        assertEval("argv <- list(structure(list(x = c(0.3, 3.6, 6.2, 3.8, 3.1, 4.1, 6), y = c(6.1, 6.2, 5.2, 2.3, 1.1, 0.8, 0.1)), .Names = c('x', 'y'), row.names = c(1L, 4L, 12L, 31L, 37L, 48L, 50L), class = 'data.frame'));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage18() {
        assertEval("argv <- list(c('1', '2', NA));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage19() {
        assertEval("argv <- list(structure(c(9, 13, 13, 18, 23, 28, 31, 34, 45, 48, 161, 5, 5, 8, 8, 12, 16, 23, 27, 30, 33, 43, 45, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1), .Dim = c(23L, 2L), .Dimnames = list(NULL, c('time', 'status')), type = 'right', class = 'Surv'));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage20() {
        assertEval("argv <- list(c(0.923879532511287+0.38268343236509i, 0.707106781186548+0.707106781186547i, 0.38268343236509+0.923879532511287i, 0+1i, -0.38268343236509+0.923879532511287i, -0.707106781186547+0.707106781186548i, -0.923879532511287+0.38268343236509i, -1+0i, -0.923879532511287-0.38268343236509i, -0.707106781186548-0.707106781186547i, -0.38268343236509-0.923879532511287i, 0-1i, 0.38268343236509-0.923879532511287i, 0.707106781186547-0.707106781186548i, 0.923879532511287-0.38268343236509i, 1-0i));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage21() {
        assertEval("argv <- list(integer(0));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage22() {
        assertEval("argv <- list(structure(0:100, .Tsp = c(1, 101, 1), class = 'ts'));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage23() {
        assertEval("argv <- list(c(0.568, 1.432, -1.08, 1.08));is.language(argv[[1]]);");
    }

    @Test
    public void testislanguage24() {
        assertEval("argv <- list('«Latin-1 accented chars»: éè øØ å<Å æ<Æ');is.language(argv[[1]]);");
    }
}
