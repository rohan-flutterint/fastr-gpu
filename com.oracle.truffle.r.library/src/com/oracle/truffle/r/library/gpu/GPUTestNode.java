/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.library.gpu;

import java.util.ArrayList;

import uk.ac.ed.datastructures.common.AcceleratorPArray;
import uk.ac.ed.datastructures.common.PArray;
import uk.ac.ed.datastructures.tuples.Tuple2;
import uk.ac.ed.jpai.graal.GraalGPUCompilationUnit;
import uk.ac.ed.jpai.graal.GraalGPUCompiler;
import uk.ac.ed.jpai.graal.GraalGPUExecutor;
import uk.ac.ed.marawacc.compilation.MarawaccGraalIR;

import com.oracle.graal.nodes.StructuredGraph;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.r.library.gpu.cache.RGPUCache;
import com.oracle.truffle.r.library.gpu.exceptions.MarawaccTypeException;
import com.oracle.truffle.r.library.gpu.types.TypeInfo;
import com.oracle.truffle.r.library.gpu.utils.ASTxUtils;
import com.oracle.truffle.r.nodes.builtin.RExternalBuiltinNode;
import com.oracle.truffle.r.runtime.data.RArgsValuesAndNames;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;

public final class GPUTestNode extends RExternalBuiltinNode {

    private static ArrayList<Object> runJavaSequential(RAbstractVector input, RootCallTarget callTarget, RFunction function, int nArgs, RAbstractVector[] additionalArgs, String[] argsName,
                    Object firstValue) {
        ArrayList<Object> output = new ArrayList<>(input.getLength());
        output.add(firstValue);

        // Create a new root node
        // RootNode rootNode = function.getRootNode();
        // RootCallTarget newCallTarget = Truffle.getRuntime().createCallTarget(rootNode);

        System.out.println("[CLASS OF CALLTARGET]: " + callTarget.getClass());
        callTarget.generateIDForGPU();

        Runnable r = () -> {
            System.out.println("Inspect GPU Compilation thread");
            while (MarawaccGraalIR.INSTANCE.getCompiledGraph(callTarget.getIDForGPU()) == null) {
                // wait
            }
            StructuredGraph graphToCompile = MarawaccGraalIR.INSTANCE.getCompiledGraph(callTarget.getIDForGPU());
            MarawaccGraalIR.INSTANCE.clean();

// System.out.println("COMPILE TO GPU: " + graphToCompile.toString());
//
// // Compilation
// GraalGPUCompilationUnit compileGraphToGPU = GraalGPUCompiler.compileGraphToGPU(input,
// graphToCompile);
//
// // Execution
// AcceleratorPArray<Tuple2<Double, Double>> copyToDevice = GraalGPUExecutor.copyToDevice(input,
// compileGraphToGPU.getInputType());
// AcceleratorPArray<Double> executeOnTheDevice = GraalGPUExecutor.<Tuple2<Double, Double>, Double>
// executeOnTheDevice(graphToCompile, copyToDevice, compileGraphToGPU.getOuputType());
// PArray<Double> result = GraalGPUExecutor.copyToHost(executeOnTheDevice,
// compileGraphToGPU.getOuputType());

        };

        Thread t = new Thread(r);
        t.start();

        for (int i = 1; i < input.getLength(); i++) {
            Object[] argsPackage = ASTxUtils.getArgsPackage(nArgs, function, input, additionalArgs, argsName, i);
            // Object val = newCallTarget.call(argsPackage);
            Object val = callTarget.call(argsPackage);
            output.add(val);
        }
        return output;
    }

    private static RAbstractVector computeMap(RAbstractVector input, RFunction function, RootCallTarget target, RAbstractVector[] additionalArgs) {

        int nArgs = ASTxUtils.getNumberOfArguments(function);
        String[] argsName = ASTxUtils.getArgumentsNames(function);
        Object[] argsPackage = ASTxUtils.getArgsPackage(nArgs, function, input, additionalArgs, argsName, 0);
        Object value = function.getTarget().call(argsPackage);
        TypeInfo outputType = null;
        try {
            outputType = ASTxUtils.typeInference(value);
        } catch (MarawaccTypeException e) {
            // DEPTIOMIZE
            e.printStackTrace();
        }

        ArrayList<Object> result = runJavaSequential(input, target, function, nArgs, additionalArgs, argsName, value);
        return ASTxUtils.unMarshallResultFromList(outputType, result);
    }

    @Override
    public Object call(RArgsValuesAndNames args) {
        RAbstractVector input = (RAbstractVector) args.getArgument(0);
        RFunction function = (RFunction) args.getArgument(1);

        // Get the callTarget from the cache
        RootCallTarget target = RGPUCache.INSTANCE.lookup(function);

        // Prepare all inputs in an array of Objects
        RAbstractVector[] additionalInputs = null;
        if (args.getLength() > 3) {
            additionalInputs = new RAbstractVector[args.getLength() - 3];
            for (int i = 0; i < additionalInputs.length; i++) {
                additionalInputs[i] = (RAbstractVector) args.getArgument(i + 3);
            }
        }
        return computeMap(input, function, target, additionalInputs);
    }
}
