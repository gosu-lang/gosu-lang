/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/* Guidewire Software
 *
 * Creator information:
 * User: <YOUR-USERID>
 * Date: Fri Dec 08 08:20:46 PST 2006
 *
 * Revision information:
 */

package gw.internal.gosu.parser;

import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.IExpression;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.util.GosuTestUtil;
import gw.test.TestClass;
import junit.framework.Assert;

/**
 * Tests the {@link ErrorType} implementation.
 */
public class ErrorTypeTest extends TestClass {  
  public void testUniversalTypeExists() throws ParseResultsException
  {
    StandardSymbolTable symTable = new StandardSymbolTable(true);
    IExpression expression = GosuTestUtil.compileExpression("var x : ErrorType", symTable);
    Assert.assertNotNull(expression);
  }
/*
  public void testUniversalTypeCanCompileWithAribitraryMethods() throws ParseResultsException {
    StandardSymbolTable symTable = new StandardSymbolTable(true);

    IExpression expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                              "x.foo()", symTable);
    Assert.assertNotNull(expression);

    expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                   "x.foo(1, 2, 3);", symTable);
    Assert.assertNotNull(expression);

    expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                   "x.foo().bar()", symTable);
    Assert.assertNotNull(expression);

  }

  public void testUniversalTypeCanBeAssignedToByAnything() throws ParseResultsException {
    StandardSymbolTable symTable = new StandardSymbolTable(true);

    IExpression expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                              "x = 10;", symTable);
    Assert.assertNotNull(expression);

    expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                   "x = new Object();", symTable);
    Assert.assertNotNull(expression);

    expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                   "x = \"\"", symTable);
    Assert.assertNotNull(expression);

  }

  public void testErrorTypeProvidesAllMethods() throws ParseResultsException {

    StandardSymbolTable symTable = new StandardSymbolTable(true);
    IExpression expression;
    expression = GosuTestUtil.compileExpression("var x : ErrorType; \n" +
                                   "var y = x.foo(1, 2, 3); \n" +
                                   "y.shortBus()\n", symTable);
    Assert.assertNotNull(expression);
  }


  public void testUniversalTypeCanAccessAribtraryProperties() throws ParseResultsException {
    StandardSymbolTable symTable = new StandardSymbolTable(true);

    IExpression expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                              "var y = x.Foo", symTable);
    Assert.assertNotNull(expression);

    expression = GosuTestUtil.compileExpression("var x : ErrorType; " +
                                   "var y = x.Foo.Bar", symTable);
    Assert.assertNotNull(expression);
  }
*/
  public void testUniversalTypeHasCorrectRelativeName() throws ParseResultsException {
    IType errorType = ErrorType.getInstance( "foo.bar" );
    assertTrue( errorType instanceof IErrorType );
    assertEquals( "foo.bar", errorType.getName() );
    assertEquals( "bar", errorType.getRelativeName() );
  }

}