/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.awt.*;
import java.util.ArrayList;

/**
 */
public class BeanMethodCallExpansionTest extends ByteCodeTestBase
{
  public void testArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testArray" );
    Point[] ret = (Point[])val;
    assertEquals( 3, ret.length );
    assertEquals( new Point( 1, 2 ), ret[0] );
    assertEquals( new Point( 3, 4 ), ret[1] );
    assertEquals( new Point( 5, 6 ), ret[2] );
  }

  public void testStringArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testStringArray" );
    String[] ret = (String[])val;
    assertEquals( 3, ret.length );
    assertEquals( "abc", ret[0] );
    assertEquals( "def", ret[1] );
    assertEquals( "ghi", ret[2] );
  }

  public void testByteArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testByteArray" );
    byte[] ret = (byte[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
    assertEquals( 2, ret[1] );
    assertEquals( 3, ret[2] );
  }

  public void testBooleanArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testBooleanArray" );
    boolean[] ret = (boolean[])val;
    assertEquals( 3, ret.length );
    assertEquals( true, ret[0] );
    assertEquals( false, ret[1] );
    assertEquals( true, ret[2] );
  }

  public void testCharArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testCharArray" );
    char[] ret = (char[])val;
    assertEquals( 3, ret.length );
    assertEquals( 'a', ret[0] );
    assertEquals( 'b', ret[1] );
    assertEquals( 'c', ret[2] );
  }

  public void testShortArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testShortArray" );
    short[] ret = (short[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
    assertEquals( 2, ret[1] );
    assertEquals( 3, ret[2] );
  }

  public void testIntArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testIntArray" );
    int[] ret = (int[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
    assertEquals( 2, ret[1] );
    assertEquals( 3, ret[2] );
  }

  public void testLongArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testLongArray" );
    long[] ret = (long[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
    assertEquals( 2, ret[1] );
    assertEquals( 3, ret[2] );
  }

  public void testFloatArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testFloatArray" );
    float[] ret = (float[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1f, ret[0] );
    assertEquals( 2f, ret[1] );
    assertEquals( 3f, ret[2] );
  }

  public void testDoubleArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testDoubleArray" );
    double[] ret = (double[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1d, ret[0] );
    assertEquals( 2d, ret[1] );
    assertEquals( 3d, ret[2] );
  }

  public void testVoid() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "testVoid" );
    ArrayList<Double> ret = (ArrayList<Double>)val;
    assertEquals( 3, ret.size() );
    assertEquals( 1d, ret.get( 0 ) );
    assertEquals( 2d, ret.get( 1 ) );
    assertEquals( 3d, ret.get( 2 ) );
  }

  private Object newTestArrayAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestBeanMethodCallExpansion";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}