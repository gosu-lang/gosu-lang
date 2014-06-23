/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReifiedGenericsTest extends ByteCodeTestBase
{
  public void testConstructInstanceOfT() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "constructInstanceOfT" );
    Object createDynInstanceOfT = method.invoke( obj );
    assertEquals( StringBuilder.class, createDynInstanceOfT.getClass() );
  }

  public void testReturnInstanceOfT() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "returnInstanceOfT", Object.class );
    Boolean typeisT = (Boolean)method.invoke( obj, 1 );
    assertFalse( typeisT );
    typeisT = (Boolean)method.invoke( obj, new StringBuilder() );
    assertTrue( typeisT );
  }

  public void testReturnFunctionTypeParamTrue() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "returnFunctionTypeParamTrue" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnFunctionTypeParamFalse() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "returnFunctionTypeParamFalse" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( String.class ), typeParam );
  }

//  public void testIndirectCallReturnClassTypeParamFromStaticFunction() throws Exception
//  {
//    IGosuObject obj = getInstance();
//    Method method = obj.getClass().getMethod( "indirectCallReturnClassTypeParamFromStaticFunction" );
//    IType typeParam = (IType)method.invoke( null );
//    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
//  }
//
//  public void testReturnClassTypeParamFromStaticFunction() throws Exception
//  {
//    IGosuClassInternal gsClass = loadTestClass();
//
//    IMethodInfo method = gsClass.getTypeInfo().getMethod( "returnClassTypeParamFromStaticFunction" );
//    IType typeParam = (IType)method.getCallHandler().handleCall( null );
//    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
//  }
//
  public void testIndirectCallReturnClassTypeParam() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "indirectCallReturnClassTypeParam" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnClassTypeParam() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "returnClassTypeParam" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testTypeParamInProperty() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "getTestTypeParamInProperty" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testIndirectTypeParamFromProperty() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "getIndirectTestTypeParamInProperty" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testTypeParamAccessFromCtor() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "getTestTypeFromCtor" );
    IType typeParam = (IType)method.invoke( obj );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnOuterTypeParamFromInnerClass() throws Exception
  {
    IGosuObject outer = getInstance();
    Method method = outer.getClass().getMethod( "createInner" );
    IGosuObject gsInnerInstance = (IGosuObject)method.invoke( outer );

    method = gsInnerInstance.getClass().getMethod( "returnOuterTyperParamFromInnerClass" );
    IType typeParam = (IType)method.invoke( gsInnerInstance );
    assertEquals( TypeSystem.get( StringBuilder.class ), typeParam );
  }

  public void testReturnJavaClassParameterizedWithT() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "returnJavaClassParameterizedWithT" );
    Object javaObj = method.invoke( obj );
    assertEquals( ArrayList.class, javaObj.getClass() );
  }

  public void testReturnGosuClassParameterizedWithT() throws Exception
  {
    IGosuObject obj = getInstance();
    IType orgType = obj.getIntrinsicType();
    Method method = obj.getClass().getMethod( "returnGosuClassParameterizedWithT" );
    obj = (IGosuObject)method.invoke( obj );
    assertEquals( orgType.getGenericType().getParameterizedType( JavaTypes.STRING_BUILDER() ), obj.getIntrinsicType() );
  }

  public void testIndirectCallToGenericFunctionParameterizedWithATypeVar() throws Exception
  {
    IGosuObject obj = getInstance();
    Method method = obj.getClass().getMethod( "indirectCallToGenericFunctionParameterizedWithATypeVar" );
    obj = (IGosuObject)method.invoke( obj );
    assertEquals( "GClass<StringBuffer>", obj.getIntrinsicType().getRelativeName() );
  }

  private IGosuObject getInstance() throws Exception
  {
    Class javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.statement.classes.ReflectionGenericClass" );
    assertNotNull( javaClass );
    return (IGosuObject)javaClass.getMethod( "paramterizeOnStringBuilder" ).invoke( null );
  }
}
