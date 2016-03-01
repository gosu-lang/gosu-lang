/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class MapAssignmentStatementTest extends ByteCodeTestBase
{
  public void testStringMap() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMap" );
    int ret = (Integer)val;
    assertEquals( 9, ret );
  }

  public void testStringMapNotAbstractMap() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMapNotAbstractMap" );
    int ret = (Integer)val;
    assertEquals( 9, ret );
  }

  public void testStringMapShort() throws Exception
  {
    Object obj = newTestMapAccess();
    Object val = invokeMethod( obj,  "testStringMapShort" );
    int ret = (Integer)val;
    assertEquals( 9, ret );
  }

  public void testNullException() throws Exception
  {
    Object obj = newTestMapAccess();
    try
    {
      invokeMethod( obj,  "testNullException" );
      fail();
    }
    catch( RuntimeException ite )
    {
      assertTrue( ite.getCause().getCause() instanceof NullPointerException );
    }
  }

  private Object newTestMapAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.TestMapAssignmentStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}