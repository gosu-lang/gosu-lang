/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class ArrayAssignmentStatementTest extends ByteCodeTestBase
{
  public void testStringArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "stringArray" );
    String ret = (String)val;
    assertEquals( "x", ret );
  }

  public void testBooleanArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "booleanArray" );
    boolean ret = (Boolean)val;
    assertEquals( true, ret );
  }

  public void testByteArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "byteArray" );
    byte ret = (Byte)val;
    assertEquals( 8, ret );
  }

  public void testShortArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "shortArray" );
    short ret = (Short)val;
    assertEquals( 8, ret );
  }

  public void testIntArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intArray" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testLongArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "longArray" );
    long ret = (Long)val;
    assertEquals( 8L, ret );
  }

  public void testDoubleArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "doubleArray" );
    double ret = (Double)val;
    assertEquals( 8D, ret );
  }

  public void testCharArray() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "charArray" );
    char ret = (Character)val;
    assertEquals( 'x', ret );
  }

  public void testListAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "listAccess" );
    String ret = (String)val;
    assertEquals( "x", ret );
  }

//  public void testCharSequence() throws Exception
//  {
//    Object obj = newTestArrayAccess();
//    Object val = invokeMethod( obj,  "charSequence" );
//    char ret = (Character)val;
//    assertEquals( 'x', ret );
//  }

  public void testNullRoot() throws Exception
  {
    Object obj = newTestArrayAccess();
    try
    {
      invokeMethod( obj,  "nullRoot" );
    }
    catch( RuntimeException ite )
    {
      assertEquals( NullPointerException.class, ite.getCause().getCause().getClass() );
      return;
    }
    fail();
  }

  private Object newTestArrayAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.TestArrayAssignmentStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}