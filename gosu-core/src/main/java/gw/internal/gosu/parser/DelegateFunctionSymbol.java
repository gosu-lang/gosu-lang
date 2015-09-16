/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.DelegateStatement;
import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IDelegateFunctionSymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;

/**
 */
public class DelegateFunctionSymbol extends DynamicFunctionSymbol implements IDelegateFunctionSymbol
{
  private DelegateStatement _delegateStmt;
  private IMethodInfo _mi;

  public DelegateFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable, ReducedDynamicFunctionSymbol dfs, IMethodInfo mi, DelegateStatement delegateStmt )
  {
    super( symTable, dfs.getDisplayName(),
           (IFunctionType)dfs.getType(),
           ReducedSymbol.makeArgs(dfs.getArgs()), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    setClassMember( true );
    setName( dfs.getName() );
    _scriptPartId = new ScriptPartId( gsClass, null );
    _delegateStmt = delegateStmt;
    _mi = mi;
    setOverride( true );
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }

  public DelegateStatement getDelegateStmt() {
    return _delegateStmt;
  }

  public IMethodInfo getMi() {
    return _mi;
  }

  public IReducedDynamicFunctionSymbol createReducedSymbol() {
    return new ReducedDelegateFunctionSymbol(this);
  }

}
