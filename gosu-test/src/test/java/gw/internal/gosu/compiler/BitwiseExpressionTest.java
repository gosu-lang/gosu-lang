/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class BitwiseExpressionTest extends ByteCodeTestBase
{
  public void testOrInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testOrInt" );
    int ret = (Integer)val;
    System.out.println( 16 | 17 );
    assertEquals( 16 | 17, ret );
  }

  public void testXorInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testXorInt" );
    int ret = (Integer)val;
    System.out.println( 16 ^ 17 );
    assertEquals( 16 ^ 17, ret );
  }

  public void testAndInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testAndInt" );
    int ret = (Integer)val;
    System.out.println( 16 & 17 );
    assertEquals( 16 & 17, ret );
  }

  
  public void testOrIntLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testOrIntLong" );
    int ret = (Integer)val;
    System.out.println( (int)16 | (long)17 );
    assertEquals( (int)16 | (long)17, ret );
  }

  public void testXorIntLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testXorIntLong" );
    int ret = (Integer)val;
    System.out.println( (int)16 ^ (long)17 );
    assertEquals( (int)16 ^ (long)17, ret );
  }

  public void testAndIntLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testAndIntLong" );
    int ret = (Integer)val;
    System.out.println( (int)16 & (long)17 );
    assertEquals( (int)16 & (long)17, ret );
  }
  
  
  public void testOrLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testOrLong" );
    long ret = (Long)val;
    System.out.println( (long)16 | (long)17 );
    assertEquals( (long)16 | (long)17, ret );
  }

  public void testXorLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testXorLong" );
    long ret = (Long)val;
    System.out.println( (long)16 ^ (long)17 );
    assertEquals( (long)16 ^ (long)17, ret );
  }

  public void testAndLong() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testAndLong" );
    long ret = (Long)val;
    System.out.println( (long)16 & (long)17 );
    assertEquals( (long)16 & (long)17, ret );
  }

  
  public void testOrLongInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testOrLongInt" );
    long ret = (Long)val;
    System.out.println( (long)16 | (int)17 );
    assertEquals( (long)16 | (int)17, ret );
  }

  public void testXorLongInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testXorLongInt" );
    long ret = (Long)val;
    System.out.println( (long)16 ^ (int)17 );
    assertEquals( (long)16 ^ (int)17, ret );
  }

  public void testAndLongInt() throws Exception
  {
    Object obj = newTestBitwiseExpression();
    Object val = invokeMethod( obj,  "testAndLongInt" );
    long ret = (Long)val;
    System.out.println( (long)16 & (int)17 );
    assertEquals( (long)16 & (int)17, ret );
  }
  
  private Object newTestBitwiseExpression() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestBitwiseExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}