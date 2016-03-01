/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.Modifier;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.gs.BytecodeOptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 */
public class ClassStructureTest extends ByteCodeTestBase
{
  public void testClassPublicEmpty() throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    Class<?> javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.statement.classes.ClassEmpty" );
    assertNotNull( javaClass );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.ClassEmpty", javaClass.getName() );
    assertTrue( Modifier.isPublic( javaClass.getModifiers() ) );
    assertNotNull( javaClass.newInstance() );
  }

  public void testClassInternalEmpty() throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    Class<?> javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.statement.classes.ClassInternalEmpty" );
    assertNotNull( javaClass );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.ClassInternalEmpty", javaClass.getName() );
    if( !BytecodeOptions.isSingleServingLoader() )
    {
      assertFalse( Modifier.isPublic( javaClass.getModifiers() ) );
    }
    else
    {
      assertTrue( Modifier.isPublic( javaClass.getModifiers() ) );
    }
  }

  public void testClassWithFields() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException
  {
    Class<?> javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.statement.classes.ClassWithFields" );
    assertNotNull( javaClass );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.ClassWithFields", javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    Object obj = javaClass.newInstance();
    Field field = obj.getClass().getField( "_stringVar2" );
    assertEquals( "hello", field.get( obj ) ); 
  }

  public void testClassWithLocalVars() throws Exception
  {
    Class<?> javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.statement.classes.ClassWithLocalVars" );
    assertNotNull( javaClass );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.ClassWithLocalVars", javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    Object obj = javaClass.newInstance();
    Method method = obj.getClass().getMethod( "foo", int.class );
    Object ret = method.invoke( obj, -2 );
    assertEquals( (Integer) 33, (Integer)ret );
  }

  public void testClassWithStaticVars() throws Exception
  {
    Object val = ReflectUtil.getStaticProperty( "gw.internal.gosu.compiler.sample.statement.classes.ClassStaticVars", "STRING_VAR" );
    assertEquals( "hello", val );
  }

  public void testAbstractClassWithStaticVars() throws Exception
  {
    Object val = ReflectUtil.getStaticProperty( "gw.internal.gosu.compiler.sample.statement.classes.AbstractClassStaticVars", "STRING_VAR" );
    assertEquals( "hello", val );
  }

  public void testExtendsAbstractClassWithStaticVarsReturnsCorrectValue() throws Exception
  {
    Object o = ReflectUtil.construct( "gw.internal.gosu.compiler.sample.statement.classes.ExtendsAbstractClassStaticVars" );
    Object val = ReflectUtil.invokeMethod( o, "getStaticVar" );
    assertEquals( "hello", val );
  }

  public void testExtendsAbstractClassWithStaticVarsReturnsCorrectValueWithConcatenation() throws Exception
  {
    Object o = ReflectUtil.construct( "gw.internal.gosu.compiler.sample.statement.classes.ExtendsAbstractClassStaticVars" );
    Object val = ReflectUtil.invokeMethod( o, "getStaticVarWithStringConcatenation" );
    assertEquals( "testhello", val );
  }

  public void testExtendsAbstractClassWithStaticVarsReturnsCorrectValueWithConcatenationWithProperty() throws Exception
  {
    Object o = ReflectUtil.construct( "gw.internal.gosu.compiler.sample.statement.classes.ExtendsAbstractClassStaticVars" );
    Object val = ReflectUtil.invokeMethod( o, "getStaticVarWithStringConcatenationWProp" );
    assertEquals( "StringProphello", val );
  }
}
