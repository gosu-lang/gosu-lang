/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IStatement;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.reflect.IType;

public interface IGosuProgram extends IGosuClass
{
  public static final String NAME_PREFIX = "__Program__";
  public static final String PACKAGE = "program_";
  public static final String PACKAGE_PLUS_DOT = PACKAGE + ".";

  boolean isExpression();
  boolean isLhsExpression();
  IExpression getExpression();
  IStatement getStatement();
  IParsedElement getEnclosingEvalExpression();
  void setEnclosingEvalExpression( IParsedElement evalExprOrAnyExpr );

  Object evaluate(IExternalSymbolMap externalSymbolMap);
  Object evaluateRoot(IExternalSymbolMap externalSymbolMap);
  void assign( Object value );

  IType getReturnType();
  IType getExpectedReturnType();

  IProgramInstance getProgramInstance();

  IType getContextType();

  public static class Runner
  {
    public static Object runProgram( String strProgram ) throws Exception
    {
      return runProgram( strProgram, null, null );
    }
    public static Object runProgram( String strProgram, ISymbolTable symTable, IType expectedType ) throws Exception
    {
      IProgramInstance instance = getProgramInstance( strProgram, symTable, expectedType );
      return instance.evaluate(null);
    }

    public static IProgramInstance getProgramInstance( String strProgram ) throws Exception
    {
      return getProgramInstance( strProgram, null, null );
    }
    public static IProgramInstance getProgramInstance( String strProgram, ISymbolTable symTable ) throws Exception
    {
      return getProgramInstance( strProgram, symTable, null );
    }
    public static IProgramInstance getProgramInstance( String strProgram, ISymbolTable symTable, IType expectedType ) throws Exception
    {
      IGosuProgramParser pcp = GosuParserFactory.createProgramParser();
      IParseResult res = pcp.parseProgramOnly( strProgram, symTable, new ParserOptions().withExpectedType(expectedType) );
      IGosuProgram gp = res.getProgram();
      if( !gp.isValid() )
      {
        throw gp.getParseResultsException();
      }

      Class<?> javaClass = gp.getBackingClass();
      return (IProgramInstance)javaClass.newInstance();
    }
  }
}