/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.IForwardingFunctionSymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;

import java.util.Arrays;
import java.util.Collections;

/**
 */
public class ProgramExecuteFunctionSymbol extends DynamicFunctionSymbol implements IForwardingFunctionSymbol
{
  public ProgramExecuteFunctionSymbol( IType gsClass, ISymbolTable symTable )
  {
    this( gsClass, symTable, false );
  }
  public ProgramExecuteFunctionSymbol( IType gsClass, ISymbolTable symTable, boolean bArgs )
  {
    super( symTable, bArgs ? "executeWithArgs" : "execute",
           new FunctionType( bArgs ? "executeWithArgs" : "execute", JavaTypes.OBJECT(), bArgs ? new IType[] {JavaTypes.STRING().getArrayType()} : IType.EMPTY_ARRAY ),
           bArgs ? Arrays.asList( new Symbol( "pArgs", JavaTypes.STRING().getArrayType(), null ) ) : Collections.emptyList(), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    setClassMember( true );
    setName( bArgs ? "executeWithArgs" : "execute" );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( true );
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }
}
