/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class GosuObjectFunctionSymbol extends DynamicFunctionSymbol
{
  private IMethodInfo _mi;

  public GosuObjectFunctionSymbol( IGosuClassInternal gsClass, DynamicFunctionSymbol dfs )
  {
    super( dfs.getSymbolTable(), dfs.getDisplayName(),
           (IFunctionType)dfs.getType(),
           dfs.getArgs(), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    setClassMember( true );
    setName( (String)dfs.getName() );
    _scriptPartId = new ScriptPartId( gsClass, null );
    _mi = JavaTypes.IGOSU_OBJECT().getTypeInfo().getMethod( dfs.getDisplayName(), ((IFunctionType)dfs.getType()).getParameterTypes() );
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }
}