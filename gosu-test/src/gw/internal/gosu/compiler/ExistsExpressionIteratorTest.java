/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class ExistsExpressionIteratorTest extends ByteCodeTestBase
{
  public void testHasExistsStringNoIndex() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsStringNoIndex" );
    assertEquals( true, ret );
  }

  public void testHasExistsString() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsString" );
    assertEquals( true, ret );
  }

  public void testHasExistsInt() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsInt" );
    assertEquals( true, ret );
  }

  public void testHasExistsLong() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsLong" );
    assertEquals( true, ret );
  }

  public void testHasExistsNeverEnterBody() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsNeverEnterBody" );
    assertEquals( false, ret );
  }

  public void testHasExistsTerminal() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsTerminal" );
    assertEquals( false, ret );
  }

  public void testHasExistsNull() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsNull" );
    assertEquals( false, ret );
  }

  public void testHasExistsIterator() throws Exception
  {
    Object obj = newExistsClass_array();

    boolean ret = (Boolean)invokeMethod( obj,  "hasExistsIterator" );
    assertEquals( true, ret );
  }

  private Object newExistsClass_array() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.HasExistsExpression_Iterator";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}