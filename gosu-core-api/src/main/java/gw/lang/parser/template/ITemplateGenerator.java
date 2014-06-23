/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.IType;

import java.io.Writer;
import java.util.List;

public interface ITemplateGenerator
{
  String PRINT_METHOD = "printContent";
  String PRINT_COMPRESSED_METHOD = "printCompressed";
  String PRINT_RANGE_METHOD = "printRange";

  void execute( Writer writer, ISymbolTable symbolTable ) throws TemplateParseException;

  void execute( Writer writer, StringEscaper escaper, ISymbolTable symTable ) throws TemplateParseException;

  void compile( ISymbolTable symTable ) throws TemplateParseException;

  void verify( IGosuParser parser ) throws ParseResultsException;

  boolean isValid();

  List<ISymbol> getParameters();

  IType getSuperType();

  String getFullyQualifiedTypeName();

  List<TemplateParseException> getTemplateSyntaxProblems();

  IProgram getProgram();

  String getSource();
}
