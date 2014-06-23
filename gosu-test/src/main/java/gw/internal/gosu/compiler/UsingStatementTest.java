/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class UsingStatementTest extends ByteCodeTestBase
{
  public void testUsingStatementBlockExecutes() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testUsingStatementBlockExecutes", 1 );
  }

  public void testDisposableExpression() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testDisposableExpression", 3 );
  }

  public void testDisposableVar() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testDisposableVar", 4 );
  }

  public void testMultipleDisposableVar() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testMultipleDisposableVar", 6 );
  }

  public void testDisposableVarReassigned() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testDisposableVarReassigned", 3 );
  }

  public void testCloseableVar() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testCloseableVar", 2 );
  }

  public void testMultipleCloseableVar() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testMultipleCloseableVar", 4 );
  }

  public void testLock() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testLock", 4 );
  }

  public void testLockWithFinally() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testLockWithFinally", 7 );
  }

  public void testLockWithFinallyAndException0() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testLockWithFinallyAndException0", 8 );
  }

  public void testLockWithFinallyAndException1() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testLockWithFinallyAndException1", 8 );
  }

  public void testReturnInsideUsingStatement() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testReturnInsideUsingStatement", 2 );
  }

  public void testWorksWithDisposeFunction() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testWorksWithDisposeFunction", 4 );
  }

  public void testWorksWithInnerDisposeFunction() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testWorksWithInnerDisposeFunction", 4 );
  }

  public void testWorksWithCloseFunction() throws Exception
  {
    invokeTestMethodAndVerifyAssertions(  "testWorksWithCloseFunction", 4 );
  }

  public void testReentrant() throws Exception
  {
    invokeTestMethodAndVerifyAssertions( "testReentrant", 5 );
  }

  private void invokeTestMethodAndVerifyAssertions( String strMethod, int iAssertions ) throws Exception
  {
    Object obj = newHasUsingStatement();
    invokeMethod( obj, strMethod );
    Object val = invokeMethod( obj, "getAssertionCount" );
    int iActualAssertions = (Integer)val;
    assertEquals( iActualAssertions, iAssertions );
  }

  private Object newHasUsingStatement() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasUsingStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}