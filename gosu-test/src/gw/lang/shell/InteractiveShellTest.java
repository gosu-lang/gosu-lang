/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.shell;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InteractiveShellTest extends TestClass
{
  public void testSimpleEvaluation() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals( 2, gs.interactivelyEvaluate( "1 + 1" ) );
  }

  public void testFunctionDefinitionThenUse() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals( null, gs.interactivelyEvaluate( "function foo() : boolean { \n" +
                                                  "  return true\n" +
                                                  "}" ) );
    assertEquals( true, gs.interactivelyEvaluate( "foo()" ));
  }

  public void testVarDefinitionThenUse() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals( null, gs.interactivelyEvaluate( "var foo = true" ) );
    assertEquals( true, gs.interactivelyEvaluate( "foo" ));
  }

  public void testTypeUsesPersistsAcrossInvocations() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals(  null, gs.interactivelyEvaluate( "uses java.util.*" ) );
    assertNotNull( gs.interactivelyEvaluate( "new Date()" ) );
  }

  public void testBasicSymbolCompletionWorks() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals(  null, gs.interactivelyEvaluate( "var foo = true" ) );
    ArrayList completions = new ArrayList();
    assertEquals( 0, gs.complete( "fo", completions ) );
    assertEquals( Arrays.asList( "foo" ), completions );
  }

  public void testMultipleSymbolCompletionWorks() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals( null , gs.interactivelyEvaluate( "var foo = true" ) );
    assertEquals( null, gs.interactivelyEvaluate( "var foo2 = true" ) );
    assertEquals( null, gs.interactivelyEvaluate( "var foo3 = true" ) );
    assertEquals( null, gs.interactivelyEvaluate( "var bar = true" ) );
    assertEquals( null, gs.interactivelyEvaluate( "var fa = true" ) );
    ArrayList completions = new ArrayList();
    assertEquals( 0, gs.complete( "fo", completions ) );
    assertEquals( Arrays.asList( "foo", "foo2", "foo3" ), completions );
  }

  public void testBasicTypeInfoCompletionWorks() throws IOException, ParseResultsException
  {
    InteractiveShell gs = new InteractiveShell();
    assertEquals( null, gs.interactivelyEvaluate( "var foo = new java.util.Date()" ) );
    ArrayList completions = new ArrayList();
    assertEquals( 4, gs.complete( "foo.H", completions ) );
    assertTrue( completions.contains("Hours") );
  }

  public void testClassPathEntriesIgnoredInProgram() throws ParseResultsException
  {
    IGosuParser parser = GosuParserFactory.createParser( "classpath \"\"\n" +
                                                                             "return 1 + 1" );
    assertEquals( 2, parser.parseProgram( null ).evaluate() );
  }

}
