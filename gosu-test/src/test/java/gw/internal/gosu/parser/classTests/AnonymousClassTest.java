/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.ReflectUtil;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.resources.Res;
import gw.internal.gosu.parser.classTests.gwtest.anonymous.IJavaInterface;
import gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClass;
import gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClassWithProtectedCtor;
import gw.test.TestClass;
import manifold.api.templ.DisableStringLiteralTemplates;

import java.util.List;

public class AnonymousClassTest extends TestClass
{
  public void testCanConstructAnonymousClassJavaInterface() throws ClassNotFoundException
  {
    Object instance = ReflectUtil.construct( "gw.internal.gosu.parser.classTests.gwtest.anonymous.CanConstructAnonymousClassJavaInterface" );
    Object value = ReflectUtil.invokeMethod(instance, "create");
    assertTrue( value instanceof IJavaInterface );

    Object obj = ReflectUtil.invokeMethod(value, "function1");
    assertEquals( 8, ((Number)obj).intValue() );

    obj = ReflectUtil.invokeMethod( value, "function2", 5.2 );
    assertEquals( "String " + 5.2, obj );
  }

  public void testCanConstructAnonymousClassJavaClass() throws ClassNotFoundException
  {
    Object instance = ReflectUtil.construct( "gw.internal.gosu.parser.classTests.gwtest.anonymous.CanConstructAnonymousClassJavaClass" );
    Object value = ReflectUtil.invokeMethod(instance, "create");
    assertTrue( value instanceof JavaClass );

    Object obj = ReflectUtil.invokeMethod(value, "function1");
    assertEquals( 10, ((Number)obj).intValue() );

    obj = ReflectUtil.invokeMethod( value, "function2", 6.2 );
    assertEquals( "Anonymous String " + 6.2, obj );
  }

  public void testCanConstructAnonymousClassOnProtectedCtor() throws ClassNotFoundException
  {
    Object instance = ReflectUtil.construct( "gw.internal.gosu.parser.classTests.gwtest.anonymous.CanConstructAnonymousClassOnProtectedCtor" );
    Object value = ReflectUtil.invokeMethod(instance, "create");
    assertTrue( value instanceof JavaClassWithProtectedCtor );
  }

  @DisableStringLiteralTemplates
  public void testCanConstructInferredAnonymousType() throws ClassNotFoundException
  {
    Object instance = ReflectUtil.construct( "gw.internal.gosu.parser.classTests.gwtest.anonymous.CanConstructInferredAnonymousType" );
    Object value = ReflectUtil.invokeMethod(instance, "create");
    assertTrue( value.getClass().getName().equals( "gw.internal.gosu.parser.classTests.gwtest.anonymous.CanConstructInferredAnonymousType$AnonymouS__0" ) );
  }

  public void testCanNotConstructClassOnProtectedCtor() throws ClassNotFoundException
  {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullNameIfValid( "gw.internal.gosu.parser.classTests.gwtest.anonymous.test.CanNotConstructClassOnProtectedCtor" );
    assertFalse( gsClass.isValid() );
    List<IParseIssue> exceptions = gsClass.getParseResultsException().getParseExceptions();
    assertEquals( 1, exceptions.size() );
    assertEquals( Res.MSG_CTOR_HAS_XXX_ACCESS, exceptions.get( 0 ).getMessageKey() );
  }
}