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
public class TestBuiltin_qr extends TestBase {

    @Test
    public void testqr1() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(list(qr = structure(c(-2.99896066496855+0i, 0.0864255261791181+0i, -0.13772470327145+0i, -0.49098351645158+0i, 0.249389337649224+0i, 1.17331892183982+0i, -1.54960102684918+0i, -0.573648421141553+0i, 0.376760682628698+0i, 0.412090084647403+0i, 0.603959683330493+0i, -0.0216928335770876+0i, -1.2469936242596+0i, 0.224366164923213+0i, 0.341798188737913+0i, 1.04294423444024+0i, 0.270564951504877+0i, -0.315381666175534+0i, 0.787566751532822+0i, 0.229154517629245+0i, -2.25109940279642+0i, 0.530750192641659+0i, -0.0977755443891602+0i, 0.6614171819615+0i, -0.0856949989622426+0i), .Dim = c(5L, 5L), .Dimnames = list(c('1', '2', '3', '4', '5'), c('c', 'a', 'd', 'b', 'e'))), rank = 5L, qraux = c(1.50410169966891+0i, 1.21888836143069+0i, 1.71355205288103+0i, 1.90021623833265+0i, 0+0i), pivot = c(3L, 1L, 4L, 2L, 5L)), .Names = c('qr', 'rank', 'qraux', 'pivot'), class = 'qr'), structure(1:5, .Dim = c(5L, 1L))); .Internal(qr_coef_cmplx(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testqr2() {
        assertEval(Ignored.Unknown,
                        "argv <- list(structure(list(qr = structure(c(-2.79657712283236, 0.0791500442336917, 0.123755637403102, 0.089607462331441, 0.175462651449591, 0.0695565565709435, 0.129006956605315, 0.206583197284758, 0.0227114114395308, 0.235970456809858, 0.0914077988155571, 0.226297607634113, 0.0934594628258066, 0.0899795540097744, 0.12841549388179, 0.240520185730483, 0.233009950431413, 0.105133974880502, 0.2095512974465, 0.258964862210899, -2.07025325833148, -1.85301582498188, 0.139094572499947, 0.342556683478902, -0.102024562608463, 0.360699451256097, 0.315324737973941, 0.0873752715112826, 0.2823485467872, -0.239863332146733, -0.00369181667619238, -0.172536775168022, 0.229736826805589, 0.0852501914884386, 0.230310089401495, -0.0314168397035678, 0.0849707357385819, 0.365804687920591, -0.0536336269418447, 0.0845797169641211, -2.03746531175251, -0.489461063366758, -1.40644653583967, -0.00873041883181913, 0.0708276075409328, 0.153420226417602, -0.370069917481653, -0.112816417432651, 0.240125650075004, 0.138426195987046, 0.128552669396225, 0.217325815608225, -0.0223361051263949, -0.0526633296159945, -0.296094517820351, -0.327346352864029, -0.249917267465335, -0.0672115093838751, 0.0654426021596298, -0.240131194574062, -2.06587739268838, -0.339470951293598, -0.49608488205654, 1.14277258876071, 0.201196269542128, 0.0348254315928563, 0.145314253550005, 0.131380830586619, -0.387015716398766, 0.283849139598354, -0.0827284627849877, 0.132994279479374, -0.113393410148955, 0.0518736136418599, -0.422882666833989, -0.141635274746576, -0.181291088091223, 0.196913259592121, -0.0460107390352923, 0.15597817986415), .Dim = c(20L, 4L)), rank = 4L, qraux = c(1.32642615746455, 1.10105053486773, 1.21513331337829, 1.21397558590595), pivot = c(4L, 1L, 3L, 2L)), .Names = c('qr', 'rank', 'qraux', 'pivot'), useLAPACK = TRUE, class = 'qr'), structure(c(0.434659484773874, 0.712514678714797, 0.399994368897751, 0.325352151878178, 0.757087148027495, 0.202692255144939, 0.711121222469956, 0.121691921027377, 0.245488513959572, 0.14330437942408, 0.239629415096715, 0.0589343772735447, 0.642288258532062, 0.876269212691113, 0.778914677444845, 0.79730882588774, 0.455274453619495, 0.410084082046524, 0.810870242770761, 0.604933290276676, 0.654723928077146, 0.353197271935642, 0.270260145887733, 0.99268406117335, 0.633493264438584, 0.213208135217428, 0.129372348077595, 0.478118034312502, 0.924074469832703, 0.59876096714288, 0.976170694921166, 0.731792511884123, 0.356726912083104, 0.431473690550774, 0.148211560677737, 0.0130775754805654, 0.715566066093743, 0.103184235747904, 0.446284348610789, 0.640101045137271, 1.00298403897323, 0.272296643047594, 0.67556063386146, 0.151371688628569, 0.340151631063782, 0.431371175684035, 0.0309030100004748, 0.457057784032077, 0.880189609760418, 0.426803491590545, 0.543544612638652, 0.655281779309735, 0.526419038954191, 0.231530745956115, 0.877417415869422, 0.686553374305367, 0.847202921006829, 0.115471200458705, 0.751486539305188, 0.432544381567277, 0.682788078673184, 0.601541217649356, 0.238868677755818, 0.258165926672518, 0.729309623362496, 0.452570831403136, 0.175126768415794, 0.746698269620538, 0.104987640399486, 0.864544949028641, 0.614644971676171, 0.557159538846463, 0.328777319053188, 0.453131445450708, 0.500440972624347, 0.180866361130029, 0.529630602803081, 0.0752757457084954, 0.277755932649598, 0.212699519237503, 0.0904899418726564, 0.0829104807786643, 0.140637623313814, 0.186663761837408, 0.0510252129565924, 0.195122500695288, 0.189470667047426, 0.14745507678017, 0.160610442608595, 0.0259712139610201, 0.0604781195987016, 0.0592939835228026, 0.157146221613511, 0.0842694476991892, 0.187063216743991, 0.126278517944738, 0.175293296081945, 0.202698964001611, 0.104955473728478, 0.1719400214497, 0.293730155099183, 0.19126010988839, 0.886450943304226, 0.503339485730976, 0.877057543024421, 0.189193622441962, 0.758103052387014, 0.724498892668635, 0.943724818294868, 0.547646587016061, 0.711743867723271, 0.388905099825934, 0.100873126182705, 0.927302088588476, 0.283232500310987, 0.59057315881364, 0.110360604943708, 0.840507032116875, 0.317963684443384, 0.782851336989552, 0.267508207354695, 0.218645284883678, 0.516796836396679, 0.268950592027977, 0.181168327340856, 0.518576137488708, 0.562782935798168, 0.129156854469329, 0.256367604015395, 0.717935275984928, 0.961409936426207, 0.100140846567228, 0.763222689507529, 0.947966354666278, 0.818634688388556, 0.308292330708355, 0.649579460499808, 0.953355451114476, 0.953732650028542, 0.339979203417897), .Dim = c(20L, 7L)), TRUE); .Internal(qr_qy_real(argv[[1]], argv[[2]], argv[[3]]))");
    }

    @Test
    public void testQr() {
        assertEval("{ qr(matrix(1:6,nrow=2), LAPACK=FALSE)$pivot }");
        assertEval("{ qr(matrix(1:6,nrow=2), LAPACK=FALSE)$rank }");
        assertEval("{ round( qr(matrix(1:6,nrow=2), LAPACK=FALSE)$qraux, digits=5 ) }");
        assertEval("{ round( qr(matrix(c(3,2,-3,-4),nrow=2), LAPACK=FALSE)$qr, digits=5 ) }");

        assertEval("{ m <- matrix(c(1,0,0,0,1,0,0,0,1),nrow=3) ; x <- qr(m, LAPACK=FALSE) ; qr.coef(x, 1:3) }");
        assertEval("{ x <- qr(cbind(1:3,2:4), LAPACK=FALSE) ; round( qr.coef(x, 1:3), digits=5 ) }");

        assertEval(Ignored.Unknown, "{ x <- qr(t(cbind(1:10,2:11)), LAPACK=TRUE) ; qr.coef(x, 1:2) }");
        assertEval("{ qr(10, LAPACK=TRUE) }");
        assertEval(Ignored.Unknown, "{ round( qr(matrix(1:6,nrow=2), LAPACK=TRUE)$qr, digits=5) }");

        // qr.coef
        assertEval(Ignored.Unknown, Output.ContainsError, "{ x <- qr(cbind(1:10,2:11), LAPACK=TRUE) ; qr.coef(x, 1:2) }");
        assertEval(Ignored.Unknown, " { x <- qr(cbind(1:10,2:11), LAPACK=TRUE) ; round( qr.coef(x, 1:10), digits=5 ) }");
        assertEval(Ignored.Unknown, "{ x <- qr(c(3,1,2), LAPACK=TRUE) ; round( qr.coef(x, c(1,3,2)), digits=5 ) }");
        // FIXME: GNU-R will print negative zero as zero
        assertEval("{ x <- qr(t(cbind(1:10,2:11)), LAPACK=FALSE) ; qr.coef(x, 1:2) }");
        assertEval(Ignored.Unknown, "{ x <- qr(c(3,1,2), LAPACK=FALSE) ; round( qr.coef(x, c(1,3,2)), digits=5 ) }");

        // qr.solve
        assertEval(Ignored.Unknown, "{ round( qr.solve(qr(c(1,3,4,2)), c(1,2,3,4)), digits=5 ) }");
        assertEval(Ignored.Unknown, "{ round( qr.solve(c(1,3,4,2), c(1,2,3,4)), digits=5) }");
    }
}
