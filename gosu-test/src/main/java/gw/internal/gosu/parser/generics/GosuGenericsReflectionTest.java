/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.generics.gwtest.java.ParameterizedConstructorTestClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

import java.util.ArrayList;

/**
 */
public class GosuGenericsReflectionTest extends TestClass
{
  public void testConstructInstanceOfT() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "constructInstanceOfT" );
    Object createDynInstanceOfT = method.getCallHandler().handleCall( gsInstance );
    assertEquals( StringBuilder.class, createDynInstanceOfT.getClass() );
  }

  public void testReturnInstanceOfT() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnInstanceOfT", TypeSystem.get( Object.class ) );
    Boolean typeisT = (Boolean)method.getCallHandler().handleCall( gsInstance, 1 );
    assertFalse( typeisT );
    typeisT = (Boolean)method.getCallHandler().handleCall( gsInstance, new StringBuilder() );
    assertTrue( typeisT );
  }

  public void testReturnFunctionTypeParamTrue() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnFunctionTypeParamTrue" );
    IType typeParam = (IType)method.getCallHandler().handleCall( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnFunctionTypeParamFalse() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnFunctionTypeParamFalse" );
    IType typeParam = (IType)method.getCallHandler().handleCall( gsInstance );
    assertEquals( TypeSystem.get( String.class ), typeParam );
  }

  public void testIndirectCallReturnClassTypeParam() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "indirectCallReturnClassTypeParam" );
    IType typeParam = (IType)method.getCallHandler().handleCall( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnClassTypeParam() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnClassTypeParam" );
    IType typeParam = (IType)method.getCallHandler().handleCall( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testTypeParamInProperty() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "TestTypeParamInProperty" );
    IType typeParam = (IType)pi.getAccessor().getValue( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testIndirectTypeParamFromProperty() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "IndirectTestTypeParamInProperty" );
    IType typeParam = (IType)pi.getAccessor().getValue( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testTypeParamAccessFromCtor() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "TestTypeFromCtor" );
    IType typeParam = (IType)pi.getAccessor().getValue( gsInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnOuterTypeParamFromInnerClass() throws ClassNotFoundException
  {
    IGosuClassInternal gsOuterClass = loadTestClass();
    Object gsOuterInstance = gsOuterClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsOuterClass.getTypeInfo().getMethod( "createInner" );
    Object gsInnerInstance = method.getCallHandler().handleCall( gsOuterInstance );

    method = TypeSystem.getFromObject(gsInnerInstance).getTypeInfo().getMethod( "returnOuterTyperParamFromInnerClass" );
    IType typeParam = (IType)method.getCallHandler().handleCall( gsInnerInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnJavaClassParameterizedWithT() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    gsClass = (IGosuClassInternal)gsClass.getGenericType().getParameterizedType( JavaTypes.STRING() );
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnJavaClassParameterizedWithT" );
    Object obj = method.getCallHandler().handleCall( gsInstance );
    assertEquals( ArrayList.class, obj.getClass() );
  }

  public void testReturnGosuClassParameterizedWithT() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    gsClass = (IGosuClassInternal)gsClass.getGenericType().getParameterizedType( JavaTypes.STRING() );
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnGosuClassParameterizedWithT" );
    IGosuObject obj = (IGosuObject)method.getCallHandler().handleCall( gsInstance );
    assertEquals( gsClass, obj.getIntrinsicType() );
  }

  public void testIndirectCallToGenericFunctionParameterizedWithATypeVar() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "indirectCallToGenericFunctionParameterizedWithATypeVar" );
    IGosuObject obj = (IGosuObject)method.getCallHandler().handleCall( gsInstance );
    assertEquals( "GClass<StringBuffer>", obj.getIntrinsicType().getRelativeName() );
  }

  public void testEnsureReferenceToThisDoesNotOverwriteSymbolInStack() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadTestClass();
    Object gsInstance = gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();

    IMethodInfo method = gsClass.getTypeInfo().getMethod( "ensureReferenceToThisDoesNotOverwriteSymbolInStack" );
    IType type = (IType)method.getCallHandler().handleCall( gsInstance );
    assertEquals( JavaTypes.STRING_BUILDER(), type );
  }

  public void testExtendingJavaClassThatHasTypeVariableTypeAsConstructorParam() throws ClassNotFoundException {
    Object gsClassInstance = TypeSystem.getByFullName("gw.internal.gosu.parser.generics.gwtest.GClass").getTypeInfo().getConstructors().get(0).getConstructor().newInstance("here", new Object());
    ParameterizedConstructorTestClass instance = (ParameterizedConstructorTestClass)
            ReflectUtil.construct("gw.internal.gosu.parser.generics.gwtest.SubclassOfJavaWithParamTypeInSuperCall", gsClassInstance);
    assertSame(gsClassInstance, instance._t);
  }

  private IGosuClassInternal loadTestClass() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) TypeSystem.getByFullName( "gw.internal.gosu.parser.generics.gwtest.ReflectionGenericClass" );
    assertTrue( gsClass.isValid() );
    gsClass = (IGosuClassInternal)gsClass.getParameterizedType( TypeSystem.get( StringBuilder.class ) );
    return gsClass;
  }
}
