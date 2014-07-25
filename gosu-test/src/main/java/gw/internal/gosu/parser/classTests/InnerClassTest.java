/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.classTests.gwtest.inner.IJavaFoo;
import gw.lang.reflect.INamespaceType;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.ReflectUtil;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.List;

/**
 */
public class InnerClassTest extends TestClass
{
  public void testCanParseSimpleJavaPackage() throws ParseResultsException
  {
    INamespaceType namespaceType = TypeSystem.getNamespace( "java" );
    assertNotNull( namespaceType );
    namespaceType = TypeSystem.getNamespace( "java.lang" );
    assertNotNull( namespaceType );
    namespaceType = TypeSystem.getNamespace( "java.awt.event" );
    assertNotNull( namespaceType );
    IExpression expr = GosuTestUtil.compileExpression( "new Object[] {null}" );
    assertNotNull( expr );
  }

  public void testCanLoadInnerClass()
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo.InnerFoo" );
    gsClass.compileDefinitionsIfNeeded();
    assertNotNull( gsClass );
  }

  private IGosuClassInternal getGosuClass(String s) {
    return (IGosuClassInternal) TypeSystem.getByFullName(s);
  }

  public void testCanLoadOuterClass()
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo" );
    gsClass.compileDefinitionsIfNeeded();
    assertNotNull( gsClass );
  }

  public void testCanConstructInnerClass() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo");
    IGosuObject value = (IGosuObject) ReflectUtil.invokeMethod( instance, "getInnerFooInstance" );
    assertEquals( getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo.InnerFoo" ), value.getIntrinsicType() );
  }

  public void testCanConstructStaticInnerClassDirectly() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo.StaticInnerFoo");
    String value = (String) ReflectUtil.getProperty(instance, "InnerValue");
    assertEquals( "staticInnerValue", value );
  }

  public void testOuterCanAccessMembersOnInner() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo");
    String value = (String) ReflectUtil.invokeMethod(instance, "getInnerFooData");
    assertEquals( "innerValue", value );
  }

  public void testInnerCanAccessMembersOnOuter() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.OuterFoo");
    IGosuObject value = (IGosuObject) ReflectUtil.invokeMethod( instance, "getInnerFooInstance" );
    String strOuterValue = (String)ReflectUtil.invokeMethod( value, "getOuterFooData" );
    assertEquals( "outerValue", strOuterValue );
  }

  public void testErrorInInnerClassExposedWhenAnErrorInOuterClassExists()
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.OuterFooError" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found errors" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 2, errors.size() );
      assertEquals( 37, errors.get( 1 ).getLine() );
    }
  }

  public void testErrorsReportedOnTwoInnerClasses()
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.ErrorOnTwoInners" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found errors" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 2, errors.size() );
      assertEquals( 36, errors.get( 0 ).getLine() );
      assertEquals( 56, errors.get( 1 ).getLine() );
    }
  }

  public void testResolvesInnerFromSelf() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromSelf");
    IGosuObject innerInstance = (IGosuObject)ReflectUtil.invokeMethod( instance, "makeInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromSelf.Inner", innerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, innerInstance.getEnclosingClassInstance() );

    innerInstance = (IGosuObject) ReflectUtil.invokeMethod(innerInstance, "makeInnerFromSelf");
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromSelf.Inner", innerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, innerInstance.getEnclosingClassInstance() );
  }

  public void testResolvesInnerFromOuter() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromOuter");
    IGosuObject innerInstance = (IGosuObject)ReflectUtil.invokeMethod( instance, "makeInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromOuter.Inner", innerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, innerInstance.getEnclosingClassInstance() );

    IGosuObject secondInnerInstance = (IGosuObject)ReflectUtil.invokeMethod( innerInstance, "makeSecondInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromOuter.SecondInner", secondInnerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, secondInnerInstance.getEnclosingClassInstance() );
  }

  public void testResolvesInnerFromInner() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromInner");
    IGosuObject innerInstance = (IGosuObject)ReflectUtil.invokeMethod( instance, "makeInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromInner.Inner", innerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, innerInstance.getEnclosingClassInstance() );

    IGosuObject innerInnerInstance = (IGosuObject)ReflectUtil.invokeMethod( innerInstance, "makeInnerInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromInner.Inner.InnerInner", innerInnerInstance.getIntrinsicType().getName() );
//    assertEquals( innerInstance, innerInnerInstance.getEnclosingClassInstance() );

    IGosuObject innerInstanceOfinnerInnerInstance = (IGosuObject)ReflectUtil.invokeMethod( innerInnerInstance, "makeInner" );
    assertEquals( "gw.internal.gosu.parser.classTests.gwtest.inner.ResolveInnerFromInner.Inner", innerInstanceOfinnerInnerInstance.getIntrinsicType().getName() );
//    assertEquals( instance, innerInstanceOfinnerInnerInstance.getEnclosingClassInstance() );
  }

  public void testFailToConstructInnerInnerFromInnerOuter()
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.FailToConstructInnerInnerFromInnerOuter" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found error" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 1, errors.size() );
      assertEquals( Res.MSG_MUST_BE_IN_OUTER_TO_CONSTRUCT_INNER, errors.get( 0 ).getMessageKey() );
    }
  }

  public void testInnerHasErrorOuterHasNone()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.inner.InnerHasErrorOuterHasNone" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found error" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 1, errors.size() );
      assertEquals( Res.MSG_INVALID_TYPE, errors.get( 0 ).getMessageKey() );
    }
  }

  public void testCanReferenceOuterFromInner() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceOuterFromInner");
    IGosuObject value = (IGosuObject)ReflectUtil.invokeMethod( instance, "makeInner" );
    assertEquals( getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceOuterFromInner.Inner" ), value.getIntrinsicType() );
    String strValue = (String)ReflectUtil.invokeMethod( value, "refOuter" );
    assertEquals( "something", strValue );
  }

  public void testCannotReferenceOuterFromStaticInner() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.CannotReferenceOuterFromStaticInner" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found error" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 1, errors.size() );
      assertEquals( Res.MSG_BAD_IDENTIFIER_NAME, errors.get( 0 ).getMessageKey() );
    }
  }

  public void testCannotReferenceOuterFromTopLevel() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.CannotReferenceOuterFromTopLevel" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found error" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 1, errors.size() );
      assertEquals( Res.MSG_BAD_IDENTIFIER_NAME, errors.get( 0 ).getMessageKey() );
    }
  }

  public void testCannotShadowEnclosingLocalVar() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.CannotShadowEnclosingLocalVar" );
    gsClass.isValid();
    IGosuClass anonymousClass = gsClass.getInnerClasses().iterator().next();
    anonymousClass.isValid();
    List<IParseIssue> errors = anonymousClass.getParseResultsException().getParseExceptions();
    assertEquals( 1, errors.size() );
    assertEquals( Res.MSG_VARIABLE_ALREADY_DEFINED, errors.get( 0 ).getMessageKey() );
  }

  public void testInnerMethodShadowsOuter() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.InnerMethodShadowsOuter");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );
    assertEquals( getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.InnerMethodShadowsOuter.Inner" ), value.getIntrinsicType() );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "getSomething" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "inner something", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "getOuterSomething" );
    strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "something", strValue );
  }

  private IMethodInfo getMethod(IGosuClassInternal iGosuClassInternal, String s) {
    return iGosuClassInternal.getTypeInfo().getMethod(s);
  }

  public void testStaticInnerClassCannotAccessNonStaticOuterMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = getGosuClass( "gw.internal.gosu.parser.classTests.gwtest.inner.StaticInnerClassCannotAccessNonStaticOuterMembers" );
    try
    {
      gsClass.compileDefinitionsIfNeeded();
      fail( "Should have found error" );
    }
    catch( ErrantGosuClassException e )
    {
      List<IParseIssue> errors = gsClass.getParseResultsException().getParseExceptions();
      assertEquals( 1, errors.size() );
      assertEquals( Res.MSG_NO_SUCH_FUNCTION, errors.get( 0 ).getMessageKey() );
    }
  }

  public void testCanReferenceInnerPrivateMembersFromOuter() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceInnerPrivateMembersFromOuter");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "accessPrivateInnerFunction" );
    String strValue = (String)mi.getCallHandler().handleCall( instance );
    assertEquals( "privateFunction", strValue );

    mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "accessPrivateInnerData" );
    strValue = (String)mi.getCallHandler().handleCall( instance );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceOuterPrivateMembersFromInner() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceOuterPrivateMembersFromInner");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateOuterFunction" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateFunction", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateOuterData" );
    strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceOuterPrivateMembersFromInnerUsingViaOuterKeyword() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceOuterPrivateMembersFromInnerUsingViaOuterKeyword");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateOuterFunction" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateFunction", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateOuterData" );
    strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateData", strValue );
  }

  public void testCanReferenceSiblingPrivateMembers() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.CanReferenceSiblingPrivateMembers");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateSiblingFunction" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateFunction", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "accessPrivateSiblingData" );
    strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "privateData", strValue );
  }

  public void testInnerCanImplInnerInterface() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.InnerCanImplInnerInterface");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "innerInterface1" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "1", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "innerInterface2" );
    Number nValue = (Number)mi.getCallHandler().handleCall( value );
    assertEquals( 2.0, nValue );
  }

  public void testInnerCanImplTopLevelInterface() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.InnerCanImplTopLevelInterface");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "interface1" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "1", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "interface2" );
    Number nValue = (Number)mi.getCallHandler().handleCall( value );
    assertEquals( 2.0, nValue );
  }

  public void testInnerCanImplJavaInterface() throws ClassNotFoundException
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance("gw.internal.gosu.parser.classTests.gwtest.inner.InnerCanImplJavaInterface");
    IMethodInfo mi = getMethod( (IGosuClassInternal)instance.getIntrinsicType(), "makeInner" );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( instance );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "interface1" );
    String strValue = (String)mi.getCallHandler().handleCall( value );
    assertEquals( "1", strValue );

    mi = getMethod( (IGosuClassInternal)value.getIntrinsicType(), "interface2" );
    Number nValue = (Number)mi.getCallHandler().handleCall( value );
    assertEquals( 2.0, nValue );

    assertEquals( "1", ((IJavaFoo)value).interface1() );
    assertEquals( 2.0, ((IJavaFoo)value).interface2() );
  }

  public void testCanAccessStaticMethodOnJavaStaticInnerClass() throws ParseResultsException
  {
    Object result = GosuTestUtil.eval( "gw.internal.gosu.parser.classTests.Foo.Bar.Fubar()" );
    assertEquals( 9, ((Number)result).intValue() );
  }

  public void testInnerCanHaveSameNameAsOuter() throws ClassNotFoundException
  {
    IGosuClassInternal gosuClass =  (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.InnerCanHaveSameNameAsOuter2");
    if (!gosuClass.isValid()) {
      System.err.println(gosuClass.getParseResultsException().getMessage());
    }
    assertTrue(gosuClass.isValid());
  }

  public void testTwoInnersCannotHaveSameName() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.TwoInnersCannotHaveSameName");
    assertFalse(gosuClass.isValid());
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfPropOnOuterThatReturnsAVariableNotCommonInBothScopesWorks() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.ThisPtrTest");
    if (!gosuClass.isValid()) {
      for (IParseIssue parseException : gosuClass.getParseResultsException().getParseExceptions()) {
        parseException.printStackTrace();
      }
      fail();
    }
    IGosuObject instance = (IGosuObject) gosuClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject) gosuClass.getTypeInfo().getProperty(gosuClass, "innerInstance").getAccessor().getValue(instance);
    String value = (String) getMethod( (IGosuClassInternal)TypeSystem.getFromObject( innerInstance ), "getOuterNoCommonProp").getCallHandler().handleCall(innerInstance);
    assertEquals("outeronly", value);
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfPropOnOuterThatReturnsAVariableCommonInBothScopesWorks() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.ThisPtrTest");
    if (!gosuClass.isValid()) {
      fail(gosuClass.getParseResultsException().getParseExceptions().toString());
    }
    IGosuObject instance = (IGosuObject) gosuClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject) gosuClass.getTypeInfo().getProperty(gosuClass, "innerInstance").getAccessor().getValue(instance);
    String value = (String) getMethod((IGosuClassInternal)TypeSystem.getFromObject( innerInstance ), "getOuterProp").getCallHandler().handleCall(innerInstance);
    assertEquals("outer", value);
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfFuncOnOuterThatReturnsAVariableNotCommonInBothScopesWorks() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.ThisPtrTest");
    if (!gosuClass.isValid()) {
      fail(gosuClass.getParseResultsException().getParseExceptions().toString());
    }
    IGosuObject instance = (IGosuObject) gosuClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject) gosuClass.getTypeInfo().getProperty(gosuClass, "innerInstance").getAccessor().getValue(instance);
    String value = (String) getMethod((IGosuClassInternal)TypeSystem.getFromObject( innerInstance ), "getOuterNoCommonFunc").getCallHandler().handleCall(innerInstance);
    assertEquals("outeronly", value);
  }

  public void testCallingMethodOnInnerClassThatReturnsValueOfFuncOnOuterThatReturnsAVariableCommonInBothScopesWorks() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.inner.ThisPtrTest");
    if (!gosuClass.isValid()) {
      fail(gosuClass.getParseResultsException().getParseExceptions().toString());
    }
    IGosuObject instance = (IGosuObject) gosuClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    IGosuObject innerInstance = (IGosuObject) gosuClass.getTypeInfo().getProperty(gosuClass, "innerInstance").getAccessor().getValue(instance);
    String value = (String) getMethod( (IGosuClassInternal)innerInstance.getIntrinsicType(), "getOuterFunc").getCallHandler().handleCall(innerInstance);
    assertEquals("outer", value);
  }
}
