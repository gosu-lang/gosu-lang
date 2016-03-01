/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

public class TryCatchFinally_NoFinally_StatementTest extends ByteCodeTestBase
{
  public void testTryCatch_NoException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatch_NoException" );
    String ret = (String)val;
    assertEquals( "try", ret );
  }

  public void testTryCatch_ExceptionTriggeredInTry_After() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();
    invokeMethod( obj,  "testTryCatch_ExceptionTriggeredInTry_After" );
    assertEquals( "trycatch", invokeMethod( obj, "getValue" ) );
  }

  public void testTryCatch_ExceptionTriggeredInTry_Before() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    invokeMethod( obj, "testTryCatch_ExceptionTriggeredInTry_Before" );
    assertEquals( "catch", invokeMethod( obj, "getValue" ) );
  }

  public void testTryCatch_ReturnFromFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatch_ReturnFromTry" );
    String ret = (String)val;
    assertEquals( "try", ret );
    assertEquals( "try", obj.getClass().getMethod( "getValue" ).invoke( null ) );
  }

  public void testTryCatchFinally_NoException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_NoException" );
    String ret = (String)val;
    assertEquals( "tryfinally", ret );
  }

  public void testTryCatchFinally_WithException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_WithException" );
    String ret = (String)val;
    assertEquals( "trycatchfinally", ret );
  }

  public void testTryCatchFinally_WithNpeException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_WithNpeException" );
    String ret = (String)val;
    assertEquals( "trynpecatchfinally", ret );
  }

  public void testTryCatchFinally_WithSecondException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_WithSecondException" );
    String ret = (String)val;
    assertEquals( "trycatchfinally", ret );
  }

  public void testTryCatchFinally_WithCatchThatThrows() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    try
    {
      Object val = invokeMethod( obj,  "testTryCatchFinally_WithCatchThatThrows" );
    }
    catch( RuntimeException e )
    {
      assertEquals( NullPointerException.class, e.getCause().getCause().getClass() );
      assertEquals( "tryfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
    }
  }

  public void testTryCatchFinally_WithFinallyThatReturnsNull() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_WithFinallyThatReturnsNull" );
    String ret = (String)val;
    assertEquals( "tryfinally", ret );
  }

  public void testTryCatchFinally_WithFinallyThatReturnsNull_AndTryThatThrows() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryCatchFinally_WithFinallyThatReturnsNull_AndTryThatThrows" );
    String ret = (String)val;
    assertEquals( null, ret );
    assertEquals( "catchfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
  }

//## todo: enable after SwitchStatementCompiler is impled
//  public void testTryCatch_BreakFromTryFinallyInSwitch() throws Exception
//  {
//    Object obj = newTryCatchFinallyStatement();
//
//    Object val = invokeMethod( obj,  "testTryCatch_BreakFromTryFinallyInSwitch" );
//    String ret = (String)val;
//    assertEquals( "finallyfinally", ret );
//  }

  private Object newTryCatchFinallyStatement() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.TryCatchStatementTestClass";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}