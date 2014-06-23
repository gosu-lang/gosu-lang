/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.reflect.FunctionType;

import gw.lang.reflect.IType;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.java.JavaTypes;

import java.util.Arrays;

/**
 */
public class EnumValueOfFunctionSymbol extends DynamicFunctionSymbol
{
  public EnumValueOfFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( symTable, "valueOf",
           new FunctionType( "valueOf", gsClass, new IType[]{JavaTypes.STRING()} ),
           Arrays.asList( (ISymbol)new Symbol( "strName", JavaTypes.STRING(), (Object)null ) ),
           new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setClassMember( true );
    setStatic( true );
  }
}
