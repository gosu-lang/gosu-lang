/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;

/**
 */
public class IntrinsicTypePropertySymbol extends DynamicPropertySymbol
{
  public IntrinsicTypePropertySymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( new IntrinsicTypeFunctionSymbol( gsClass, symTable ), true );
    setClassMember( true );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( false );
  }
}