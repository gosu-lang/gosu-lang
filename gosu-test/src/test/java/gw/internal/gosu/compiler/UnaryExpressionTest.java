/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.IDimension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 */
public class UnaryExpressionTest extends ByteCodeTestBase
{
  public void testTypeWithContextPbyte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePbyte" );
    Byte n = (Byte)m.invoke( o );
    assertEquals( new Byte( (byte)-8 ), n );
  }
  public void testNegateByte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateByte" );
    Byte n = (Byte)m.invoke( o );
    assertEquals( new Byte( (byte)-8 ), n );
  }

  public void testNegatePshort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePshort" );
    Short n = (Short)m.invoke( o );
    assertEquals( new Short( (short)-8 ), n );
  }
  public void testNegateShort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateShort" );
    Short n = (Short)m.invoke( o );
    assertEquals( new Short( (short)-8 ), n );
  }

  public void testNegatePint() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePint" );
    Integer n = (Integer)m.invoke( o );
    assertEquals( new Integer( -8 ), n );
  }
  public void testNegateInteger() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateInt" );
    Integer n = (Integer)m.invoke( o );
    assertEquals( new Integer( -8 ), n );
  }

  public void testNegatePlong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePlong" );
    Long n = (Long)m.invoke( o );
    assertEquals( new Long( (long)-8 ), n );
  }
  public void testNegateLong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateLong" );
    Long n = (Long)m.invoke( o );
    assertEquals( new Long( (long)-8 ), n );
  }

  public void testNegatePfloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePfloat" );
    Float n = (Float)m.invoke( o );
    assertEquals( new Float( (float)-8 ), n );
  }
  public void testNegateFloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateFloat" );
    Float n = (Float)m.invoke( o );
    assertEquals( new Float( (float)-8 ), n );
  }

  public void testNegatePdouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegatePdouble" );
    Double n = (Double)m.invoke( o );
    assertEquals( new Double( (double)-8 ), n );
  }
  public void testNegateDouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateDouble" );
    Double n = (Double)m.invoke( o );
    assertEquals( new Double( (double)-8 ), n );
  }

  public void testNegateBigInteger() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateBigInteger" );
    BigInteger n = (BigInteger)m.invoke( o );
    assertEquals( new BigInteger( "-8" ), n );
  }

  public void testNegateBigDecimal() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateBigDecimal" );
    BigDecimal n = (BigDecimal)m.invoke( o );
    assertEquals( new BigDecimal( "-8.500" ), n );
  }

  public void testNegateDimension() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testNegateDimension" );
    IDimension n = (IDimension)m.invoke( o );
    assertEquals( n.toNumber(), -9 );
  }

  private Object newTestNumericLiteral() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestUnaryExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}