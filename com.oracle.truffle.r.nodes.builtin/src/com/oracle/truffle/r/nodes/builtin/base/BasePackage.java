/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.nodes.builtin.base;

import com.oracle.truffle.r.nodes.binary.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.nodes.unary.*;
import com.oracle.truffle.r.runtime.ops.*;

public class BasePackage extends RBuiltinPackage {

    public BasePackage() {
        super("base");

        /*
         * Primitive operations (these are really builtins, but not currently defined that way, so
         * we fake it). N.B. UnaryNotNode is annotated, but not loaded automatically because it is
         * not in the {nodes.builtin.base} package, (along with all the other nodes). A corollary of
         * this is that all the node classes referenced here must be annotated with
         * 
         * @GenerateNodeFactory.
         * 
         * Arguably this is structurally incorrect and they should all be moved into
         * {nodes.builtin.base}
         */
        add(UnaryNotNode.class, UnaryNotNodeFactory::create);

        add(BinaryArithmetic.AddBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.ADD, UnaryArithmetic.PLUS, arguments, builtin, signature));
        add(BinaryArithmetic.SubtractBuiltin.class,
                        (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.SUBTRACT, UnaryArithmetic.NEGATE, arguments, builtin, signature));
        add(BinaryArithmetic.DivBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.DIV, null, arguments, builtin, signature));
        add(BinaryArithmetic.IntegerDivBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.INTEGER_DIV, null, arguments, builtin, signature));
        add(BinaryArithmetic.ModBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.MOD, null, arguments, builtin, signature));
        add(BinaryArithmetic.MultiplyBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.MULTIPLY, null, arguments, builtin, signature));
        add(BinaryArithmetic.PowBuiltin.class, (arguments, builtin, signature) -> BinaryArithmeticNodeFactory.create(BinaryArithmetic.POW, null, arguments, builtin, signature));

        add(BinaryCompare.EqualBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.EQUAL, arguments, builtin, signature));
        add(BinaryCompare.NotEqualBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.NOT_EQUAL, arguments, builtin, signature));
        add(BinaryCompare.GreaterEqualBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.GREATER_EQUAL, arguments, builtin, signature));
        add(BinaryCompare.GreaterBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.GREATER_THAN, arguments, builtin, signature));
        add(BinaryCompare.LessBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.LESS_THAN, arguments, builtin, signature));
        add(BinaryCompare.LessEqualBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryCompare.LESS_EQUAL, arguments, builtin, signature));

        add(BinaryLogic.AndBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryLogic.AND, arguments, builtin, signature));
        add(BinaryLogic.OrBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNodeFactory.create(BinaryLogic.OR, arguments, builtin, signature));

        add(BinaryLogic.NonVectorAndBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNonVectorizedNodeFactory.create(BinaryLogic.NON_VECTOR_AND, arguments, builtin, signature));
        add(BinaryLogic.NonVectorOrBuiltin.class, (arguments, builtin, signature) -> BinaryBooleanNonVectorizedNodeFactory.create(BinaryLogic.NON_VECTOR_OR, arguments, builtin, signature));

        // Now load the rest of the builtins in "base"
        add(APerm.class, APermFactory::create);
        add(Abs.class, AbsFactory::create);
        add(All.class, AllFactory::create);
        add(Any.class, AnyFactory::create);
        add(AnyNA.class, AnyNAFactory::create);
        add(Args.class, ArgsFactory::create);
        add(Array.class, ArrayFactory::create);
        add(AsCall.class, AsCallFactory::create);
        add(AsCharacter.class, AsCharacterFactory::create);
        add(AsComplex.class, AsComplexFactory::create);
        add(AsDouble.class, AsDoubleFactory::create);
        add(AsFunction.class, AsFunctionFactory::create);
        add(AsInteger.class, AsIntegerFactory::create);
        add(AsLogical.class, AsLogicalFactory::create);
        add(AsRaw.class, AsRawFactory::create);
        add(AsVector.class, AsVectorFactory::create);
        add(Assign.class, AssignFactory::create);
        add(AttachFunctions.Attach.class, AttachFunctionsFactory.AttachFactory::create);
        add(AttachFunctions.Detach.class, AttachFunctionsFactory.DetachFactory::create);
        add(Attr.class, AttrFactory::create);
        add(Attributes.class, AttributesFactory::create);
        add(BaseGammaFunctions.DiGamma.class, BaseGammaFunctionsFactory.DiGammaFactory::create);
        add(BaseGammaFunctions.Gamma.class, BaseGammaFunctionsFactory.GammaFactory::create);
        add(BaseGammaFunctions.Lgamma.class, BaseGammaFunctionsFactory.LgammaFactory::create);
        add(BaseGammaFunctions.TriGamma.class, BaseGammaFunctionsFactory.TriGammaFactory::create);
        add(Bind.CbindInternal.class, BindFactory.CbindInternalFactory::create);
        add(Bind.RbindInternal.class, BindFactory.RbindInternalFactory::create);
        add(BitwiseFunctions.BitwiseAnd.class, BitwiseFunctionsFactory.BitwiseAndFactory::create);
        add(BitwiseFunctions.BitwiseNot.class, BitwiseFunctionsFactory.BitwiseNotFactory::create);
        add(BitwiseFunctions.BitwiseOr.class, BitwiseFunctionsFactory.BitwiseOrFactory::create);
        add(BitwiseFunctions.BitwiseShiftL.class, BitwiseFunctionsFactory.BitwiseShiftLFactory::create);
        add(BitwiseFunctions.BitwiseShiftR.class, BitwiseFunctionsFactory.BitwiseShiftRFactory::create);
        add(BitwiseFunctions.BitwiseXor.class, BitwiseFunctionsFactory.BitwiseXorFactory::create);
        add(Body.class, BodyFactory::create);
        add(BrowserFunctions.BrowserCondition.class, BrowserFunctionsFactory.BrowserConditionFactory::create);
        add(BrowserFunctions.BrowserNode.class, BrowserFunctionsFactory.BrowserNodeFactory::create);
        add(BrowserFunctions.BrowserSetDebug.class, BrowserFunctionsFactory.BrowserSetDebugFactory::create);
        add(BrowserFunctions.BrowserText.class, BrowserFunctionsFactory.BrowserTextFactory::create);
        add(Call.class, CallFactory::create);
        add(CapabilitiesFunctions.Capabilities.class, CapabilitiesFunctionsFactory.CapabilitiesFactory::create);
        add(Cat.class, CatFactory::create);
        add(Ceiling.class, Ceiling::new);
        add(CharMatch.class, CharMatchFactory::create);
        add(Col.class, ColFactory::create);
        add(ColMeans.class, ColMeansFactory::create);
        add(ColSums.class, ColSumsFactory::create);
        add(Combine.class, CombineFactory::create);
        add(CommandArgs.class, CommandArgsFactory::create);
        add(Complex.class, ComplexFactory::create);
        add(ConditionFunctions.AddCondHands.class, ConditionFunctionsFactory.AddCondHandsFactory::create);
        add(ConditionFunctions.AddRestart.class, ConditionFunctionsFactory.AddRestartFactory::create);
        add(ConditionFunctions.DfltStop.class, ConditionFunctionsFactory.DfltStopFactory::create);
        add(ConditionFunctions.DfltWarn.class, ConditionFunctionsFactory.DfltWarnFactory::create);
        add(ConditionFunctions.GetRestart.class, ConditionFunctionsFactory.GetRestartFactory::create);
        add(ConditionFunctions.Geterrmessage.class, ConditionFunctionsFactory.GeterrmessageFactory::create);
        add(ConditionFunctions.InvokeRestart.class, ConditionFunctionsFactory.InvokeRestartFactory::create);
        add(ConditionFunctions.ResetCondHands.class, ConditionFunctionsFactory.ResetCondHandsFactory::create);
        add(ConditionFunctions.Seterrmessage.class, ConditionFunctionsFactory.SeterrmessageFactory::create);
        add(ConditionFunctions.SignalCondition.class, ConditionFunctionsFactory.SignalConditionFactory::create);
        add(ConnectionFunctions.Close.class, ConnectionFunctionsFactory.CloseFactory::create);
        add(ConnectionFunctions.File.class, ConnectionFunctionsFactory.FileFactory::create);
        add(ConnectionFunctions.Flush.class, ConnectionFunctionsFactory.FlushFactory::create);
        add(ConnectionFunctions.GZFile.class, ConnectionFunctionsFactory.GZFileFactory::create);
        add(ConnectionFunctions.GetAllConnections.class, ConnectionFunctionsFactory.GetAllConnectionsFactory::create);
        add(ConnectionFunctions.GetConnection.class, ConnectionFunctionsFactory.GetConnectionFactory::create);
        add(ConnectionFunctions.IsOpen.class, ConnectionFunctionsFactory.IsOpenFactory::create);
        add(ConnectionFunctions.Open.class, ConnectionFunctionsFactory.OpenFactory::create);
        add(ConnectionFunctions.PushBack.class, ConnectionFunctionsFactory.PushBackFactory::create);
        add(ConnectionFunctions.PushBackClear.class, ConnectionFunctionsFactory.PushBackClearFactory::create);
        add(ConnectionFunctions.PushBackLength.class, ConnectionFunctionsFactory.PushBackLengthFactory::create);
        add(ConnectionFunctions.ReadBin.class, ConnectionFunctionsFactory.ReadBinFactory::create);
        add(ConnectionFunctions.ReadChar.class, ConnectionFunctionsFactory.ReadCharFactory::create);
        add(ConnectionFunctions.ReadLines.class, ConnectionFunctionsFactory.ReadLinesFactory::create);
        add(ConnectionFunctions.SocketConnection.class, ConnectionFunctionsFactory.SocketConnectionFactory::create);
        add(ConnectionFunctions.Stderr.class, ConnectionFunctionsFactory.StderrFactory::create);
        add(ConnectionFunctions.Stdin.class, ConnectionFunctionsFactory.StdinFactory::create);
        add(ConnectionFunctions.Stdout.class, ConnectionFunctionsFactory.StdoutFactory::create);
        add(ConnectionFunctions.Summary.class, ConnectionFunctionsFactory.SummaryFactory::create);
        add(ConnectionFunctions.TextConnection.class, ConnectionFunctionsFactory.TextConnectionFactory::create);
        add(ConnectionFunctions.TextConnectionValue.class, ConnectionFunctionsFactory.TextConnectionValueFactory::create);
        add(ConnectionFunctions.URLConnection.class, ConnectionFunctionsFactory.URLConnectionFactory::create);
        add(ConnectionFunctions.WriteBin.class, ConnectionFunctionsFactory.WriteBinFactory::create);
        add(ConnectionFunctions.WriteChar.class, ConnectionFunctionsFactory.WriteCharFactory::create);
        add(ConnectionFunctions.WriteLines.class, ConnectionFunctionsFactory.WriteLinesFactory::create);
        add(Contributors.class, ContributorsFactory::create);
        add(CopyDFAttr.class, CopyDFAttrFactory::create);
        add(Crossprod.class, CrossprodFactory::create);
        add(CumMax.class, CumMaxFactory::create);
        add(CumMin.class, CumMinFactory::create);
        add(CumProd.class, CumProdFactory::create);
        add(CumSum.class, CumSumFactory::create);
        add(Date.class, DateFactory::create);
        add(DatePOSIXFunctions.Date2POSIXlt.class, DatePOSIXFunctionsFactory.Date2POSIXltFactory::create);
        add(DebugFunctions.Debug.class, DebugFunctionsFactory.DebugFactory::create);
        add(DebugFunctions.DebugOnce.class, DebugFunctionsFactory.DebugOnceFactory::create);
        add(DebugFunctions.IsDebugged.class, DebugFunctionsFactory.IsDebuggedFactory::create);
        add(DebugFunctions.UnDebug.class, DebugFunctionsFactory.UnDebugFactory::create);
        add(DelayedAssign.class, DelayedAssignFactory::create);
        add(Deparse.class, DeparseFactory::create);
        add(Diag.class, DiagFactory::create);
        add(Dim.class, DimFactory::create);
        add(DimNames.class, DimNamesFactory::create);
        add(DoCall.class, DoCallFactory::create);
        add(Drop.class, DropFactory::create);
        add(DuplicatedFunctions.AnyDuplicated.class, DuplicatedFunctionsFactory.AnyDuplicatedFactory::create);
        add(DuplicatedFunctions.Duplicated.class, DuplicatedFunctionsFactory.DuplicatedFactory::create);
        add(DynLoadFunctions.DynLoad.class, DynLoadFunctionsFactory.DynLoadFactory::create);
        add(DynLoadFunctions.DynUnload.class, DynLoadFunctionsFactory.DynUnloadFactory::create);
        add(DynLoadFunctions.GetLoadedDLLs.class, DynLoadFunctionsFactory.GetLoadedDLLsFactory::create);
        add(DynLoadFunctions.GetSymbolInfo.class, DynLoadFunctionsFactory.GetSymbolInfoFactory::create);
        add(DynLoadFunctions.IsLoaded.class, DynLoadFunctionsFactory.IsLoadedFactory::create);
        add(EncodeString.class, EncodeStringFactory::create);
        add(EncodingFunctions.Encoding.class, EncodingFunctionsFactory.EncodingFactory::create);
        add(EncodingFunctions.SetEncoding.class, EncodingFunctionsFactory.SetEncodingFactory::create);
        add(EnvFunctions.AsEnvironment.class, EnvFunctionsFactory.AsEnvironmentFactory::create);
        add(EnvFunctions.BaseEnv.class, EnvFunctionsFactory.BaseEnvFactory::create);
        add(EnvFunctions.BindingIsActive.class, EnvFunctionsFactory.BindingIsActiveFactory::create);
        add(EnvFunctions.BindingIsLocked.class, EnvFunctionsFactory.BindingIsLockedFactory::create);
        add(EnvFunctions.EmptyEnv.class, EnvFunctionsFactory.EmptyEnvFactory::create);
        add(EnvFunctions.EnvToList.class, EnvFunctionsFactory.EnvToListFactory::create);
        add(EnvFunctions.Environment.class, EnvFunctionsFactory.EnvironmentFactory::create);
        add(EnvFunctions.EnvironmentIsLocked.class, EnvFunctionsFactory.EnvironmentIsLockedFactory::create);
        add(EnvFunctions.EnvironmentName.class, EnvFunctionsFactory.EnvironmentNameFactory::create);
        add(EnvFunctions.GlobalEnv.class, EnvFunctionsFactory.GlobalEnvFactory::create);
        add(EnvFunctions.IsEnvironment.class, EnvFunctionsFactory.IsEnvironmentFactory::create);
        add(EnvFunctions.LockBinding.class, EnvFunctionsFactory.LockBindingFactory::create);
        add(EnvFunctions.LockEnvironment.class, EnvFunctionsFactory.LockEnvironmentFactory::create);
        add(EnvFunctions.MakeActiveBinding.class, EnvFunctionsFactory.MakeActiveBindingFactory::create);
        add(EnvFunctions.NewEnv.class, EnvFunctionsFactory.NewEnvFactory::create);
        add(EnvFunctions.ParentEnv.class, EnvFunctionsFactory.ParentEnvFactory::create);
        add(EnvFunctions.Search.class, EnvFunctionsFactory.SearchFactory::create);
        add(EnvFunctions.SetParentEnv.class, EnvFunctionsFactory.SetParentEnvFactory::create);
        add(EnvFunctions.UnlockBinding.class, EnvFunctionsFactory.UnlockBindingFactory::create);
        add(EvalFunctions.Eval.class, EvalFunctionsFactory.EvalFactory::create);
        add(EvalFunctions.WithVisible.class, EvalFunctionsFactory.WithVisibleFactory::create);
        add(Exists.class, ExistsFactory::create);
        add(Expression.class, ExpressionFactory::create);
        add(FastR.class, FastRFactory::create);
        add(FileFunctions.BaseName.class, FileFunctionsFactory.BaseNameFactory::create);
        add(FileFunctions.DirCreate.class, FileFunctionsFactory.DirCreateFactory::create);
        add(FileFunctions.DirName.class, FileFunctionsFactory.DirNameFactory::create);
        add(FileFunctions.FileAccess.class, FileFunctionsFactory.FileAccessFactory::create);
        add(FileFunctions.FileAppend.class, FileFunctionsFactory.FileAppendFactory::create);
        add(FileFunctions.FileCopy.class, FileFunctionsFactory.FileCopyFactory::create);
        add(FileFunctions.FileCreate.class, FileFunctionsFactory.FileCreateFactory::create);
        add(FileFunctions.FileExists.class, FileFunctionsFactory.FileExistsFactory::create);
        add(FileFunctions.FileInfo.class, FileFunctionsFactory.FileInfoFactory::create);
        add(FileFunctions.FileLink.class, FileFunctionsFactory.FileLinkFactory::create);
        add(FileFunctions.FilePath.class, FileFunctionsFactory.FilePathFactory::create);
        add(FileFunctions.FileRemove.class, FileFunctionsFactory.FileRemoveFactory::create);
        add(FileFunctions.FileRename.class, FileFunctionsFactory.FileRenameFactory::create);
        add(FileFunctions.FileSymLink.class, FileFunctionsFactory.FileSymLinkFactory::create);
        add(FileFunctions.ListFiles.class, FileFunctionsFactory.ListFilesFactory::create);
        add(FileFunctions.Unlink.class, FileFunctionsFactory.UnlinkFactory::create);
        add(Floor.class, Floor::new);
        add(ForeignFunctions.C.class, ForeignFunctionsFactory.CFactory::create);
        add(ForeignFunctions.DotCall.class, ForeignFunctionsFactory.DotCallFactory::create);
        add(ForeignFunctions.DotExternal.class, ForeignFunctionsFactory.DotExternalFactory::create);
        add(ForeignFunctions.DotExternal2.class, ForeignFunctionsFactory.DotExternal2Factory::create);
        add(ForeignFunctions.DotExternalGraphics.class, ForeignFunctionsFactory.DotExternalGraphicsFactory::create);
        add(ForeignFunctions.Fortran.class, ForeignFunctionsFactory.FortranFactory::create);
        add(Formals.class, FormalsFactory::create);
        add(Format.class, FormatFactory::create);
        add(FrameFunctions.MatchCall.class, FrameFunctionsFactory.MatchCallFactory::create);
        add(FrameFunctions.ParentFrame.class, FrameFunctionsFactory.ParentFrameFactory::create);
        add(FrameFunctions.SysCall.class, FrameFunctionsFactory.SysCallFactory::create);
        add(FrameFunctions.SysFrame.class, FrameFunctionsFactory.SysFrameFactory::create);
        add(FrameFunctions.SysFrames.class, FrameFunctionsFactory.SysFramesFactory::create);
        add(FrameFunctions.SysFunction.class, FrameFunctionsFactory.SysFunctionFactory::create);
        add(FrameFunctions.SysNFrame.class, FrameFunctionsFactory.SysNFrameFactory::create);
        add(FrameFunctions.SysParent.class, FrameFunctionsFactory.SysParentFactory::create);
        add(FrameFunctions.SysParents.class, FrameFunctionsFactory.SysParentsFactory::create);
        add(Gc.class, GcFactory::create);
        add(GetClass.class, GetClassFactory::create);
        add(GetFunctions.Get.class, GetFunctionsFactory.GetFactory::create);
        add(GetFunctions.MGet.class, GetFunctionsFactory.MGetFactory::create);
        add(GetOldClass.class, GetOldClassFactory::create);
        add(GetText.class, GetTextFactory::create);
        add(Getwd.class, GetwdFactory::create);
        add(GrepFunctions.AGrep.class, GrepFunctionsFactory.AGrepFactory::create);
        add(GrepFunctions.AGrepL.class, GrepFunctionsFactory.AGrepLFactory::create);
        add(GrepFunctions.GSub.class, GrepFunctionsFactory.GSubFactory::create);
        add(GrepFunctions.Gregexpr.class, GrepFunctionsFactory.GregexprFactory::create);
        add(GrepFunctions.Grep.class, GrepFunctionsFactory.GrepFactory::create);
        add(GrepFunctions.GrepL.class, GrepFunctionsFactory.GrepLFactory::create);
        add(GrepFunctions.Regexp.class, GrepFunctionsFactory.RegexpFactory::create);
        add(GrepFunctions.Strsplit.class, GrepFunctionsFactory.StrsplitFactory::create);
        add(GrepFunctions.Sub.class, GrepFunctionsFactory.SubFactory::create);
        add(HiddenInternalFunctions.GetRegisteredRoutines.class, HiddenInternalFunctionsFactory.GetRegisteredRoutinesFactory::create);
        add(HiddenInternalFunctions.ImportIntoEnv.class, HiddenInternalFunctionsFactory.ImportIntoEnvFactory::create);
        add(HiddenInternalFunctions.LazyLoadDBFetch.class, HiddenInternalFunctionsFactory.LazyLoadDBFetchFactory::create);
        add(HiddenInternalFunctions.MakeLazy.class, HiddenInternalFunctionsFactory.MakeLazyFactory::create);
        add(IConv.class, IConvFactory::create);
        add(Identical.class, IdenticalFactory::create);
        add(Im.class, Im::new);
        add(InfixEmulationFunctions.AccessArraySubscriptBuiltin.class, InfixEmulationFunctionsFactory.AccessArraySubscriptBuiltinFactory::create);
        add(InfixEmulationFunctions.AccessArraySubscriptDefaultBuiltin.class, InfixEmulationFunctionsFactory.AccessArraySubscriptDefaultBuiltinFactory::create);
        add(InfixEmulationFunctions.AccessArraySubsetBuiltin.class, InfixEmulationFunctionsFactory.AccessArraySubsetBuiltinFactory::create);
        add(InfixEmulationFunctions.AccessArraySubsetDefaultBuiltin.class, InfixEmulationFunctionsFactory.AccessArraySubsetDefaultBuiltinFactory::create);
        add(InfixEmulationFunctions.AccessFieldBuiltin.class, InfixEmulationFunctionsFactory.AccessFieldBuiltinFactory::create);
        add(InfixEmulationFunctions.AssignBuiltin.class, InfixEmulationFunctionsFactory.AssignBuiltinFactory::create);
        add(InfixEmulationFunctions.AssignOuterBuiltin.class, InfixEmulationFunctionsFactory.AssignOuterBuiltinFactory::create);
        add(InfixEmulationFunctions.BraceBuiltin.class, InfixEmulationFunctionsFactory.BraceBuiltinFactory::create);
        add(InfixEmulationFunctions.BreakBuiltin.class, InfixEmulationFunctionsFactory.BreakBuiltinFactory::create);
        add(InfixEmulationFunctions.ColonBuiltin.class, InfixEmulationFunctionsFactory.ColonBuiltinFactory::create);
        add(InfixEmulationFunctions.ForBuiltin.class, InfixEmulationFunctionsFactory.ForBuiltinFactory::create);
        add(InfixEmulationFunctions.FunctionBuiltin.class, InfixEmulationFunctionsFactory.FunctionBuiltinFactory::create);
        add(InfixEmulationFunctions.IfBuiltin.class, InfixEmulationFunctionsFactory.IfBuiltinFactory::create);
        add(InfixEmulationFunctions.NextBuiltin.class, InfixEmulationFunctionsFactory.NextBuiltinFactory::create);
        add(InfixEmulationFunctions.ParenBuiltin.class, InfixEmulationFunctionsFactory.ParenBuiltinFactory::create);
        add(InfixEmulationFunctions.RepeatBuiltin.class, InfixEmulationFunctionsFactory.RepeatBuiltinFactory::create);
        add(InfixEmulationFunctions.UpdateArrayNodeSubscriptBuiltin.class, InfixEmulationFunctionsFactory.UpdateArrayNodeSubscriptBuiltinFactory::create);
        add(InfixEmulationFunctions.UpdateArraySubsetBuiltin.class, InfixEmulationFunctionsFactory.UpdateArraySubsetBuiltinFactory::create);
        add(InfixEmulationFunctions.UpdateFieldBuiltin.class, InfixEmulationFunctionsFactory.UpdateFieldBuiltinFactory::create);
        add(InfixEmulationFunctions.WhileBuiltin.class, InfixEmulationFunctionsFactory.WhileBuiltinFactory::create);
        add(Inherits.class, InheritsFactory::create);
        add(Interactive.class, InteractiveFactory::create);
        add(Internal.class, InternalFactory::create);
        add(Invisible.class, InvisibleFactory::create);
        add(IsFiniteFunctions.IsFinite.class, IsFiniteFunctionsFactory.IsFiniteFactory::create);
        add(IsFiniteFunctions.IsInfinite.class, IsFiniteFunctionsFactory.IsInfiniteFactory::create);
        add(IsFiniteFunctions.IsNan.class, IsFiniteFunctionsFactory.IsNanFactory::create);
        add(IsMethodsDispatchOn.class, IsMethodsDispatchOnFactory::create);
        add(IsNA.class, IsNAFactory::create);
        add(IsS4.class, IsS4Factory::create);
        add(IsTypeFunctions.IsArray.class, IsTypeFunctionsFactory.IsArrayFactory::create);
        add(IsTypeFunctions.IsAtomic.class, IsTypeFunctionsFactory.IsAtomicFactory::create);
        add(IsTypeFunctions.IsCall.class, IsTypeFunctionsFactory.IsCallFactory::create);
        add(IsTypeFunctions.IsCharacter.class, IsTypeFunctionsFactory.IsCharacterFactory::create);
        add(IsTypeFunctions.IsComplex.class, IsTypeFunctionsFactory.IsComplexFactory::create);
        add(IsTypeFunctions.IsDouble.class, IsTypeFunctionsFactory.IsDoubleFactory::create);
        add(IsTypeFunctions.IsExpression.class, IsTypeFunctionsFactory.IsExpressionFactory::create);
        add(IsTypeFunctions.IsFunction.class, IsTypeFunctionsFactory.IsFunctionFactory::create);
        add(IsTypeFunctions.IsInteger.class, IsTypeFunctionsFactory.IsIntegerFactory::create);
        add(IsTypeFunctions.IsLanguage.class, IsTypeFunctionsFactory.IsLanguageFactory::create);
        add(IsTypeFunctions.IsList.class, IsTypeFunctionsFactory.IsListFactory::create);
        add(IsTypeFunctions.IsLogical.class, IsTypeFunctionsFactory.IsLogicalFactory::create);
        add(IsTypeFunctions.IsMatrix.class, IsTypeFunctionsFactory.IsMatrixFactory::create);
        add(IsTypeFunctions.IsName.class, IsTypeFunctionsFactory.IsNameFactory::create);
        add(IsTypeFunctions.IsNull.class, IsTypeFunctionsFactory.IsNullFactory::create);
        add(IsTypeFunctions.IsNumeric.class, IsTypeFunctionsFactory.IsNumericFactory::create);
        add(IsTypeFunctions.IsObject.class, IsTypeFunctionsFactory.IsObjectFactory::create);
        add(IsTypeFunctions.IsPairList.class, IsTypeFunctionsFactory.IsPairListFactory::create);
        add(IsTypeFunctions.IsRaw.class, IsTypeFunctionsFactory.IsRawFactory::create);
        add(IsTypeFunctions.IsRecursive.class, IsTypeFunctionsFactory.IsRecursiveFactory::create);
        add(IsTypeFunctions.IsVector.class, IsTypeFunctionsFactory.IsVectorFactory::create);
        add(IsUnsorted.class, IsUnsortedFactory::create);
        add(LaFunctions.DetGeReal.class, LaFunctionsFactory.DetGeRealFactory::create);
        add(LaFunctions.LaChol.class, LaFunctionsFactory.LaCholFactory::create);
        add(LaFunctions.Qr.class, LaFunctionsFactory.QrFactory::create);
        add(LaFunctions.QrCoefReal.class, LaFunctionsFactory.QrCoefRealFactory::create);
        add(LaFunctions.Rg.class, LaFunctionsFactory.RgFactory::create);
        add(LaFunctions.Version.class, LaFunctionsFactory.VersionFactory::create);
        add(Lapply.class, LapplyFactory::create);
        add(Length.class, LengthFactory::create);
        add(License.class, LicenseFactory::create);
        add(ListBuiltin.class, ListBuiltinFactory::create);
        add(LoadFunctions.LoadFromConn2.class, LoadFunctionsFactory.LoadFromConn2Factory::create);
        add(LocaleFunctions.BindTextDomain.class, LocaleFunctionsFactory.BindTextDomainFactory::create);
        add(LocaleFunctions.Enc2Native.class, LocaleFunctionsFactory.Enc2NativeFactory::create);
        add(LocaleFunctions.Enc2Utf8.class, LocaleFunctionsFactory.Enc2Utf8Factory::create);
        add(LocaleFunctions.GetLocale.class, LocaleFunctionsFactory.GetLocaleFactory::create);
        add(LocaleFunctions.L10nInfo.class, LocaleFunctionsFactory.L10nInfoFactory::create);
        add(LocaleFunctions.LocaleConv.class, LocaleFunctionsFactory.LocaleConvFactory::create);
        add(LocaleFunctions.SetLocale.class, LocaleFunctionsFactory.SetLocaleFactory::create);
        add(LogFunctions.Log.class, LogFunctionsFactory.LogFactory::create);
        add(LogFunctions.Log10.class, LogFunctionsFactory.Log10Factory::create);
        add(LogFunctions.Log1p.class, LogFunctionsFactory.Log1pFactory::create);
        add(LogFunctions.Log2.class, LogFunctionsFactory.Log2Factory::create);
        add(Ls.class, LsFactory::create);
        add(MakeNames.class, MakeNamesFactory::create);
        add(MakeUnique.class, MakeUniqueFactory::create);
        add(MatMult.class, MatMultFactory::create);
        add(Match.class, MatchFactory::create);
        add(MatchFun.class, MatchFunFactory::create);
        add(Matrix.class, MatrixFactory::create);
        add(Max.class, Max::new);
        add(Mean.class, MeanFactory::create);
        add(Min.class, Min::new);
        add(Missing.class, MissingFactory::create);
        add(Mod.class, ModFactory::create);
        add(NArgs.class, NArgsFactory::create);
        add(NChar.class, NCharFactory::create);
        add(NGetText.class, NGetTextFactory::create);
        add(NZChar.class, NZCharFactory::create);
        add(Names.class, NamesFactory::create);
        add(NamespaceFunctions.GetNamespaceRegistry.class, NamespaceFunctionsFactory.GetNamespaceRegistryFactory::create);
        add(NamespaceFunctions.GetRegisteredNamespace.class, NamespaceFunctionsFactory.GetRegisteredNamespaceFactory::create);
        add(NamespaceFunctions.IsNamespaceEnv.class, NamespaceFunctionsFactory.IsNamespaceEnvFactory::create);
        add(NamespaceFunctions.RegisterNamespace.class, NamespaceFunctionsFactory.RegisterNamespaceFactory::create);
        add(NamespaceFunctions.UnregisterNamespace.class, NamespaceFunctionsFactory.UnregisterNamespaceFactory::create);
        add(NormalizePath.class, NormalizePathFactory::create);
        add(OnExit.class, OnExitFactory::create);
        add(Options.class, OptionsFactory::create);
        add(Order.class, OrderFactory::create);
        add(PMatch.class, PMatchFactory::create);
        add(PMinMax.PMax.class, PMinMaxFactory.PMaxFactory::create);
        add(PMinMax.PMin.class, PMinMaxFactory.PMinFactory::create);
        add(Parse.class, ParseFactory::create);
        add(Paste.class, PasteFactory::create);
        add(Paste0.class, Paste0Factory::create);
        add(PathExpand.class, PathExpandFactory::create);
        add(PrimTrace.class, PrimTraceFactory::create);
        add(Primitive.class, PrimitiveFactory::create);
        add(PrintFunctions.PrintDefault.class, PrintFunctionsFactory.PrintDefaultFactory::create);
        add(PrintFunctions.PrintFunction.class, PrintFunctionsFactory.PrintFunctionFactory::create);
        add(ProcTime.class, ProcTimeFactory::create);
        add(Prod.class, ProdFactory::create);
        add(Quit.class, QuitFactory::create);
        add(Quote.class, QuoteFactory::create);
        add(RNGFunctions.RNGkind.class, RNGFunctionsFactory.RNGkindFactory::create);
        add(RNGFunctions.SetSeed.class, RNGFunctionsFactory.SetSeedFactory::create);
        add(RVersion.class, RVersionFactory::create);
        add(RawFunctions.CharToRaw.class, RawFunctionsFactory.CharToRawFactory::create);
        add(Re.class, Re::new);
        add(ReadDCF.class, ReadDCFFactory::create);
        add(ReadREnviron.class, ReadREnvironFactory::create);
        add(Recall.class, Recall::new);
        add(Repeat.class, RepeatFactory::create);
        add(RepeatInternal.class, RepeatInternalFactory::create);
        add(RepeatLength.class, RepeatLengthFactory::create);
        add(Return.class, ReturnFactory::create);
        add(Rhome.class, RhomeFactory::create);
        add(Rm.class, RmFactory::create);
        add(Round.class, RoundFactory::create);
        add(RowMeans.class, RowMeansFactory::create);
        add(RowSums.class, RowSumsFactory::create);
        add(S3DispatchFunctions.NextMethod.class, S3DispatchFunctionsFactory.NextMethodFactory::create);
        add(S3DispatchFunctions.UseMethod.class, S3DispatchFunctionsFactory.UseMethodFactory::create);
        add(Sample.class, SampleFactory::create);
        add(Scan.class, ScanFactory::create);
        add(Seq.class, SeqFactory::create);
        add(SeqAlong.class, SeqAlongFactory::create);
        add(SeqLen.class, SeqLenFactory::create);
        add(SerializeFunctions.Serialize.class, SerializeFunctionsFactory.SerializeFactory::create);
        add(SerializeFunctions.SerializeB.class, SerializeFunctionsFactory.SerializeBFactory::create);
        add(SerializeFunctions.SerializeToConn.class, SerializeFunctionsFactory.SerializeToConnFactory::create);
        add(SerializeFunctions.Unserialize.class, SerializeFunctionsFactory.UnserializeFactory::create);
        add(SerializeFunctions.UnserializeFromConn.class, SerializeFunctionsFactory.UnserializeFromConnFactory::create);
        add(Setwd.class, SetwdFactory::create);
        add(ShortRowNames.class, ShortRowNamesFactory::create);
        add(Sign.class, SignFactory::create);
        add(Signif.class, SignifFactory::create);
        add(SinkFunctions.Sink.class, SinkFunctionsFactory.SinkFactory::create);
        add(SinkFunctions.SinkNumber.class, SinkFunctionsFactory.SinkNumberFactory::create);
        add(SortFunctions.PartialSort.class, SortFunctionsFactory.PartialSortFactory::create);
        add(SortFunctions.QSort.class, SortFunctionsFactory.QSortFactory::create);
        add(SortFunctions.RadixSort.class, SortFunctionsFactory.RadixSortFactory::create);
        add(SortFunctions.Sort.class, SortFunctionsFactory.SortFactory::create);
        add(Split.class, SplitFactory::create);
        add(Sprintf.class, SprintfFactory::create);
        add(Sqrt.class, SqrtFactory::create);
        add(Stop.class, StopFactory::create);
        add(Strtoi.class, StrtoiFactory::create);
        add(Structure.class, StructureFactory::create);
        add(Substitute.class, SubstituteFactory::create);
        add(Substr.class, SubstrFactory::create);
        add(Sum.class, Sum::new);
        add(Switch.class, SwitchFactory::create);
        add(SysFunctions.SysChmod.class, SysFunctionsFactory.SysChmodFactory::create);
        add(SysFunctions.SysGetenv.class, SysFunctionsFactory.SysGetenvFactory::create);
        add(SysFunctions.SysGetpid.class, SysFunctionsFactory.SysGetpidFactory::create);
        add(SysFunctions.SysGlob.class, SysFunctionsFactory.SysGlobFactory::create);
        add(SysFunctions.SysInfo.class, SysFunctionsFactory.SysInfoFactory::create);
        add(SysFunctions.SysReadlink.class, SysFunctionsFactory.SysReadlinkFactory::create);
        add(SysFunctions.SysSetEnv.class, SysFunctionsFactory.SysSetEnvFactory::create);
        add(SysFunctions.SysSleep.class, SysFunctionsFactory.SysSleepFactory::create);
        add(SysFunctions.SysTime.class, SysFunctionsFactory.SysTimeFactory::create);
        add(SysFunctions.SysUmask.class, SysFunctionsFactory.SysUmaskFactory::create);
        add(SysFunctions.SysUnSetEnv.class, SysFunctionsFactory.SysUnSetEnvFactory::create);
        add(SystemFunction.class, SystemFunctionFactory::create);
        add(Tabulate.class, TabulateFactory::create);
        add(TempDir.class, TempDirFactory::create);
        add(TempFile.class, TempFileFactory::create);
        add(ToLower.class, ToLowerFactory::create);
        add(ToUpper.class, ToUpperFactory::create);
        add(Transpose.class, TransposeFactory::create);
        add(TrigExpFunctions.Acos.class, TrigExpFunctionsFactory.AcosFactory::create);
        add(TrigExpFunctions.Acosh.class, TrigExpFunctionsFactory.AcoshFactory::create);
        add(TrigExpFunctions.Asin.class, TrigExpFunctionsFactory.AsinFactory::create);
        add(TrigExpFunctions.Asinh.class, TrigExpFunctionsFactory.AsinhFactory::create);
        add(TrigExpFunctions.Atan.class, TrigExpFunctionsFactory.AtanFactory::create);
        add(TrigExpFunctions.Atan2.class, TrigExpFunctionsFactory.Atan2Factory::create);
        add(TrigExpFunctions.Atanh.class, TrigExpFunctionsFactory.AtanhFactory::create);
        add(TrigExpFunctions.Cos.class, TrigExpFunctionsFactory.CosFactory::create);
        add(TrigExpFunctions.Cosh.class, TrigExpFunctionsFactory.CoshFactory::create);
        add(TrigExpFunctions.Cospi.class, TrigExpFunctionsFactory.CospiFactory::create);
        add(TrigExpFunctions.Exp.class, TrigExpFunctionsFactory.ExpFactory::create);
        add(TrigExpFunctions.ExpM1.class, TrigExpFunctionsFactory.ExpM1Factory::create);
        add(TrigExpFunctions.Sin.class, TrigExpFunctionsFactory.SinFactory::create);
        add(TrigExpFunctions.Sinh.class, TrigExpFunctionsFactory.SinhFactory::create);
        add(TrigExpFunctions.Sinpi.class, TrigExpFunctionsFactory.SinpiFactory::create);
        add(TrigExpFunctions.Tan.class, TrigExpFunctionsFactory.TanFactory::create);
        add(TrigExpFunctions.Tanh.class, TrigExpFunctionsFactory.TanhFactory::create);
        add(TrigExpFunctions.Tanpi.class, TrigExpFunctionsFactory.TanpiFactory::create);
        add(Trunc.class, TruncFactory::create);
        add(Typeof.class, TypeofFactory::create);
        add(UnClass.class, UnClassFactory::create);
        add(Unique.class, UniqueFactory::create);
        add(Unlist.class, UnlistFactory::create);
        add(UpdateAttr.class, UpdateAttrFactory::create);
        add(UpdateAttributes.class, UpdateAttributesFactory::create);
        add(UpdateClass.class, UpdateClassFactory::create);
        add(UpdateDiag.class, UpdateDiagFactory::create);
        add(UpdateDim.class, UpdateDimFactory::create);
        add(UpdateDimNames.class, UpdateDimNamesFactory::create);
        add(UpdateLength.class, UpdateLengthFactory::create);
        add(UpdateLevels.class, UpdateLevelsFactory::create);
        add(UpdateNames.class, UpdateNamesFactory::create);
        add(UpdateOldClass.class, UpdateOldClassFactory::create);
        add(UpdateStorageMode.class, UpdateStorageModeFactory::create);
        add(UpdateSubstr.class, UpdateSubstrFactory::create);
        add(UpperTri.class, UpperTriFactory::create);
        add(VApply.class, VApplyFactory::create);
        add(Vector.class, VectorFactory::create);
        add(Warning.class, WarningFactory::create);
        add(WhichFunctions.Which.class, WhichFunctionsFactory.WhichFactory::create);
        add(WhichFunctions.WhichMax.class, WhichFunctionsFactory.WhichMaxFactory::create);
        add(WhichFunctions.WhichMin.class, WhichFunctionsFactory.WhichMinFactory::create);
    }
}
