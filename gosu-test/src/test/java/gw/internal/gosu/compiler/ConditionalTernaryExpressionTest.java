/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class ConditionalTernaryExpressionTest extends ByteCodeTestBase
{
  public void testStringTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "stringTernaryPos" );
    String ret = (String)val;
    assertEquals( "a", ret );
  }
  public void testStringTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "stringTernaryNeg" );
    String ret = (String)val;
    assertEquals( "b", ret );
  }
  
  public void testCharTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "charTernaryPos" );
    char ret = (Character)val;
    assertEquals( 'a', ret );
  }
  public void testCharTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "charTernaryNeg" );
    char ret = (Character)val;
    assertEquals( 'b', ret );
  }

  public void testByteTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "byteTernaryPos" );
    byte ret = (Byte)val;
    assertEquals( 1, ret );
  }
  public void testByteTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "byteTernaryNeg" );
    byte ret = (Byte)val;
    assertEquals( 2, ret );
  }

  public void testShortTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "shortTernaryPos" );
    short ret = (Short)val;
    assertEquals( 1, ret );
  }
  public void testShortTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "shortTernaryNeg" );
    short ret = (Short)val;
    assertEquals( 2, ret );
  }
  
  public void testIntTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "intTernaryPos" );
    int ret = (Integer)val;
    assertEquals( 1, ret );
  }
  public void testIntTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "intTernaryNeg" );
    int ret = (Integer)val;
    assertEquals( 2, ret );
  }
  
  public void testLongTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "longTernaryPos" );
    long ret = (Long)val;
    assertEquals( 1, ret );
  }
  public void testLongTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "longTernaryNeg" );
    long ret = (Long)val;
    assertEquals( 2, ret );
  }

  public void testFloatTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "floatTernaryPos" );
    float ret = (Float)val;
    assertEquals( 1.0f, ret );
  }
  public void testFloatTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "floatTernaryNeg" );
    float ret = (Float)val;
    assertEquals( 2.0f, ret );
  }

  public void testDoubleTernaryPos() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "doubleTernaryPos" );
    double ret = (Double)val;
    assertEquals( 1.0d, ret );
  }
  public void testDoubleTernaryNeg() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "doubleTernaryNeg" );
    double ret = (Double)val;
    assertEquals( 2.0d, ret );
  }

  public void testElvisDoesNotEvaluatingConditionTwice() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "testElvisDoesNotEvaluatingConditionTwice" );
    int ret = (Integer)val;
    assertEquals( 1, ret );
  }

  private Object newTestNewExpression() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestConditionaryTernaryExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}