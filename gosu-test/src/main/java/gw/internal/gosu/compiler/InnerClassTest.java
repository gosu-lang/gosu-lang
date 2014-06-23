/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.compiler.sample.statement.classes.inner.IJavaFoo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.IParseIssue;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.IGosuClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class InnerClassTest extends ByteCodeTestBase
{
  public void testCanLoadInnerClass() throws Exception
  {
    getGosuClass( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo.InnerFoo" );
  }

  public void testCanLoadOuterClass() throws Exception
  {
    getGosuClass( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo" );
  }

  public void testCanConstructInnerClass() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo" );
    Method m = getMethod( instance.getClass(), "getInnerFooInstance" );
    IGosuObject value = (IGosuObject)m.invoke( instance );
    assertEquals( getGosuClass( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo.InnerFoo" ), value.getIntrinsicType() );
  }

  private IGosuObject constructInstance( String strClass ) throws Exception
  {
    Class javaClass = getJavaClass( strClass );
    return (IGosuObject)javaClass.newInstance();
  }

  public void testCanConstructStaticInnerClassDirectly() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo.StaticInnerFoo" );
    Method m = getMethod( instance.getClass(), "getInnerValue" );
    String value = (String)m.invoke( instance );
    assertEquals( "staticInnerValue", value );
  }

  public void testOuterCanAccessMembersOnInner() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo" );
    Method m = getMethod( instance.getClass(), "getInnerFooData" );
    String value = (String)m.invoke( instance );
    assertEquals( "innerValue", value );
  }

  public void testInnerCanAccessMembersOnOuter() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.OuterFoo" );
    Method m = getMethod( instance.getClass(), "getInnerFooInstance" );
    IGosuObject value = (IGosuObject)m.invoke( instance );
    m = getMethod( value.getClass(), "getOuterFooData" );
    String strOuterValue = (String)m.invoke( value );
    assertEquals( "outerValue", strOuterValue );
  }

  public void testResolvesInnerFromSelf() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromSelf" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject innerInstance = (IGosuObject)m.invoke( instance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromSelf.Inner", innerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( innerInstance ) );

    m = getMethod( innerInstance.getClass(), "makeInnerFromSelf" );
    innerInstance = (IGosuObject)m.invoke( innerInstance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromSelf.Inner", innerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( innerInstance ) );
  }

  public void testResolvesInnerFromOuter() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromOuter" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject innerInstance = (IGosuObject)m.invoke( instance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromOuter.Inner", innerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( innerInstance ) );

    m = getMethod( innerInstance.getClass(), "makeSecondInner" );
    IGosuObject secondInnerInstance = (IGosuObject)m.invoke( innerInstance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromOuter.SecondInner", secondInnerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( secondInnerInstance ) );
  }

  public void testResolvesInnerFromInner() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromInner" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject innerInstance = (IGosuObject)m.invoke( instance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromInner.Inner", innerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( innerInstance ) );

    m = getMethod( innerInstance.getClass(), "makeInnerInner" );
    IGosuObject innerInnerInstance = (IGosuObject)m.invoke( innerInstance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromInner.Inner.InnerInner", innerInnerInstance.getIntrinsicType().getName() );
    assertEquals( innerInstance, getEnclosingClassInstance( innerInnerInstance ) );

    m = getMethod( innerInnerInstance.getClass(), "makeInner" );
    IGosuObject innerInstanceOfinnerInnerInstance = (IGosuObject)m.invoke( innerInnerInstance );
    assertEquals( "gw.internal.gosu.compiler.sample.statement.classes.inner.ResolveInnerFromInner.Inner", innerInstanceOfinnerInnerInstance.getIntrinsicType().getName() );
    assertEquals( instance, getEnclosingClassInstance( innerInstanceOfinnerInnerInstance ) );
  }

  public void testCanReferenceOuterFromInner() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceOuterFromInner" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );
    assertEquals( getGosuClass( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceOuterFromInner.Inner" ), value.getIntrinsicType() );
    m = getMethod( value.getClass(), "refOuter" );
    String strValue = (String)m.invoke( value );
    assertEquals( "something", strValue );
  }

  public void testInnerMethodShadowsOuter() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.InnerMethodShadowsOuter" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );
    assertEquals( getGosuClass( "gw.internal.gosu.compiler.sample.statement.classes.inner.InnerMethodShadowsOuter.Inner" ), value.getIntrinsicType() );

    m = getMethod( value.getClass(), "getSomething" );
    String strValue = (String)m.invoke( value );
    assertEquals( "inner something", strValue );

    m = getMethod( value.getClass(), "getOuterSomething" );
    strValue = (String)m.invoke( value );
    assertEquals( "something", strValue );
  }

  public void testCanReferenceInnerPrivateMembersFromOuter() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceInnerPrivateMembersFromOuter" );
    Method m = getMethod( instance.getClass(), "accessPrivateInnerFunction" );
    String strValue = (String)m.invoke( instance );
    assertEquals( "privateFunction", strValue );

    m = getMethod( instance.getClass(), "accessPrivateInnerData" );
    strValue = (String)m.invoke( instance );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceOuterPrivateMembersFromInner() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceOuterPrivateMembersFromInner" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "accessPrivateOuterFunction" );
    String strValue = (String)m.invoke( value );
    assertEquals( "privateFunction", strValue );

    m = getMethod( value.getClass(), "accessPrivateOuterData" );
    strValue = (String)m.invoke( value );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceOuterPrivateMembersFromInnerUsingViaOuterKeyword() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceOuterPrivateMembersFromInnerUsingViaOuterKeyword" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "accessPrivateOuterFunction" );
    String strValue = (String)m.invoke( value );
    assertEquals( "privateFunction", strValue );

    m = getMethod( value.getClass(), "accessPrivateOuterData" );
    strValue = (String)m.invoke( value );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceSiblingPrivateMembers() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.CanReferenceSiblingPrivateMembers" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "accessPrivateSiblingFunction" );
    String strValue = (String)m.invoke( value );
    assertEquals( "privateFunction", strValue );

    m = getMethod( value.getClass(), "accessPrivateSiblingData" );
    strValue = (String)m.invoke( value );
    assertEquals( "privateData", strValue );
  }

  public void testInnerCanImplInnerInterface() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.InnerCanImplInnerInterface" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "innerInterface1" );
    String strValue = (String)m.invoke( value );
    assertEquals( "1", strValue );

    m = getMethod( value.getClass(), "innerInterface2" );
    Number nValue = (Number)m.invoke( value );
    assertEquals( 2.0, nValue );
  }

  public void testInnerCanImplTopLevelInterface() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.InnerCanImplTopLevelInterface" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "interface1" );
    String strValue = (String)m.invoke( value );
    assertEquals( "1", strValue );

    m = getMethod( value.getClass(), "interface2" );
    Number nValue = (Number)m.invoke( value );
    assertEquals( 2.0, nValue );
  }

  public void testInnerCanImplJavaInterface() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.InnerCanImplJavaInterface" );
    Method m = getMethod( instance.getClass(), "makeInner" );
    IGosuObject value = (IGosuObject)m.invoke( instance );

    m = getMethod( value.getClass(), "interface1" );
    String strValue = (String)m.invoke( value );
    assertEquals( "1", strValue );

    m = getMethod( value.getClass(), "interface2" );
    Number nValue = (Number)m.invoke( value );
    assertEquals( 2.0, nValue );

    assertEquals( "1", ((IJavaFoo)value).interface1() );
    assertEquals( 2.0, ((IJavaFoo)value).interface2() );
  }

  public void testAnonymousClassReferencesLocalVar() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.AnonymousClassReferencesLocalVar" );
    Method m = getMethod( instance.getClass(), "foo" );
    Runnable runnable = (Runnable)m.invoke( instance );

    m = getMethod( instance.getClass(), "getStr" );
    String str = (String)m.invoke( instance );
    assertEquals( "hello", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( runnable.getClass(), "run" );
    m.invoke( runnable );

    m = getMethod( instance.getClass(), "getStr" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );
  }

  public void testNestedAnonymousClassCapturesOutersCapture() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.AnonymousClassReferencesLocalVar" );
    Method m = getMethod( instance.getClass(), "nestedAnonymousClassCapturesOutersCapture" );
    Runnable runnable = (Runnable)m.invoke( instance );

    m = getMethod( instance.getClass(), "getStr" );
    String str = (String)m.invoke( instance );
    assertEquals( "hello", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( runnable.getClass(), "run" );
    m.invoke( runnable );

    m = getMethod( instance.getClass(), "getStr" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );
  }

  public void testAnonymousClassReferencesLocalVarThatIsAFunctionParam() throws Exception
  {
    IGosuObject instance = constructInstance( "gw.internal.gosu.compiler.sample.statement.classes.inner.AnonymousClassReferencesLocalVar" );
    Method m = getMethod( instance.getClass(), "fooWithCapturedArg" );
    Runnable runnable = (Runnable)m.invoke( instance, "hello" );

    m = getMethod( instance.getClass(), "getStr" );
    String str = (String)m.invoke( instance );
    assertEquals( "hello", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( runnable.getClass(), "run" );
    m.invoke( runnable );

    m = getMethod( instance.getClass(), "getStr" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );

    m = getMethod( instance.getClass(), "getStrAfter" );
    str = (String)m.invoke( instance );
    assertEquals( "bye", str );
  }

  public void testTwoInnersCannotHaveSameName()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.statement.classes.inner.TwoInnersCannotHaveSameName" );
    assertFalse( gsClass.isValid() );
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfPropOnOuterThatReturnsAVariableNotCommonInBothScopesWorks() throws IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.statement.classes.inner.ThisPtrTest" );
    if( !gsClass.isValid() )
    {
      for( IParseIssue parseException : gsClass.getParseResultsException().getParseExceptions() )
      {
        parseException.printStackTrace();
      }
      fail();
    }
    IGosuObject instance = (IGosuObject)gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject)gsClass.getTypeInfo().getProperty( gsClass, "innerInstance" ).getAccessor().getValue( instance );
    String value = (String)((IGosuClass)innerInstance.getIntrinsicType()).getTypeInfo().getMethod( gsClass, "getOuterNoCommonProp" ).getCallHandler().handleCall( innerInstance );
    assertEquals( "outeronly", value );
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfPropOnOuterThatReturnsAVariableCommonInBothScopesWorks()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.statement.classes.inner.ThisPtrTest" );
    if( !gsClass.isValid() )
    {
      fail( gsClass.getParseResultsException().getParseExceptions().toString() );
    }
    IGosuObject instance = (IGosuObject)gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject)gsClass.getTypeInfo().getProperty( gsClass, "innerInstance" ).getAccessor().getValue( instance );
    String value = (String)((IGosuClass)innerInstance.getIntrinsicType()).getTypeInfo().getMethod( gsClass, "getOuterProp" ).getCallHandler().handleCall( innerInstance );
    assertEquals( "outer", value );
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfFuncOnOuterThatReturnsAVariableNotCommonInBothScopesWorks()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.statement.classes.inner.ThisPtrTest" );
    if( !gsClass.isValid() )
    {
      fail( gsClass.getParseResultsException().getParseExceptions().toString() );
    }
    IGosuObject instance = (IGosuObject )gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject )gsClass.getTypeInfo().getProperty( gsClass, "innerInstance" ).getAccessor().getValue( instance );
    String value = (String)((IGosuClass)innerInstance.getIntrinsicType()).getTypeInfo().getMethod( gsClass, "getOuterNoCommonFunc" ).getCallHandler().handleCall( innerInstance );
    assertEquals( "outeronly", value );
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfFuncOnOuterThatReturnsAVariableCommonInBothScopesWorks()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.statement.classes.inner.ThisPtrTest" );
    if( !gsClass.isValid() )
    {
      fail( gsClass.getParseResultsException().getParseExceptions().toString() );
    }
    IGosuObject instance = (IGosuObject)gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject)gsClass.getTypeInfo().getProperty( gsClass, "innerInstance" ).getAccessor().getValue( instance );
    String value = (String)((IGosuClass)innerInstance.getIntrinsicType()).getTypeInfo().getMethod( gsClass, "getOuterFunc" ).getCallHandler().handleCall( innerInstance );
    assertEquals( "outer", value );
  }

  public void testStaticAnonymousFieldWorks() throws Exception
  {
    Callable o = (Callable)invokeStaticMethod( "gw.internal.gosu.compiler.sample.statement.classes.inner.StaticAnonymousInnerClass",
                                               "staticField" );
    assertEquals( "Static field", o.call() );
  }

  public void testStaticAnonymousInnerClassesInFunctionWork() throws Exception
  {
    Callable o = (Callable)invokeStaticMethod( "gw.internal.gosu.compiler.sample.statement.classes.inner.StaticAnonymousInnerClass",
                                               "staticFunctionProducingCallable" );
    assertEquals( "Static function", o.call() );
  }

  public void testStaticAnonymousInnerClassesInProperty() throws Exception
  {
    Callable o = (Callable)invokeStaticMethod( "gw.internal.gosu.compiler.sample.statement.classes.inner.StaticAnonymousInnerClass",
                                               "getSaticPropProducingCallable" );
    assertEquals( "Static property", o.call() );
  }

  private Class getJavaClass( String strClass ) throws Exception
  {
    Class javaClass = GosuClassLoader.instance().findClass( strClass );
    assertNotNull( javaClass );
    return javaClass;
  }

  private IGosuClassInternal getGosuClass( String strClass ) throws Exception
  {
    return (IGosuClassInternal)TypeSystem.getByFullName( strClass );
  }

  protected Method getMethod( Class javaClass, String strMethod )
  {
    return getMethod( javaClass, strMethod, true );
  }

  protected Method getMethod( Class javaClass, String strMethod, boolean bDisplayName )
  {
    Method[] methods = javaClass.getMethods();
    for( Method m : methods )
    {
      String strName = m.getName();
      if( strName.equals( strMethod ) )
      {
        return m;
      }
    }
    return null;
  }

  private IGosuObject getEnclosingClassInstance( IGosuObject obj ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {
    Method m = obj.getClass().getDeclaredMethod( "access$0", obj.getClass() );
    m.setAccessible( true );
    return (IGosuObject)m.invoke( null, obj );
  }
}
