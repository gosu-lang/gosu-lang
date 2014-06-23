/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IParserPart;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.ISymbolTable;

import java.util.List;

public interface IGosuClassParser extends IParserPart
{
  void parseDeclarations( IGosuClass gsClass ) throws ParseResultsException;

  void parseDefinitions( IGosuClass gsClass ) throws ParseResultsException;

  List<ParseException> resolveFunctionAndPropertyDecls( ISymbolTable table );
}
