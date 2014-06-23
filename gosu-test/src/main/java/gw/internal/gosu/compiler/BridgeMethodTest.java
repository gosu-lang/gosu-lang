/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;


public class BridgeMethodTest extends ByteCodeTestBase
{
  public void testNonBridgeOnJustArg() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNonBridgeOnJustArg" );
    assertEquals( 5, m.invoke( o ) );
  }

  public void testBridgeOnJustArg() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testBridgeOnJustArg" );
    assertEquals( 5, m.invoke( o ) );
  }

  public void testNonBridgeOnJustArgWithGenericMethod() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNonBridgeOnJustArgWithGenericMethod" );
    String res = (String)m.invoke( o );
    assertEquals( "11.2java.lang.Float", res );
  }

  public void testBridgeOnJustArgWithGenericMethod() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testBridgeOnJustArgWithGenericMethod" );
    String res = (String)m.invoke( o );
    assertEquals( "11.3java.lang.Float", res );
  }

  private Object newTestNumericLiteral() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestBridgeMethods_Generic";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}