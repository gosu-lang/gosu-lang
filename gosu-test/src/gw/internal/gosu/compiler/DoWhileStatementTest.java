/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class DoWhileStatementTest extends ByteCodeTestBase
{
  public void testHasDoWhile() throws Exception
  {
    Object obj = newDoWhileClass();

    Object val = invokeMethod( obj,  "hasDoWhile" );
    String ret = (String)val;
    assertEquals( "lo", ret );
  }

  public void testHasDoWhileTerminal() throws Exception
  {
    Object obj = newDoWhileClass();

    Object val = invokeMethod( obj,  "hasDoWhileTerminal" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasDoWhileWithThrow() throws Exception
  {
    Object obj = newDoWhileClass();

    try
    {
      Object val = invokeMethod( obj,  "hasDoWhileWithThrow" );
    }
    catch( RuntimeException e )
    {
      assertEquals( "yay", e.getCause().getCause().getMessage() );
      return;
    }
    fail();
  }

  public void testHasDoWhileWithNonBooleanCondition() throws Exception
  {
    Object obj = newDoWhileClass();

    Object val = invokeMethod( obj,  "hasDoWhileWithNonBooleanCondition" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testHasDoWhileWithWideNonBooleanCondition() throws Exception
  {
    Object obj = newDoWhileClass();

    Object val = invokeMethod( obj,  "hasDoWhileWithWideNonBooleanCondition" );
    long ret = (Long)val;
    assertEquals( 8, ret );
  }

  public void testHasDoWhileWithBoxBooleanCondition() throws Exception
  {
    Object obj = newDoWhileClass();

    Object val = invokeMethod( obj,  "hasDoWhileWithBoxBooleanCondition" );
    String ret = (String)val;
    assertEquals( "lo", ret );
  }

  private Object newDoWhileClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasDoWhileStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}