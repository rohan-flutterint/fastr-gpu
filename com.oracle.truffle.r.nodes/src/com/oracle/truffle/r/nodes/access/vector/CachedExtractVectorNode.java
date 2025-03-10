/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.nodes.access.vector;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.utilities.BranchProfile;
import com.oracle.truffle.api.utilities.ConditionProfile;
import com.oracle.truffle.api.utilities.ValueProfile;
import com.oracle.truffle.r.nodes.access.vector.CachedExtractVectorNodeFactory.SetNamesNodeGen;
import com.oracle.truffle.r.nodes.access.vector.PositionsCheckNode.PositionProfile;
import com.oracle.truffle.r.nodes.profile.VectorLengthProfile;
import com.oracle.truffle.r.runtime.NullProfile;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.data.RAttributeProfiles;
import com.oracle.truffle.r.runtime.data.RAttributes;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RExpression;
import com.oracle.truffle.r.runtime.data.RFactor;
import com.oracle.truffle.r.runtime.data.RList;
import com.oracle.truffle.r.runtime.data.RLogical;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.RString;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.RTypedValue;
import com.oracle.truffle.r.runtime.data.RVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.model.RAbstractStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.env.REnvironment;
import com.oracle.truffle.r.runtime.nodes.RNode;

final class CachedExtractVectorNode extends CachedVectorNode {

    protected static final boolean DEFAULT_EXACT = true;
    protected static final boolean DEFAULT_DROP_DIMENSION = true;

    private final Class<? extends RTypedValue> targetClass;
    private final Class<? extends RTypedValue> exactClass;
    private final Class<? extends RTypedValue> dropDimensionsClass;
    private final boolean exact;
    private final boolean dropDimensions;

    private final VectorLengthProfile vectorLengthProfile = VectorLengthProfile.create();
    private final RAttributeProfiles vectorNamesProfile = RAttributeProfiles.create();

    @Child private WriteIndexedVectorNode writeVectorNode;
    @Child private PositionsCheckNode positionsCheckNode;
    @Child private SetNamesNode setNamesNode;
    @Children private final CachedExtractVectorNode[] extractNames;

    @Child private ExtractDimNamesNode extractDimNames;

    private final ConditionProfile resultHasDimensions = ConditionProfile.createBinaryProfile();

    /**
     * Profile if any metadata was applied at any point in time. This is useful extract primitive
     * values from the result in case no metadata was ever applied.
     */
    private final BranchProfile metadataApplied = BranchProfile.create();

    public CachedExtractVectorNode(ElementAccessMode mode, RTypedValue vector, Object[] positions, RTypedValue exact, RTypedValue dropDimensions, boolean recursive) {
        super(mode, vector, positions, recursive);
        this.targetClass = vector.getClass();
        this.exactClass = exact.getClass();
        this.dropDimensionsClass = dropDimensions.getClass();
        Object[] convertedPositions = filterPositions(positions);
        this.extractNames = new CachedExtractVectorNode[convertedPositions.length];
        this.exact = logicalAsBoolean(exact, DEFAULT_EXACT);
        this.dropDimensions = logicalAsBoolean(dropDimensions, DEFAULT_DROP_DIMENSION);
        this.positionsCheckNode = new PositionsCheckNode(mode, vectorType, convertedPositions, this.exact, false, recursive);
        if (vectorType != RType.Null && vectorType != RType.Environment) {
            this.writeVectorNode = WriteIndexedVectorNode.create(vectorType, convertedPositions.length, true, false, false);
        }
    }

    public boolean isSupported(Object target, Object[] positions, Object exactValue, Object dropDimensionsValue) {
        if (targetClass == target.getClass() && exactValue.getClass() == this.exactClass //
                        && logicalAsBoolean(dropDimensionsClass.cast(dropDimensionsValue), DEFAULT_DROP_DIMENSION) == this.dropDimensions //
                        && dropDimensionsValue.getClass() == this.dropDimensionsClass //
                        && logicalAsBoolean(exactClass.cast(exactValue), DEFAULT_EXACT) == this.exact) {
            return positionsCheckNode.isSupported(positions);
        }
        return false;
    }

    private final ConditionProfile extractedLengthGTZeroProfile = ConditionProfile.createBinaryProfile();
    private final ConditionProfile oneDimensionProfile = ConditionProfile.createBinaryProfile();

    public Object apply(Object originalVector, Object[] originalPositions, PositionProfile[] originalProfiles, Object originalExact, Object originalDropDimensions) {
        final Object[] positions = filterPositions(originalPositions);

        assert isSupported(originalVector, positions, originalExact, originalDropDimensions);

        final RTypedValue castVector = targetClass.cast(originalVector);
        final RAbstractContainer vector;
        switch (vectorType) {
            case Null:
                return RNull.instance;
            case Environment:
                /*
                 * TODO (chumer) the environment case cannot be applied to the default extract
                 * method as it does not implement RAbstractContainer. This should be harmonized
                 * later.
                 */
                return doEnvironment((REnvironment) castVector, positions);
            case Integer:
                if (castVector instanceof RFactor) {
                    vector = ((RFactor) castVector).getVector();
                    break;
                }
                vector = (RAbstractContainer) castVector;
                break;
            default:
                vector = (RAbstractContainer) castVector;
                break;
        }

        int vectorLength = vectorLengthProfile.profile(vector.getLength());

        int[] dimensions = getDimensions(vector);

        PositionProfile[] positionProfiles;
        if (originalProfiles == null) {
            positionProfiles = positionsCheckNode.executeCheck(vector, dimensions, vectorLength, positions);
        } else {
            positionProfiles = originalProfiles;
        }

        if (isMissingSingleDimension()) {
            // special case for x<-matrix(1:4, ncol=2); x[]
            return originalVector;
        }

        int extractedVectorLength = positionsCheckNode.getSelectedPositionsCount(positionProfiles);
        final RVector extractedVector;
        switch (vectorType) {
            case Language:
            case DataFrame:
            case Expression:
            case PairList:
                extractedVector = RType.List.create(extractedVectorLength, false);
                break;
            default:
                extractedVector = vectorType.create(extractedVectorLength, false);
                break;
        }

        if (mode.isSubset()) {
            if (extractedLengthGTZeroProfile.profile(extractedVectorLength > 0)) {
                writeVectorNode.enableValueNACheck(vector);
                writeVectorNode.apply(extractedVector, extractedVectorLength, positions, vector, vectorLength, dimensions);
                extractedVector.setComplete(writeVectorNode.neverSeenNAInValue());
                RNode.reportWork(this, extractedVectorLength);
            }
            if (oneDimensionProfile.profile(numberOfDimensions == 1)) {
                // names only need to be considered for single dimensional accesses
                RStringVector originalNames = vector.getNames(vectorNamesProfile);
                if (originalNames != null) {
                    metadataApplied.enter();
                    setNames(extractedVector, extractNames(originalNames, positions, positionProfiles, 0, originalExact, originalDropDimensions));
                }
            } else {
                assert numberOfDimensions > 1;
                applyDimensions(vector, extractedVector, extractedVectorLength, positionProfiles, positions);
            }

            switch (vectorType) {
                case Expression:
                    return new RExpression((RList) extractedVector);
                case Language:
                    return materializeLanguage(extractedVector);
                default:
                    return trySubsetPrimitive(extractedVector);
            }

        } else {
            writeVectorNode.apply(extractedVector, extractedVectorLength, positions, vector, vectorLength, dimensions);
            RNode.reportWork(this, 1);
            assert extractedVectorLength == 1;
            return extractedVector.getDataAtAsObject(0);
        }
    }

    private int[] getDimensions(final RAbstractContainer vector) {
        int[] dimensions;
        if (numberOfDimensions == 1) {
            dimensions = null;
        } else {
            dimensions = loadVectorDimensions(vector);
        }
        return dimensions;
    }

    private Object trySubsetPrimitive(RAbstractVector extractedVector) {
        if (!metadataApplied.isVisited() && positionsCheckNode.getCachedSelectedPositionsCount() == 1 && !isList()) {
            /*
             * If the selected count was always 1 and no metadata was ever set we can just extract
             * the primitive value from the vector. This branch has to fold to a constant because we
             * want to avoid the toggling of the return types depending on input values.
             */
            assert extractedVector.getNames(RAttributeProfiles.create()) == null;
            assert extractedVector.getDimensions() == null;
            assert extractedVector.getDimNames(null) == null;
            return extractedVector.getDataAtAsObject(0);
        }
        return extractedVector;
    }

    private Object doEnvironment(REnvironment env, Object[] positions) {
        if (mode.isSubset()) {
            errorBranch.enter();
            throw RError.error(this, RError.Message.OBJECT_NOT_SUBSETTABLE, RType.Environment.getName());
        }

        String positionString = tryCastSingleString(positionsCheckNode, positions);
        if (positionString != null) {
            Object obj = env.get(positionString);
            return obj == null ? RNull.instance : obj;
        }
        errorBranch.enter();
        throw RError.error(this, RError.Message.WRONG_ARGS_SUBSET_ENV);
    }

    private boolean isMissingSingleDimension() {
        return numberOfDimensions == 1 && positionsCheckNode.isMissing();
    }

    @TruffleBoundary
    private static Object materializeLanguage(RAbstractVector extractedVector) {
        return RContext.getRRuntimeASTAccess().fromList((RList) extractedVector);
    }

    private Object extract(int dimensionIndex, RAbstractStringVector vector, Object pos, PositionProfile profile) {
        if (extractDimNames == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            extractDimNames = new ExtractDimNamesNode(numberOfDimensions);
        }
        return extractDimNames.extract(dimensionIndex, vector, pos, profile);
    }

    private boolean isList() {
        return vectorType == RType.List;
    }

    private final NullProfile dimNamesNull = NullProfile.create();
    private final ValueProfile foundDimNamesProfile = ValueProfile.createClassProfile();

    @ExplodeLoop
    private void applyDimensions(RAbstractContainer originalTarget, RVector extractedTarget, int extractedTargetLength, PositionProfile[] positionProfile, Object[] positions) {
        // TODO speculate on the number of counted dimensions
        int dimCount = countDimensions(positionProfile);

        int[] newDimensions = new int[dimCount];
        RList originalDimNames = dimNamesNull.profile(originalTarget.getDimNames(null));
        Object[] newDimNames = null;
        if (originalDimNames != null) {
            newDimNames = new Object[dimCount];
        }

        int dimIndex = -1;
        for (int i = 0; i < numberOfDimensions; i++) {
            int selectedPositionsCount = positionProfile[i].selectedPositionsCount;
            if (selectedPositionsCount != 1 || !dropDimensions) {
                dimIndex++;
                newDimensions[dimIndex] = selectedPositionsCount;
                if (originalDimNames != null) {
                    Object dataAt = originalDimNames.getDataAt(i);
                    Object result;
                    if (dataAt == RNull.instance) {
                        result = RNull.instance;
                    } else {
                        result = extract(i, (RAbstractStringVector) dataAt, positions[i], positionProfile[i]);
                    }
                    newDimNames[dimIndex] = result;
                }
            }
        }

        if (resultHasDimensions.profile(dimCount > 1)) {
            metadataApplied.enter();
            extractedTarget.setDimensions(newDimensions);
            if (newDimNames != null) {
                extractedTarget.setDimNames(RDataFactory.createList(newDimNames));
            }
        } else if (originalDimNames != null && originalDimNames.getLength() > 0) {
            RAbstractStringVector foundNames = translateDimNamesToNames(positionProfile, originalDimNames, extractedTargetLength, positions);
            if (foundNames != null) {
                foundNames = foundDimNamesProfile.profile(foundNames);
                if (foundNames.getLength() > 0) {
                    metadataApplied.enter();
                    setNames(extractedTarget, foundNames);
                }
            }
        }
    }

    private final BranchProfile droppedDimensionProfile = BranchProfile.create();

    @ExplodeLoop
    private int countDimensions(PositionProfile[] boundsProfile) {
        if (dropDimensions) {
            int dimCount = numberOfDimensions;
            for (int i = 0; i < numberOfDimensions; i++) {
                int selectedPositionsCount = boundsProfile[i].selectedPositionsCount;
                if (selectedPositionsCount == 1) {
                    droppedDimensionProfile.enter();
                    dimCount--;
                }
            }
            return dimCount;
        } else {
            return numberOfDimensions;
        }
    }

    private final ConditionProfile newNamesProfile = ConditionProfile.createBinaryProfile();

    @ExplodeLoop
    private RAbstractStringVector translateDimNamesToNames(PositionProfile[] positionProfile, RList originalDimNames, int newVectorLength, Object[] positions) {
        RAbstractStringVector foundNames = null;
        for (int currentDimIndex = numberOfDimensions - 1; currentDimIndex >= 0; currentDimIndex--) {
            PositionProfile profile = positionProfile[currentDimIndex];
            if (profile.selectedPositionsCount != newVectorLength) {
                continue;
            }

            Object srcNames = originalDimNames.getDataAt(currentDimIndex);
            if (srcNames != RNull.instance) {
                Object position = positions[currentDimIndex];

                Object newNames = extractNames((RAbstractStringVector) srcNames, new Object[]{position}, new PositionProfile[]{profile}, currentDimIndex, RLogical.valueOf(true),
                                RLogical.valueOf(dropDimensions));
                if (newNames != RNull.instance) {
                    if (newNamesProfile.profile(newNames instanceof String)) {
                        newNames = RDataFactory.createStringVector((String) newNames);
                    }
                    RAbstractStringVector castFoundNames = (RAbstractStringVector) newNames;
                    if (castFoundNames.getLength() == newVectorLength) {
                        if (foundNames != null) {
                            /*
                             * the idea here is that you can get names from dimnames only if the
                             * name of of an item can be unambiguously identified (there can be only
                             * one matching name in all dimensions - if "name" has already been set,
                             * we might as well return null already)
                             */
                            foundNames = null;
                            break;
                        }
                        foundNames = (RAbstractStringVector) newNames;
                    }
                }

            }
        }
        return foundNames;
    }

    private Object extractNames(RAbstractStringVector originalNames, Object[] positions, PositionProfile[] profiles, int dimension, Object originalExact, Object originalDropDimensions) {
        if (extractNames[dimension] == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            extractNames[dimension] = insert(new CachedExtractVectorNode(mode, originalNames, positions, (RTypedValue) originalExact, (RTypedValue) originalDropDimensions, recursive));
        }

        assert extractNames[dimension].isSupported(originalNames, positions, originalExact, originalDropDimensions);
        Object newNames = extractNames[dimension].apply(originalNames, positions, profiles, originalExact, originalDropDimensions);
        return newNames;
    }

    private void setNames(RVector vector, Object newNames) {
        if (setNamesNode == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            setNamesNode = insert(SetNamesNodeGen.create());
        }
        setNamesNode.execute(vector, newNames);
    }

    protected abstract static class SetNamesNode extends Node {

        public abstract void execute(RVector container, Object newNames);

        @Specialization
        protected void setNames(RVector container, RAbstractStringVector newNames) {
            RStringVector newNames1 = newNames.materialize();
            assert newNames1.getLength() <= container.getLength();
            assert container.getInternalDimensions() == null;
            if (container.getAttributes() == null) {
                // usual case
                container.initAttributes(RAttributes.createInitialized(new String[]{RRuntime.NAMES_ATTR_KEY}, new Object[]{newNames1}));
                container.setInternalNames(newNames1);
            } else {
                // from an RLanguage extraction that set a name
                RAttributes attrs = container.getAttributes();
                RStringVector oldNames = (RStringVector) attrs.get(RRuntime.NAMES_ATTR_KEY);
                assert oldNames.getLength() == newNames.getLength();
                assert oldNames.toString().equals(newNames1.toString());
                // i.e. nothing actually needs to be done
            }
        }

        @Specialization
        protected void setNames(RVector container, String newNames) {
            // TODO: why materialize()?
            setNames(container, RString.valueOf(newNames).materialize());
        }

        @Specialization
        protected void setNames(RVector container, @SuppressWarnings("unused") RNull newNames) {
            assert container.getAttributes() == null;
        }
    }

    private static class ExtractDimNamesNode extends Node {

        @Children private final CachedExtractVectorNode[] extractNodes;

        public ExtractDimNamesNode(int dimensions) {
            this.extractNodes = new CachedExtractVectorNode[dimensions];
        }

        public Object extract(int dimensionIndex, RAbstractStringVector vector, Object position, PositionProfile profile) {
            Object[] positions = new Object[]{position};
            PositionProfile[] profiles = new PositionProfile[]{profile};
            if (extractNodes[dimensionIndex] == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                extractNodes[dimensionIndex] = insert(new CachedExtractVectorNode(ElementAccessMode.SUBSET, vector, positions, RLogical.TRUE, RLogical.TRUE, false));
            }
            CompilerAsserts.partialEvaluationConstant(dimensionIndex);
            assert extractNodes[dimensionIndex].isSupported(vector, positions, RLogical.TRUE, RLogical.TRUE);
            return extractNodes[dimensionIndex].apply(vector, positions, profiles, RLogical.TRUE, RLogical.TRUE);
        }
    }

}
