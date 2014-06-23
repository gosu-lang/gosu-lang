/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.util.GosuTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class NewExpressionTest extends ByteCodeTestBase
{
  //## todo: Include test for non-bytecode type. Need to make a test type for this.

  public void testNewString() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newString" );
    String ret = (String)val;
    assertEquals( "hello", ret );
  }

  public void testNewStringArray() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newStringArray" );
    String[] ret = (String[])val;
    assertEquals( 1, ret.length );
  }

  public void testNewStringArrayInitialized() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newStringArrayInitialized" );
    String[] ret = (String[])val;
    assertEquals( 3, ret.length );
    assertEquals( "a", ret[0] );
    assertEquals( "b", ret[1] );
    assertEquals( "c", ret[2] );
  }

  public void testNewIntArrayInitialized() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newIntArrayInitialized" );
    int[] ret = (int[])val;
    assertEquals( 3, ret.length );
    assertEquals( 2, ret[0] );
    assertEquals( 3, ret[1] );
    assertEquals( 4, ret[2] );
  }

  public void testNewDoubleArray() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newDoubleArray" );
    double[] ret = (double[])val;
    assertEquals( 4, ret.length );
  }

  public void testNewDoubleArrayInitialized() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newDoubleArrayInitialized" );
    double[] ret = (double[])val;
    assertEquals( 1, ret.length );
    assertEquals( 42d, ret[0] );
  }

  public void testNewThreeDDoubleArrayWithTwoSizes() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newThreeDDoubleArrayWithTwoSizes" );
    double[][][] ret = (double[][][])val;
    assertEquals( 5, ret.length );
    assertEquals( 6, ret[0].length );
  }

  public void testNewStaticInner() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newStaticInner" );
    Object inner = val;
    assertTrue( inner.getClass().getName().endsWith( "StaticInner" ) );
    assertEquals( obj.getClass(), inner.getClass().getEnclosingClass() );
  }

  public void testNewNonStaticInner() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newNonStaticInner" );
    Object inner = val;
    assertTrue( inner.getClass().getName().endsWith( "NonStaticInner" ) );
    assertEquals( obj.getClass(), inner.getClass().getEnclosingClass() );
  }

  public void testNewAnonymousInner() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newAnonymousInner" );
    Object staticInner = val;
    assertTrue( staticInner.getClass().getName().contains( "AnonymouS" ) );
    assertEquals( obj.getClass(), staticInner.getClass().getEnclosingClass() );
  }

  public void testNewListWithInitializer() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newListWithInitializer" );
    ArrayList list = (ArrayList)val;
    assertListEquals( Arrays.asList( "a", "b", "c" ), list );
  }
  
  public void testNewSetWithInitializer() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newSetWithInitializer" );
    HashSet set = (HashSet)val;
    assertEquals( 3, set.size() );
    assertTrue( set.contains( "a" ) );
    assertTrue( set.contains( "b" ) );
    assertTrue( set.contains( "c" ) );
  }
  
  public void testNewMapWithInitializer() throws Exception
  {
    Object obj = newTestNewExpression();
    Object val = invokeMethod( obj,  "newMapWithInitializer" );
    HashMap map = (HashMap)val;
    assertEquals( 2, map.size() );
    assertEquals( "AA", map.get( "a" ) );
    assertEquals( "BB", map.get( "b" ) );
  }

  public void testInterfacesCannotBeConstructedWithOrWithoutArgs()
  {
    assertNotNull( GosuTestUtil.getParseResultsException( "new java.lang.Runnable() " ) );
    assertNotNull( GosuTestUtil.getParseResultsException( "new java.lang.Runnable({}) " ) );
  }

  private Object newTestNewExpression() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestNewExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}