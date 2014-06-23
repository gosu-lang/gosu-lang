/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;

/**
 */
public class EnumDisplayNamePropertySymbol extends DynamicPropertySymbol
{
  public EnumDisplayNamePropertySymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( new EnumDisplayNameFunctionSymbol( gsClass, symTable ), true );
    setClassMember( true );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( false );
  }
}