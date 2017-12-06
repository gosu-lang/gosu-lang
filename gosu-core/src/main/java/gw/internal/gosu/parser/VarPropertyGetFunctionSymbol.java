/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.reflect.FunctionType;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.IType;

import java.util.Collections;

/**
 */
public class VarPropertyGetFunctionSymbol extends DynamicFunctionSymbol
{
  private String _varIdentifier;

  public VarPropertyGetFunctionSymbol( ICompilableType gsClass, ISymbolTable symTable, String strProperty, String strVarIdentifier, IType varType )
  {
    super( symTable, '@' + strProperty,
           new FunctionType( '@' + strProperty, varType, null ),
           Collections.<ISymbol>emptyList(), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    _scriptPartId = new ScriptPartId( gsClass, null );
    _varIdentifier = strVarIdentifier;
  }

  public String getVarIdentifier()
  {
    return _varIdentifier;
  }
}
