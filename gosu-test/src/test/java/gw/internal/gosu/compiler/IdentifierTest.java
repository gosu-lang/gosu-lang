/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;

/**
 */
public class IdentifierTest extends ByteCodeTestBase
{
  public void testThis() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testThis" );
    Object ret = getM2.invoke( obj );
    assertSame( obj, ret );
  }

  public void testLocalVar() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testLocalVar" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Local", ret );
  }

  public void testParamVar() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testParamVar", String.class );
    Object ret = getM2.invoke( obj, "Param" );
    assertEquals( "Param", ret );
  }

  public void testInstanceVar() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testInstanceVar" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Instance", ret );
  }

  public void testStaticVar() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testStaticVar" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Static", ret );
  }

  public void testInstanceProperty() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testInstanceProperty" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Instance", ret );
  }

  public void testStaticProperty() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testStaticProperty" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Static", ret );
  }

  public void testOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testOuter" );
    Object ret = getM2.invoke( obj );
    assertSame( obj, ret );
  }

  public void testInstanceVarFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testInstanceVarFromOuter" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Instance", ret );
  }

  public void testInstancePropertyFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testInstancePropertyFromOuter" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Instance", ret );
  }

  public void testStaticVarFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testStaticVarFromOuter" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Static", ret );
  }

  public void testStaticPropertyFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();

    Method getM2 = obj.getClass().getMethod( "testStaticPropertyFromOuter" );
    Object ret = getM2.invoke( obj );
    assertEquals( "Static", ret );
  }

  private Object newIdentifierTestClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String classPropertyIdentifier = "gw.internal.gosu.compiler.sample.statement.classes.IdentifierTestClass";
    Class<?> javaClass = GosuClassLoader.instance().findClass( classPropertyIdentifier );
    assertNotNull( javaClass );
    assertEquals( classPropertyIdentifier, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}