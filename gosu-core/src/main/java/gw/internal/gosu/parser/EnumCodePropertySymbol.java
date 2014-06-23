/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;

/**
 */
public class EnumCodePropertySymbol extends DynamicPropertySymbol
{
  public EnumCodePropertySymbol( IGosuClassInternal gsClass, ISymbolTable symTable )
  {
    super( new EnumCodeFunctionSymbol( gsClass, symTable ), true );
    setClassMember( true );
    _scriptPartId = new ScriptPartId( gsClass, null );
    setStatic( false );
  }
}