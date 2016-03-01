/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 */
public class NumericLiteralTest extends ByteCodeTestBase
{
  public void testTypeWithContextPbyte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPbyte" );
    Byte n = (Byte)m.invoke( o );
    assertEquals( new Byte( (byte)8 ), n );
  }
  public void testTypeWithContextByte() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextByte" );
    Byte n = (Byte)m.invoke( o );
    assertEquals( new Byte( (byte)8 ), n );
  }

  public void testTypeWithContextPshort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPshort" );
    Short n = (Short)m.invoke( o );
    assertEquals( new Short( (short)8 ), n );
  }
  public void testTypeWithContextShort() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextShort" );
    Short n = (Short)m.invoke( o );
    assertEquals( new Short( (short)8 ), n );
  }

  public void testTypeWithContextPint() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPint" );
    Integer n = (Integer)m.invoke( o );
    assertEquals( new Integer( 8 ), n );
  }
  public void testTypeWithContextInteger() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextInt" );
    Integer n = (Integer)m.invoke( o );
    assertEquals( new Integer( 8 ), n );
  }

  public void testTypeWithContextPlong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPlong" );
    Long n = (Long)m.invoke( o );
    assertEquals( new Long( (long)8 ), n );
  }
  public void testTypeWithContextLong() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextLong" );
    Long n = (Long)m.invoke( o );
    assertEquals( new Long( (long)8 ), n );
  }

  public void testTypeWithContextPfloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPfloat" );
    Float n = (Float)m.invoke( o );
    assertEquals( new Float( (float)8 ), n );
  }
  public void testTypeWithContextFloat() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextFloat" );
    Float n = (Float)m.invoke( o );
    assertEquals( new Float( (float)8 ), n );
  }

  public void testTypeWithContextPdouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextPdouble" );
    Double n = (Double)m.invoke( o );
    assertEquals( new Double( (double)8 ), n );
  }
  public void testTypeWithContextDouble() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextDouble" );
    Double n = (Double)m.invoke( o );
    assertEquals( new Double( (double)8 ), n );
  }

  public void testTypeWithContextBigInteger() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextBigInteger" );
    BigInteger n = (BigInteger)m.invoke( o );
    assertEquals( new BigInteger( "8" ), n );
  }

  public void testTypeWithContextBigDecimal() throws Exception
  {
    Object o = newTestNumericLiteral();
    Method m = o.getClass().getMethod( "testTypeWithContextBigDecimal" );
    BigDecimal n = (BigDecimal)m.invoke( o );
    assertEquals( new BigDecimal( "8.500" ), n );
  }

  private Object newTestNumericLiteral() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestNumericLiteral";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}