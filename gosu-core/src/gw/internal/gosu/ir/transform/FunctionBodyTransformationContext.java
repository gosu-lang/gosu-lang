/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.parser.Keyword;
import gw.util.Stack;

import java.util.List;

/**
 */
public class FunctionBodyTransformationContext
{
  public static final String TEMP_VAR_PREFIX = "*temp";
  private int _tempVarCount = 0;

  private Stack<IRScope> _scopes;
  private ConstructorState _constructorState;

  protected TopLevelTransformationContext _context;
  private boolean _isStatic;

  public FunctionBodyTransformationContext(TopLevelTransformationContext context, boolean isStatic) {
    _context = context;
    _isStatic = isStatic;
  }

  private enum ConstructorState {
    PRE_SUPER,
    SUPER_INVOKED,
    CLEAR
  }

  public void updateSuperInvokedAfterLastExpressionCompiles()
  {
    if( _constructorState == ConstructorState.SUPER_INVOKED )
    {
      _constructorState = ConstructorState.CLEAR;
    }
  }

  public boolean hasSuperBeenInvoked()
  {
    return _constructorState == ConstructorState.CLEAR;
  }

  public void markSuperInvoked()
  {
    _constructorState = ConstructorState.SUPER_INVOKED;
  }

  public IRSymbol makeAndIndexTempSymbol( IRType type )
  {
    return makeAndIndexTempSymbol( null, type );
  }
  public IRSymbol makeAndIndexTempSymbol( String strNameSuffix, IRType type )
  {
    String strName = strNameSuffix != null ? TEMP_VAR_PREFIX + strNameSuffix : TEMP_VAR_PREFIX + _tempVarCount++;
    IRSymbol symbol = new IRSymbol( strName, type, true);
    _scopes.peek().addSymbol( symbol );
    return symbol;
  }

  public String makeTempSymbolName( )
  {
    String strName = TEMP_VAR_PREFIX + _tempVarCount;
    _tempVarCount ++;
    return strName;
  }

  public void pushScope( boolean bInitialInstanceMethodScope )
  {
    if( _scopes == null )
    {
      _scopes = new Stack<IRScope>();
    }
    IRScope parent = _scopes.isEmpty() ? null : _scopes.peek();
    _scopes.push( new IRScope( parent ) );
    if( bInitialInstanceMethodScope )
    {
      assert parent == null;
      _scopes.peek().addSymbol( Keyword.KW_this.getName(), _context.getIRTypeForCurrentClass() );
    }
  }

  public void popScope()
  {
    _scopes.pop();
  }

  public IRSymbol getTypeParamIndex( TypeVariableType type )
  {
    return _scopes.peek().getSymbol( AbstractElementTransformer.TYPE_PARAM_PREFIX + type.getRelativeName() );
  }

  public IRSymbol getSymbol(String symbolName) {
    IRSymbol symbol = _scopes.peek().getSymbol( symbolName );
    if (symbol == null) {
      throw new IllegalStateException("No symbol found named " + symbolName);
    }
    return symbol;
  }

  public boolean hasSymbol(String symbolName)
  {
    return _scopes.peek().getSymbol( symbolName ) != null;
  }

  public void putSymbols(List<IRSymbol> symbols) {
    for (IRSymbol symbol : symbols) {
      putSymbol(symbol);
    }
  }

  public void putSymbol(IRSymbol symbol) {
    _scopes.peek().addSymbol(symbol);
  }

  public IRSymbol createSymbol(String name, IRType type) {
    return _scopes.peek().addSymbol(name, type);
  }

  public Stack<IRScope> getScopes()
  {
    return _scopes;
  }

  public boolean isBlockInvoke()
  {
    return false;
  }

  public DynamicFunctionSymbol getCurrentDFS()
  {
    return null;
  }

  public boolean isStatic() {
    return _isStatic;
  }
}
