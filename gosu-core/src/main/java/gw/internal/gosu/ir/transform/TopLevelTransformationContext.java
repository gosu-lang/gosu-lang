/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.parser.TypeVariableType;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.parser.IStatement;
import gw.util.Stack;

import java.util.List;

public interface TopLevelTransformationContext {

  void maybeAssignOuterRef(List<IRStatement> statements);

  void maybePushSupersEnclosingThisRef( List<IRExpression> arguments );

  void pushEnumNameAndOrdinal( IType type, List<IRExpression> args );

  void initCapturedSymbolFields( List<IRStatement> statements );

  void initTypeVarFields( List<IRStatement> statements );

  void initializeInstanceFields( List<IRStatement> statements );

  void addAssertionsStaticField();

  ICompilableTypeInternal getGosuClass();

  IRClass getIrClass();
  void setIrClass( IRClass irClass );

  boolean shouldUseReflection( IType declaringClass, IRType root, IRelativeTypeInfo.Accessibility accessibility );
  boolean isIllegalProtectedCall( IType declaringClass, IRType root, IRelativeTypeInfo.Accessibility accessibility );

  String getOuterThisFieldName();

  String getOuterThisParamName();

  IType getSuperType();

  ICompilableTypeInternal getEnclosingType();

  String getSourceFileRef();

  String getSourceLine(int lineNumber);

  boolean isNonStaticInnerClass();

  boolean compilingEnhancement();

  boolean compilingEnum();

  boolean compilingBlock();

  boolean compilingProgram();

  IRType getIRTypeForCurrentClass();

  boolean isExternalSymbol( String name );

  // Calls that previously were on FunctionStatementTransformer

  IRSymbol makeAndIndexTempSymbol( IRType type );
  public IRSymbol makeAndIndexTempSymbol( String strNameSuffix, IRType type );

  IRSymbol getTypeParamIndex( TypeVariableType type );

  IRSymbol getSymbol(String symbolName);

  boolean hasSymbol( String strSymbol );

  void pushScope( boolean bInitialInstanceMethodScope );

  void popScope();

  void putSymbol(IRSymbol symbol);

  void putSymbols(List<IRSymbol> symbols);

  String makeTempSymbolName();

  boolean isBlockInvoke();

  boolean isFragmentEvaluation();

  IRStatement compile( IStatement stmt );

  IRSymbol createSymbol(String name, IRType type);

  boolean hasSuperBeenInvoked();
  boolean isStatic();
  void markInvokingSuper();
  void markSuperInvoked();
  void updateSuperInvokedAfterLastExpressionCompiles();

  Stack<IRScope> getScopes();

  String getCurrentFunctionName();

  boolean isCurrentFunctionStatic();

  IType getCurrentFunctionReturnType();

  IRStatement compileInitializerAssignment( InitializerAssignment stmt, IRExpression root );

  boolean currentlyCompilingBlock();

  DynamicFunctionSymbol getCurrentFunction();

  int incrementLazyTypeMethodCount();
}
