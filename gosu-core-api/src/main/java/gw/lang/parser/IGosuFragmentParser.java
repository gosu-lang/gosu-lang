/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.parser.exceptions.ParseResultsException;

public interface IGosuFragmentParser {

  IGosuFragment parseExpressionOnly( String script, ISymbolTable table, ParserOptions options) throws ParseResultsException;

  IGosuFragment parseProgramOnly( String script, ISymbolTable table, ParserOptions options) throws ParseResultsException;

  IGosuFragment parseExpressionOrProgram(String script, ISymbolTable table, ParserOptions options) throws ParseResultsException;

}
