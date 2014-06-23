/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;


/**
 */
public class IntrinsicTypeFunctionSymbol extends DynamicFunctionSymbol
{
  public IntrinsicTypeFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( symTable, "@IntrinsicType",
           new FunctionType( "@IntrinsicType", JavaTypes.ITYPE(), null ),
           Collections.<ISymbol>emptyList(), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( false );
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }
}