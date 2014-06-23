/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.parser.GosuParserTypes;
import gw.lang.reflect.FunctionType;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IInitConstructorFunctionSymbol;

/**
 */
public class InitConstructorFunctionSymbol extends DynamicFunctionSymbol implements IInitConstructorFunctionSymbol
{
  /**
   */
  public InitConstructorFunctionSymbol( ISymbolTable symTable )
  {
    super( symTable, "_init", new FunctionType( "_init", GosuParserTypes.NULL_TYPE(), null ), null, (Statement)null );
  }
}
