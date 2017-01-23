/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.List;

public interface IGosuProgramParser
{
  IParseResult parseExpressionOnly( String strSource, ISymbolTable symTable, ParserOptions options ) throws ParseResultsException;
  IParseResult parseProgramOnly( String strSource, ISymbolTable symTable, ParserOptions options) throws ParseResultsException;
  IParseResult parseExpressionOrProgram( String strSource, ISymbolTable symTable, ParserOptions options ) throws ParseResultsException;
  IParseResult parseTemplate( String strSource, ISymbolTable symTable, ParserOptions options ) throws ParseResultsException;

  IParseResult parseEval( String strSource, List<ICapturedSymbol> symTable, IType enclosingClass, IParsedElement evalExpressionOrAnyCtxElement, ISymbolTable extSyms );

  public IParseResult parseRuntimeExpr( String typeName, String strSource, IType enclosingClass, ISymbolTable extSyms, IParseTree ctxElem );
}
