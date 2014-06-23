/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class ForeachStatementArrayTest extends ByteCodeTestBase
{
  public void testHasForeachStringNoIndex() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachStringNoIndex" );
    String ret = (String)val;
    assertEquals( "abc", ret );
  }

  public void testHasForeachString() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachString" );
    String ret = (String)val;
    assertEquals( "a0b1c2", ret );
  }

  public void testHasForeachInt() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachInt" );
    String ret = (String)val;
    assertEquals( "506172", ret );
  }

  public void testHasForeachLong() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachLong" );
    String ret = (String)val;
    assertEquals( "506172", ret );
  }

  public void testHasForeachBreak() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachBreak" );
    String ret = (String)val;
    assertEquals( "01", ret );
  }

  public void testHasForeachContinue() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachContinue" );
    String ret = (String)val;
    assertEquals( "02", ret );
  }

  public void testHasForeachNeverEnterBody() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachNeverEnterBody" );
    String ret = (String)val;
    assertEquals( "", ret );
  }

  public void testHasForeachTerminal() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachTerminal" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasForeachWithThrow() throws Exception
  {
    Object obj = newForeachClass_array();

    try
    {
      Object val = invokeMethod( obj,  "hasForeachWithThrow" );
    }
    catch( RuntimeException ite )
    {
      assertEquals( "yay", ite.getCause().getCause().getMessage() );
      return;
    }
    fail();
  }

  public void testHasForeachNull() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachNull" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasForeachWithEval() throws Exception
  {
    Object obj = newForeachClass_array();

    Object val = invokeMethod( obj,  "hasForeachWithEval" );
    String ret = (String)val;
    assertEquals( "a0b1c2", ret );
  }

  private Object newForeachClass_array() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasForeachStatement_Array";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}