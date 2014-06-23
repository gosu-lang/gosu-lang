/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;

/**
 */
public class EnumAllValuesPropertySymbol extends DynamicPropertySymbol
{
  public EnumAllValuesPropertySymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( new EnumAllValuesFunctionSymbol( gsClass, symTable ), true );
    setClassMember( false );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( true );
  }
}
