/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class MapAccessTest extends ByteCodeTestBase
{
  public void testStringMap() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMap" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testStringMapNotAbstractMap() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMapNotAbstractMap" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testStringMapShort() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMapShort" );
    int ret = (Integer)val;
    assertEquals( 8, ret );
  }

  public void testShortCircuitNullMap() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testNullShortcircuit" );
    Object ret = val;
    assertNull( ret );
  }

  private Object newTestMapAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestMapAccess";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}