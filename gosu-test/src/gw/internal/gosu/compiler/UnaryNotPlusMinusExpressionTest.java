/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;

public class UnaryNotPlusMinusExpressionTest extends ByteCodeTestBase
{
  public void testNotPbyte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPbyte" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotByte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotByte" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  public void testNotPshort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPshort" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotShort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotShort" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  public void testNotPint() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPint" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotInteger() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotInt" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  public void testNotPlong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPlong" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotLong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotLong" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  public void testNotPfloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPfloat" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotFloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotFloat" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  public void testNotPdouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotPdouble" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }
  public void testNotDouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNotDouble" );
    Boolean n = (Boolean)m.invoke( o );
    assertFalse( n );
  }

  private Object newTestNumericLiteral() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestUnaryNotPlusMinusExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}