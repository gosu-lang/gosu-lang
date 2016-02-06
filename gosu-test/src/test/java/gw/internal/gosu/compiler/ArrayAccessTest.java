/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class ArrayAccessTest extends ByteCodeTestBase
{
  public void testStringArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    String ret = (String) invokeMethod(  obj,  "stringArray" );
    assertEquals( "b", ret );
  }

  public void testListAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    String ret = (String)invokeMethod(  obj,  "listAccess" );
    assertEquals( "b", ret );
  }

  public void testShortCircuitNullArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    String ret = (String)invokeMethod(  obj,  "shortCircuitNullArray" );
    assertNull( ret );
  }

  public void testBooleanArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    boolean ret = (Boolean)invokeMethod(  obj,  "booleanArray" );
    assertEquals( true, ret );
  }

  public void testByteArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    byte ret = (Byte)invokeMethod(  obj,  "byteArray" );
    assertEquals( 2, ret );
  }

  public void testShortArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    short ret = (Short)invokeMethod(  obj,  "shortArray" );
    assertEquals( 2, ret );
  }

  public void testIntArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    int ret = (Integer)invokeMethod(  obj,  "intArray" );
    assertEquals( 2, ret );
  }

  public void testLongArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    long ret = (Long)invokeMethod(  obj,  "longArray" );
    assertEquals( 2L, ret );
  }

  public void testDoubleArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    double ret = (Double)invokeMethod(  obj,  "doubleArray" );
    assertEquals( 2D, ret );
  }

  public void testCharArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    char ret = (Character)invokeMethod(  obj,  "charArray" );
    assertEquals( 'b', ret );
  }

  public void testCharSequence() throws Exception
  {
    Object obj = newTestArrayAccess();
    char ret = (Character)invokeMethod(  obj,  "charSequence" );
    assertEquals( 'e', ret );
  }

  Object newTestArrayAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.expression.TestArrayAccess" );
  }
}