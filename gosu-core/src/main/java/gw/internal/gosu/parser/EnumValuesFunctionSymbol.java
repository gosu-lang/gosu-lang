/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;

import java.util.Collections;

/**
 */
public class EnumValuesFunctionSymbol extends DynamicFunctionSymbol
{
  public EnumValuesFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( symTable, "values",
           new FunctionType( "values", gsClass.getArrayType(), new IType[]{} ),
           Collections.<ISymbol>emptyList(),
           new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setClassMember( true );
    setStatic( true );
  }
}
