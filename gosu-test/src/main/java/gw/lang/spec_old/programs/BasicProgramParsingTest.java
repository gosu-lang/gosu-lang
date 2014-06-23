/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.spec_old.programs;

import gw.test.TestClass;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.exceptions.ParseResultsException;

import java.util.List;

public class BasicProgramParsingTest extends TestClass
{

  public void testClassPathsWithGoodPathPasses() throws ParseResultsException
  {
    IGosuParser scriptParser = GosuParserFactory.createParser( "classpath \"foo\"" );
    IProgram iProgram = scriptParser.parseProgram( null );
    List<IParseIssue> issues = iProgram.getParseIssues();
    assertEquals( 0, issues.size() );

    scriptParser = GosuParserFactory.createParser( "classpath \"foo\"\n" +
                                                      "classpath \"bar\"" );
    iProgram = scriptParser.parseProgram( null );
    issues = iProgram.getParseIssues();
    assertEquals( 0, issues.size() );
  }

  public void testClassPathsWithBadPathsWarn() throws ParseResultsException
  {
    IGosuParser scriptParser = GosuParserFactory.createParser( "classpath \"foo;bar\"" );
    IProgram iProgram = scriptParser.parseProgram( null );
    List<IParseIssue> warnings = iProgram.getParseWarnings();
    assertEquals( 1, warnings.size() );
    assertEquals( Res.MSG_COMMA_IS_THE_CLASSPATH_SEPARATOR, warnings.get(0).getMessageKey() );
  }
  
}
