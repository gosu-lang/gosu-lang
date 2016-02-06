/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

public class TryCatchFinally_NoCatch_StatementTest extends ByteCodeTestBase
{
  public void testTryFinally_NoException() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_NoException" );
    String ret = (String)val;
    assertEquals( "tryfinally", ret );
  }

  public void testTryFinally_ExceptionTriggeredInTry_After() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    try
    {
      Object val = invokeMethod( obj,  "testTryFinally_ExceptionTriggeredInTry_After" );
      fail( "Should have thrown npe" );
    }
    catch( RuntimeException e )
    {
      assertEquals( NullPointerException.class, e.getCause().getCause().getClass() );
      assertEquals( "tryfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
    }
  }

  public void testTryFinally_ExceptionTriggeredInTry_Before() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    try
    {
      Object val = invokeMethod( obj,  "testTryFinally_ExceptionTriggeredInTry_Before" );
      fail( "Should have thrown npe" );
    }
    catch( RuntimeException e )
    {
      assertEquals( NullPointerException.class, e.getCause().getCause().getClass() );
      assertEquals( "finally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
    }
  }

  public void testTryFinally_ReturnFromFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_ReturnFromFinally" );
    String ret = (String)val;
    assertEquals( "try", ret );
    assertEquals( "tryfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
  }

  public void testTryFinally_NotBreakFromTryFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_NotBreakFromTryFinally" );
    String ret = (String)val;
    assertEquals( "tryfinally", ret );
  }

  public void testTryFinally_BreakFromTryFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_BreakFromTryFinally" );
    String ret = (String)val;
    assertEquals( "finally", ret );
  }

  public void testTryFinally_NotContinueFromTryFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_NotContinueFromTryFinally" );
    String ret = (String)val;
    assertEquals( "tryfinally", ret );
  }

  public void testTryFinally_ContinueFromTryFinally() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_ContinueFromTryFinally" );
    String ret = (String)val;
    assertEquals( "finallyfinally", ret );
  }

  public void testTryFinally_InlinedFinallyNotInFinallyPartition() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    try
    {
      Object val = invokeMethod( obj,  "testTryFinally_InlinedFinallyNotInFinallyPartition" );
      fail( "Should have thrown npe" );
    }
    catch( RuntimeException e )
    {
      assertEquals( NullPointerException.class, e.getCause().getCause().getClass() );
      assertEquals( "tryfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
    }
  }

  public void testTryFinally_InlinedFinallyNotInFinallyPartitionWithReturn() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    try
    {
      Object val = invokeMethod( obj,  "testTryFinally_InlinedFinallyNotInFinallyPartitionWithReturn" );
      fail( "Should have thrown npe" );
    }
    catch( RuntimeException e )
    {
      assertEquals( NullPointerException.class, e.getCause().getCause().getClass() );
      assertEquals( "tryfinally", obj.getClass().getMethod( "getValue" ).invoke( null ) );
    }
  }

  public void testTryFinally_BreakFromTryFinallyInSwitch() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_BreakFromTryFinallyInSwitch" );
    String ret = (String)val;
    assertEquals( "finally", ret );
  }

  public void testTryFinally_FinallyAroundBlockIsInlinedCorrectly() throws Exception
  {
    Object obj = newTryCatchFinallyStatement();

    Object val = invokeMethod( obj,  "testTryFinally_FinallyAroundBlockIsNotExecutedInBlocksReturn" );
    String ret = (String)val;
    assertEquals( "finallyinBlock", ret );
  }

  private Object newTryCatchFinallyStatement() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.TryFinallyStatementTestClass";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}