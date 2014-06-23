/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.IProgramClassFunctionSymbol;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.IFunctionType;

/**
 */
public class ProgramClassFunctionSymbol extends DynamicFunctionSymbol implements IProgramClassFunctionSymbol
{

  public ProgramClassFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable, GosuMethodInfo mi )
  {
    super( symTable, mi.getDisplayName(), (IFunctionType) mi.getDfs().getType(),
           ReducedSymbol.makeArgs(mi.getArgs()), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    setClassMember( true );
    setName( mi.getName() );
    _scriptPartId = new ScriptPartId( gsClass, null );
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }
}