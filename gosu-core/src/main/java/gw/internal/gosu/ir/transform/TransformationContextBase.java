/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.TypeVariableType;
import gw.internal.gosu.parser.IGosuEnhancementInternal;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Stack;

import java.util.List;

public abstract class TransformationContextBase implements TopLevelTransformationContext {

  private FunctionBodyTransformationContext _bodyContext;
  private ICompilableTypeInternal _compilingType;
  private IRClass _irClass;
  private int _iLazyMethodCount;

  protected TransformationContextBase( ICompilableTypeInternal compilingType ) {
    _compilingType = compilingType;
  }

  public void initBodyContext( boolean isStatic ) {
    _bodyContext = new FunctionBodyTransformationContext( this, isStatic );
  }

  public void initBodyContext(  boolean isStatic, DynamicFunctionSymbol dfs ) {
    _bodyContext = new DFSFunctionBodyTransformationContext( this, isStatic, dfs );
  }

  public IRClass getIrClass() {
    return _irClass;
  }
  public void setIrClass( IRClass irClass ) {
    _irClass = irClass;
  }

  @Override
  public IRStatement compile( IStatement stmt ) {
    return StatementTransformer.compile( this, stmt );
  }

  @Override
  public IRStatement compileInitializerAssignment( InitializerAssignment stmt, IRExpression root ) {
    return StatementTransformer.compileInitializerAssignment(this, stmt, root);
  }

  @Override
  public boolean currentlyCompilingBlock()
  {
    return false;
  }

  @Override
  public ICompilableTypeInternal getGosuClass() {
    return _compilingType;
  }

  @Override
  public IType getSuperType() {
    return _compilingType.getSupertype() == null
           ? JavaTypes.OBJECT()
           : _compilingType.getSupertype();
  }

  @Override
  public ICompilableTypeInternal getEnclosingType() {
    return _compilingType.getEnclosingType();
  }

  @Override
  public boolean isNonStaticInnerClass() {
    return !_compilingType.isStatic() && _compilingType.getEnclosingType() != null;
  }

    @Override
  public boolean compilingEnhancement() {
    return _compilingType instanceof IGosuEnhancementInternal;
  }

  @Override
  public boolean compilingEnum() {
    return _compilingType.isEnum();
  }

    @Override
  public boolean compilingBlock() {
    return _compilingType instanceof IBlockClass;
  }

  @Override
  public boolean compilingProgram() {
    return _compilingType instanceof IGosuProgram;
  }

  // Methods that rely on the FunctionBodyTransformationContext

  @Override
  public IRSymbol makeAndIndexTempSymbol( IRType type ) {
    return _bodyContext.makeAndIndexTempSymbol( type );
  }
  public IRSymbol makeAndIndexTempSymbol( String strNameSuffix, IRType type ) {
    return _bodyContext.makeAndIndexTempSymbol( strNameSuffix, type );
  }

  @Override
  public IRSymbol getTypeParamIndex( TypeVariableType type ) {
    return _bodyContext.getTypeParamIndex( type );
  }

  @Override
  public IRSymbol getSymbol(String symbolName) {
    return _bodyContext.getSymbol( symbolName );
  }

  @Override
  public boolean hasSymbol( String strSymbol ) {
    return _bodyContext.hasSymbol( strSymbol );
  }

  @Override
  public void pushScope( boolean bInitialInstanceMethodScope ) {
    _bodyContext.pushScope( bInitialInstanceMethodScope );
  }

  @Override
  public void popScope() {
    _bodyContext.popScope();
  }

  @Override
  public void putSymbol(IRSymbol symbol) {
    _bodyContext.putSymbol( symbol );
  }

  @Override
  public void putSymbols(List<IRSymbol> symbols) {
    _bodyContext.putSymbols(symbols);
  }

  @Override
  public String makeTempSymbolName() {
    return _bodyContext.makeTempSymbolName();
  }

  @Override
  public boolean isBlockInvoke() {
    return _bodyContext.isBlockInvoke();
  }

  @Override
  public IRSymbol createSymbol(String name, IRType type) {
    return _bodyContext.createSymbol( name, type );
  }

  @Override
  public boolean hasSuperBeenInvoked() {
    return _bodyContext.hasSuperBeenInvoked();
  }

  @Override
  public boolean isStatic() {
    return _bodyContext.isStatic();
  }

  @Override
  public void markSuperInvoked() {
    _bodyContext.markSuperInvoked();
  }

  @Override
  public void markInvokingSuper() {
    _bodyContext.markInvokingSuper();
  }

  @Override
  public void updateSuperInvokedAfterLastExpressionCompiles() {
    _bodyContext.updateSuperInvokedAfterLastExpressionCompiles();
  }

  @Override
  public Stack<IRScope> getScopes() {
    return _bodyContext.getScopes();
  }

  // These methods should all be rolled directly onto FunctionBodyContext, I believe

  @Override
  public String getCurrentFunctionName() {
    DynamicFunctionSymbol functionSymbol = _bodyContext.getCurrentDFS();
    return (functionSymbol == null ? null : functionSymbol.getName());
  }

  @Override
  public DynamicFunctionSymbol getCurrentFunction() {
    return _bodyContext.getCurrentDFS();
  }

  @Override
  public int incrementLazyTypeMethodCount() {
    return _iLazyMethodCount++;
  }

  @Override
  public boolean isCurrentFunctionStatic() {
    return _bodyContext.isStatic();
  }

  @Override
  public IType getCurrentFunctionReturnType() {
    DynamicFunctionSymbol functionSymbol = _bodyContext.getCurrentDFS();
    return (functionSymbol == null ? null : functionSymbol.getReturnType());
  }

}
