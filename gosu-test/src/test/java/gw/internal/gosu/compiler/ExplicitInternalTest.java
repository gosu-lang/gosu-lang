/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.ir.Internal;
import gw.lang.reflect.Modifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 */
public class ExplicitInternalTest extends ByteCodeTestBase
{
  public void testThis() throws Exception
  {
    Object obj = newTestClass();

    // The compiler adds the @Internal annotation so tooling can distinguish between actual internal fields and those we changed from private to internal so they are accessible to inner classes
    Field f = obj.getClass().getDeclaredField( "_explicitInternal" );
    assertFalse( Modifier.isPublic( f.getModifiers() ) );
    assertFalse( Modifier.isProtected( f.getModifiers() ) );
    assertFalse( Modifier.isPrivate( f.getModifiers() ) );
    Annotation[] annotations = f.getDeclaredAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( Internal.class, annotations[0].annotationType() );

    // Private fields are made internal by the compiler so they are accessible to inner classes
    f = obj.getClass().getDeclaredField( "_implicitInternal" );
    assertFalse( Modifier.isPublic( f.getModifiers() ) );
    assertFalse( Modifier.isProtected( f.getModifiers() ) );
    assertFalse( Modifier.isPrivate( f.getModifiers() ) );
    annotations = f.getDeclaredAnnotations();
    assertZero( annotations.length );

    // The compiler adds the @Internal annotation so tooling can distinguish between actual internal methods and those we changed from private to internal so they are accessible to inner classes
    Method m = obj.getClass().getDeclaredMethod( "explicitInternal" );
    assertFalse( Modifier.isPublic( m.getModifiers() ) );
    assertFalse( Modifier.isProtected( m.getModifiers() ) );
    assertFalse( Modifier.isPrivate( m.getModifiers() ) );
    annotations = m.getDeclaredAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( Internal.class, annotations[0].annotationType() );

    // Private fields are made internal by the compiler so they are accessible to inner classes
    m = obj.getClass().getDeclaredMethod( "implicitInternal" );
    assertFalse( Modifier.isPublic( m.getModifiers() ) );
    assertFalse( Modifier.isProtected( m.getModifiers() ) );
    assertFalse( Modifier.isPrivate( m.getModifiers() ) );
    annotations = m.getDeclaredAnnotations();
    assertZero( annotations.length );
  }

  private Object newTestClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String classPropertyIdentifier = "gw.internal.gosu.compiler.sample.statement.classes.ExplicitInternalTestClass";
    Class<?> javaClass = GosuClassLoader.instance().findClass( classPropertyIdentifier );
    assertNotNull( javaClass );
    assertEquals( classPropertyIdentifier, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}