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

import uk.ac.ed.datastructures.common.PArray;
import uk.ac.ed.jpai.ArrayFunction;
import uk.ac.ed.jpai.Marawacc;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.r.library.gpu.cache.MarawaccPackage;
import com.oracle.truffle.r.library.gpu.cache.RGPUCache;
import com.oracle.truffle.r.library.gpu.cache.RMarawaccPromises;
import com.oracle.truffle.r.library.gpu.types.TypeInfo;
import com.oracle.truffle.r.library.gpu.types.TypeInfoList;
import com.oracle.truffle.r.library.gpu.utils.ASTxUtils;
import com.oracle.truffle.r.nodes.builtin.RExternalBuiltinNode;
import com.oracle.truffle.r.runtime.data.RArgsValuesAndNames;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;

public final class MarawaccReduceBuiltin extends RExternalBuiltinNode {

    private static <T, R> ArrayFunction<T, R> createMarawaccLambda(int nArgs, RootCallTarget callTarget, RFunction rFunction, String[] nameArgs, int neutral) {
        @SuppressWarnings("unchecked")
        ArrayFunction<T, R> function = (ArrayFunction<T, R>) Marawacc.reduce((x, y) -> {
            Object[] argsPackage = ASTxUtils.getArgsPackage(nArgs, rFunction, x, y, nameArgs);
            Object result = callTarget.call(argsPackage);
            return (Integer) result;
        }, neutral);
        return function;
    }

    private static <T, R> ArrayFunction<T, R> createMarawaccLambda(int nArgs, RootCallTarget callTarget, RFunction rFunction, String[] nameArgs, int neutral,
                    @SuppressWarnings("rawtypes") ArrayFunction arrayFunction) {
        @SuppressWarnings("unchecked")
        ArrayFunction<T, R> function = arrayFunction.reduce((x, y) -> {
            Object[] argsPackage = ASTxUtils.getArgsPackage(nArgs, rFunction, x, y, nameArgs);
            Object result = callTarget.call(argsPackage);
            return result;
        }, neutral);
        return function;
    }

    @SuppressWarnings("rawtypes")
    public static ArrayFunction composeReduceExpression(RAbstractVector input, RFunction rFunction, RootCallTarget callTarget, RAbstractVector[] additionalArgs, int neutral) {
        int nArgs = ASTxUtils.getNumberOfArguments(rFunction);
        String[] argsName = ASTxUtils.getArgumentsNames(rFunction);

        Object[] argsPackage = ASTxUtils.getArgsPackageForReduction(nArgs, neutral, rFunction, input, additionalArgs, argsName, 0);
        Object value = rFunction.getTarget().call(argsPackage);

        TypeInfoList inputTypeList = ASTxUtils.typeInference(input, additionalArgs);
        TypeInfo outputType = ASTxUtils.typeInference(value);

        ArrayFunction composeLambda = createMarawaccLambda(inputTypeList.size() + 1, callTarget, rFunction, argsName, neutral);
        PArray pArrayInput = ASTxUtils.marshall(input, additionalArgs, inputTypeList);

        // Create package and annotate in the promises
        MarawaccPackage marawaccPackage = new MarawaccPackage(composeLambda);
        marawaccPackage.setpArray(pArrayInput);
        marawaccPackage.setTypeInfo(outputType);
        marawaccPackage.setOutput(value);
        RMarawaccPromises.INSTANCE.addPromise(marawaccPackage);
        return composeLambda;
    }

    @SuppressWarnings("rawtypes")
    public static ArrayFunction composeReduceExpression(ArrayFunction marawaccFunction, RFunction rFunction, RootCallTarget callTarget, RAbstractVector[] additionalArgs, int neutral) {

        int nArgs = ASTxUtils.getNumberOfArguments(rFunction);
        String[] argsName = ASTxUtils.getArgumentsNames(rFunction);

        // Get the package from the Promises
        MarawaccPackage packageForArrayFunction = RMarawaccPromises.INSTANCE.getPackageForArrayFunction(marawaccFunction);
        Object output = packageForArrayFunction.getRVector();

        Object[] argsPackage = ASTxUtils.getArgsPackageForReduction(nArgs, neutral, rFunction, output, additionalArgs, argsName, 0);
        Object value = rFunction.getTarget().call(argsPackage);

        TypeInfo outputType = ASTxUtils.typeInference(value);

        ArrayFunction composeLambda = createMarawaccLambda(nArgs, callTarget, rFunction, argsName, neutral, marawaccFunction);

        // Create package and annotate in the promises
        MarawaccPackage marawaccPackage = new MarawaccPackage(composeLambda);
        marawaccPackage.setTypeInfo(outputType);
        marawaccPackage.setOutput(value);
        RMarawaccPromises.INSTANCE.addPromise(marawaccPackage);
        return composeLambda;
    }

    /**
     * Call method with variable number of arguments.
     *
     * Built-in from R:
     *
     * <code>
     * marawacc.reduce(x, function, ...)
     * </code>
     *
     * It invokes to the Marawacc-API for multiple-threads/GPU backend.
     *
     */
    @Override
    public Object call(RArgsValuesAndNames args) {

        // The first argument could be either an ArrayFunction
        // or input data (RAbstractVector)
        Object firstArgument = args.getArgument(0);
        RAbstractVector input = null;
        ArrayFunction<?, ?> marawaccFunction = null;
        if (firstArgument instanceof RAbstractVector) {
            // It is the initial operation
            input = (RAbstractVector) firstArgument;
        } else if (firstArgument instanceof ArrayFunction) {
            marawaccFunction = (ArrayFunction<?, ?>) firstArgument;
        }
        RFunction rFunction = (RFunction) args.getArgument(1);

        // Get the callTarget from the cache
        RootCallTarget callTarget = RGPUCache.INSTANCE.lookup(rFunction);
        int neutral = ((Double) args.getArgument(2)).intValue();

        // Prepare all inputs in an array of Objects
        RAbstractVector[] additionalInputs = null;
        if (args.getLength() > 3) {
            additionalInputs = new RAbstractVector[args.getLength() - 3];
            for (int i = 0; i < additionalInputs.length; i++) {
                additionalInputs[i] = (RAbstractVector) args.getArgument(i + 3);
            }
        }

        if (input != null) {
            return composeReduceExpression(input, rFunction, callTarget, additionalInputs, neutral);
        } else if (marawaccFunction != null) {
            return composeReduceExpression(marawaccFunction, rFunction, callTarget, additionalInputs, neutral);
        } else {
            return null;
        }
    }
}
