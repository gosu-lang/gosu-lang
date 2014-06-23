/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;

public class ThisSymbol extends Symbol
{
  public ThisSymbol( IType thisType, ISymbolTable symTable )
  {
    super( Keyword.KW_this.getName(), thisType, symTable, null );
    setModifiers( Modifier.FINAL );
  }
}
