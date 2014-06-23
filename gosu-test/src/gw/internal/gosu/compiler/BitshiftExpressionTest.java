/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class BitshiftExpressionTest extends ByteCodeTestBase
{
  public void testShiftRightInt() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftRightInt" );
    int ret = (Integer)val;
    System.out.println( 1024 >> 2 );
    assertEquals( 1024 >> 2, ret );
  }

  public void testShiftLeftInt() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftLeftInt" );
    int ret = (Integer)val;
    assertEquals( 1024 << 2, ret );
  }

  public void testShiftURightInt() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftURightInt" );
    int ret = (Integer)val;
    assertEquals( 1024 >>> 2, ret );
  }

  public void testShiftRightIntNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftRightIntNeg" );
    int ret = (Integer)val;
    assertEquals( -1024 >> 2, ret );
  }

  public void testShiftLeftIntNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftLeftIntNeg" );
    int ret = (Integer)val;
    assertEquals( -1024 << 2, ret );
  }

  public void testShiftURightIntNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftURightIntNeg" );
    int ret = (Integer)val;
    assertEquals( -1024 >>> 2, ret );
  }

  public void testShiftRightLong() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftRightLong" );
    long ret = (Long)val;
    long x = (long)Integer.MAX_VALUE + (long)2;
    assertEquals( x >> 2, ret );
  }

  public void testShiftLeftLong() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftLeftLong" );
    long ret = (Long)val;
    System.out.println( ((long)Integer.MAX_VALUE + 2) << 2 );
    assertEquals( ((long)Integer.MAX_VALUE + 2) << 2, ret );
  }

  public void testShiftURightLong() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftURightLong" );
    long ret = (Long)val;
    System.out.println( ((long)Integer.MAX_VALUE + 2) >>> 2 );
    assertEquals( ((long)Integer.MAX_VALUE + 2) >>> 2, ret );
  }

  public void testShiftRightLongNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftRightLongNeg" );
    long ret = (Long)val;
    System.out.println( -((long)Integer.MAX_VALUE + 2) >> 2 );
    assertEquals( -((long)Integer.MAX_VALUE + 2) >> 2, ret );
  }

  public void testShiftLeftLongNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftLeftLongNeg" );
    long ret = (Long)val;
    System.out.println( -((long)Integer.MAX_VALUE + 2) << 2 );
    assertEquals( -((long)Integer.MAX_VALUE + 2) << 2, ret );
  }

  public void testShiftURightLongNeg() throws Exception
  {
    Object obj = newTestBitshiftExpression();
    Object val = invokeMethod( obj,  "testShiftURightLongNeg" );
    long ret = (Long)val;
    System.out.println( -((long)Integer.MAX_VALUE + 2) >>> 2 );
    assertEquals( -((long)Integer.MAX_VALUE + 2) >>> 2, ret );
  }
  
  private Object newTestBitshiftExpression() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestBitshiftExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}