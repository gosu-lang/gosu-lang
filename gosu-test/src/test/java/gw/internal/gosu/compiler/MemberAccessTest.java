/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class MemberAccessTest extends ByteCodeTestBase
{
  public void testBooleanFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "booleanFieldAccess" );
    boolean ret = (Boolean)val;
    assertEquals( true, ret );
  }

  public void testByteFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "byteFieldAccess" );
    byte ret = (Byte)val;
    assertEquals( 3, ret );
  }

  public void testShortFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "shortFieldAccess" );
    short ret = (Short)val;
    assertEquals( 4, ret );
  }

  public void testIntFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intFieldAccess" );
    int ret = (Integer)val;
    assertEquals( 5, ret );
  }

  public void testLongFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "longFieldAccess" );
    long ret = (Long)val;
    assertEquals( 6L, ret );
  }

  public void testFloatFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "floatFieldAccess" );
    float ret = (Float)val;
    assertEquals( 7F, ret );
  }

  public void testDoubleFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "doubleFieldAccess" );
    double ret = (Double)val;
    assertEquals( 8D, ret );
  }

  public void testCharFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "charFieldAccess" );
    char ret = (Character)val;
    assertEquals( '9', ret );
  }

  public void testStringFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "stringFieldAccess" );
    String ret = (String)val;
    assertEquals( "10", ret );
  }

  public void testIntarrayFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intArrayFieldAccess" );
    int[] ret = (int[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
  }

  public void testShortCircuitFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "shortCircuitFieldAccess" );
    String ret = (String)val;
    assertNull( ret );
  }

  public void testNpePrimitiveFieldAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    try
    {
      Object val = invokeMethod( obj,  "npePrimitiveFieldAccess" );
    }
    catch( RuntimeException npe )
    {
      if( npe.getCause().getCause() instanceof NullPointerException )
      {
        // expected
        return;
      }
    }
    fail();
  }

  public void testShortCircuitPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "shortCircuitPropertyAccess" );
    String ret = (String)val;
    assertNull( ret );
  }

  public void testAssociativeArrayAccessStatic() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "associativeArrayAccesStatic" );
    int ret = (Integer)val;
    assertEquals( Integer.MIN_VALUE, ret );
  }

  public void testAssociativeArrayAccesStaticPolymorphism() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "associativeArrayAccesStaticPolymorphism" );
    int ret = (Integer)val;
    assertEquals( Integer.MIN_VALUE, ret );
  }

  public void testAssociativeArrayAccessNonStatic() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "associativeArrayAccesNonStatic" );
    int ret = (Integer)val;
    assertEquals( 4, ret );
  }

  public void testAssociativeArrayAccesNonStaticPolymorphism() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "associativeArrayAccesNonStaticPolymorphism" );
    int ret = (Integer)val;
    assertEquals( 4, ret );
  }

  public void testNpePrimitivePropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    try
    {
      Object val = invokeMethod( obj,  "npePrimitivePropertyAccess" );
    }
    catch( RuntimeException npe )
    {
      if( npe.getCause().getCause() instanceof NullPointerException )
      {
        // expected
        return;
      }
    }
    fail();
  }

  public void testBooleanPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "booleanPropertyAccess" );
    boolean ret = (Boolean)val;
    assertEquals( true, ret );
  }

  public void testBytePropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "bytePropertyAccess" );
    byte ret = (Byte)val;
    assertEquals( 3, ret );
  }

  public void testShortPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "shortPropertyAccess" );
    short ret = (Short)val;
    assertEquals( 4, ret );
  }

  public void testIntPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intPropertyAccess" );
    int ret = (Integer)val;
    assertEquals( 5, ret );
  }

  public void testLongPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "longPropertyAccess" );
    long ret = (Long)val;
    assertEquals( 6L, ret );
  }

  public void testFloatPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "floatPropertyAccess" );
    float ret = (Float)val;
    assertEquals( 7F, ret );
  }

  public void testDoublePropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "doublePropertyAccess" );
    double ret = (Double)val;
    assertEquals( 8D, ret );
  }

  public void testCharPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "charPropertyAccess" );
    char ret = (Character)val;
    assertEquals( '9', ret );
  }

  public void testStringPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "stringPropertyAccess" );
    String ret = (String)val;
    assertEquals( "10", ret );
  }

  public void testIntarrayPropertyAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intArrayPropertyAccess" );
    int[] ret = (int[])val;
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
  }

  public void testIntarrayPropertyLengthAccess() throws Exception
  {
    Object obj = newTestArrayAccess();
    Object val = invokeMethod( obj,  "intArrayPropertyLengthAccess" );
    int ret = (Integer)val;
    assertEquals( 3, ret );
  }

  Object newTestArrayAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.expression.TestMemberAccess" );
  }
}