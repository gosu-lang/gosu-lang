/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.testharness.Disabled;

/**
 */
public class WhileStatementTest extends ByteCodeTestBase
{
  public void testHasWhile() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "hasWhile" );
    String ret = (String)val;
    assertEquals( "lo", ret );
  }

  public void testHasWhileTerminal() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "hasWhileTerminal" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  @Disabled(assignee = "cgross", reason = "this currently does not compile, although it should")
  public void testIncrementsInTerminal() throws Exception
  {
//    Object obj = newWhileClass();
//    Object val = invokeMethod( obj, "incrementsInTerminal" );
//    String ret = (String)val;
//    assertEquals( "pass", ret );
  }

  public void testHasWhileWithThrow() throws Exception
  {
    Object obj = newWhileClass();

    try
    {
      Object val = invokeMethod( obj,  "hasWhileWithThrow" );
    }
    catch( RuntimeException e )
    {
      assertEquals( "yay", e.getCause().getCause().getMessage() );
      return;
    }
    fail();
  }

  public void testHasWhileWithNonBooleanCondition() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "hasWhileWithNonBooleanCondition" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testHasWhileNeverEnterBody() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "hasWhileNeverEnterBody" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasWhileWithWideNonBooleanCondition() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "hasWhileWithWideNonBooleanCondition" );
    long ret = (Long)val;
    assertEquals( 8, ret );
  }

  public void testWhileWithBoxBooleanCondition() throws Exception
  {
    Object obj = newWhileClass();

    Object val = invokeMethod( obj,  "whileWithBoxBooleanCondition" );
    String ret = (String)val;
    assertEquals( "lo", ret );
  }

  private Object newWhileClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasWhileStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}