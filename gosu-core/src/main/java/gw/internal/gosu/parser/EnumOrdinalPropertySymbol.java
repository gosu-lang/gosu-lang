/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;

/**
 */
public class EnumOrdinalPropertySymbol extends DynamicPropertySymbol
{
  public EnumOrdinalPropertySymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( new EnumOrdinalFunctionSymbol( gsClass, symTable ), true );
    setClassMember( true );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( false );
  }
}