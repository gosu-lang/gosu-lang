/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;

/**
 */
public class PropertyIdentifierTest extends ByteCodeTestBase
{
  public void testInstanceVarProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM2 = obj.getClass().getMethod( "getM2" );
    Object ret = getM2.invoke( obj );
    assertNull( ret );

    Method setM2 = obj.getClass().getMethod( "setM2", String.class );
    setM2.invoke( obj, "goober" );

    ret = getM2.invoke( obj );
    assertSame( "goober", ret );
  }

  public void testStaticVarProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM3 = obj.getClass().getMethod( "getM3" );
    Object ret = getM3.invoke( null );
    assertNull( ret );

    Method setM3 = obj.getClass().getMethod( "setM3", String.class );
    setM3.invoke( null, "goober" );

    ret = getM3.invoke( null );
    assertSame( "goober", ret );
  }

  public void testReadOnlyInstanceProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM4 = obj.getClass().getMethod( "getM4" );
    Object ret = getM4.invoke( obj );
    assertEquals( (Integer) 4, (Integer)ret );
  }

  public void testInstanceProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM5 = obj.getClass().getMethod( "getM5" );
    Object ret = getM5.invoke( obj );
    assertZero( (Integer)ret );

    Method setM5 = obj.getClass().getMethod( "setM5", int.class );
    setM5.invoke( obj, 888 );

    ret = getM5.invoke( obj );
    assertEquals( (Integer) 888, (Integer)ret );
  }

  public void testStaticProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM6 = obj.getClass().getMethod( "getM6" );
    Object ret = getM6.invoke( null );
    assertZero( (Integer)ret );

    Method setM6 = obj.getClass().getMethod( "setM6", int.class );
    setM6.invoke( null, 888 );

    ret = getM6.invoke( null );
    assertEquals( (Integer) 888, (Integer)ret );
  }

  public void testAccessInstanceProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM5 = obj.getClass().getMethod( "accessM5" );
    Object ret = getM5.invoke( obj );
    assertZero( (Integer)ret );

    Method setM5 = obj.getClass().getMethod( "changeM5", int.class );
    setM5.invoke( obj, 888 );

    ret = getM5.invoke( obj );
    assertEquals( (Integer) 888, (Integer)ret );
  }

  public void testAccessStaticProperty() throws Exception
  {
    Object obj = newPropertyIdentifierClass();

    Method getM5 = obj.getClass().getMethod( "accessM5" );
    Object ret = getM5.invoke( obj );
    assertZero( (Integer)ret );

    Method setM5 = obj.getClass().getMethod( "changeM5", int.class );
    setM5.invoke( obj, 888 );

    ret = getM5.invoke( obj );
    assertEquals( (Integer) 888, (Integer)ret );
  }

  private Object newPropertyIdentifierClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String classPropertyIdentifier = "gw.internal.gosu.compiler.sample.expression.PropertyIdentifier";
    Class<?> javaClass = GosuClassLoader.instance().findClass( classPropertyIdentifier );
    assertNotNull( javaClass );
    assertEquals( classPropertyIdentifier, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}