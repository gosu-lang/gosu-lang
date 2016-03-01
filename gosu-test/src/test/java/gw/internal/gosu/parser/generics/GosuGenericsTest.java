/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.internal.gosu.parser.GosuMethodInfo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 */
public class GosuGenericsTest extends TestClass
{
  public void testGenericGosuClassExtendsGenericInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.GenericClass" );
    assertTrue( gsClass.isValid() );
    List<GosuMethodInfo> methods = (List) gsClass.getTypeInfo().getMethods();
    GosuMethodInfo method = findMethod( methods, "foo" );
    assertTrue( method != null );
    assertEquals( 1, method.getParameters().length );
    assertTrue( method.getParameters()[0].getFeatureType() instanceof TypeVariableType );
    assertTrue( method.getReturnType() instanceof TypeVariableType );

    List<? extends IPropertyInfo> properties = gsClass.getTypeInfo().getProperties( gsClass );
    IPropertyInfo property = gsClass.getTypeInfo().getProperty(gsClass, "Prop1");
    assertEquals( "Prop1", property.getName() );
    assertTrue( property.getFeatureType() instanceof TypeVariableType );

    property = gsClass.getTypeInfo().getProperty(gsClass, "_t");
    assertEquals( "_t", property.getName() );
    assertTrue( property.getFeatureType() instanceof TypeVariableType );
  }

  private GosuMethodInfo findMethod(List<GosuMethodInfo> methods, String name)
  {
    for(GosuMethodInfo m : methods)
    {
      if( m.getDisplayName().equals( name) )
      {
        return m;
      }
    }
    return null;
  }

  public void testErrantGenericGosuClassHasErrorsForUnimplementedMethodsAndProperties() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ErrantGenericClass" );
    gsClass.isValid();
    assertTrue( gsClass.hasError() );
    ParseResultsException pre = gsClass.getParseResultsException();
    assertEquals( 3, pre.getParseExceptions().size() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, pre.getParseExceptions().get( 0 ).getMessageKey() );
    assertTrue( pre.getParseExceptions().get( 0 ).getConsoleMessage().contains( "foo" ) );

    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, pre.getParseExceptions().get( 1 ).getMessageKey() );
    assertTrue( pre.getParseExceptions().get( 1 ).getConsoleMessage().contains( "Prop1" ) );

    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, pre.getParseExceptions().get( 2 ).getMessageKey() );
    assertTrue( pre.getParseExceptions().get( 2 ).getConsoleMessage().contains( "Prop1" ) );
  }

  public void testGosuClassImplsGenericJavaInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.IterableImpl" );

    GosuMethodInfo method = (GosuMethodInfo) gsClass.getTypeInfo().getMethod("iterator");
    assertNotNull( method );
    assertEquals(TypeSystem.get( Iterator.class ).getParameterizedType( JavaTypes.OBJECT() ), method.getReturnType());
    Object gsInstance = constructInstance( "gw.internal.gosu.parser.generics.gwtest.IterableImpl" );
    Iterator iter = (Iterator)ReflectUtil.invokeMethod( gsInstance, "iterator" );
    assertTrue( iter.hasNext() );
    assertEquals( Date.class, iter.next().getClass() );
  }

  public void testGosuGenTypeWithTypeVarRef() throws ClassNotFoundException
  {
    IGosuObject res = (IGosuObject)ReflectUtil.invokeStaticMethod( "gw.internal.gosu.parser.generics.gwtest.SimpleGenericType_WithTypeVarRef", "foo" );
    IType[] typeParameters = res.getIntrinsicType().getTypeParameters();
    assertEquals( 2, typeParameters.length );
    assertEquals( JavaTypes.CHAR_SEQUENCE(), typeParameters[0] );
    assertEquals( JavaTypes.STRING(), typeParameters[1] );
  }

  public void testGosuClassImplsParameterizedJavaInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.IterableParameterizedImpl" );

    GosuMethodInfo method = (GosuMethodInfo) gsClass.getTypeInfo().getMethod("iterator");
    assertNotNull(method);
    assertEquals(TypeSystem.get( Iterator.class ).getParameterizedType( JavaTypes.STRING() ), method.getReturnType());

    Object gsInstance = constructInstance( "gw.internal.gosu.parser.generics.gwtest.IterableParameterizedImpl" );
    Iterator iter = (Iterator)ReflectUtil.invokeMethod( gsInstance, "iterator" );
    assertTrue( iter.hasNext() );
    IMethodInfo mi =((IGosuObject) iter).getIntrinsicType().getTypeInfo().getMethod("next");
    assertEquals( TypeSystem.get( String.class ), mi.getReturnType() );
    assertEquals( "hello", mi.getCallHandler().handleCall( iter ) );
  }

  public void testErrantParameterizedGosuClassHasErrorsForUnimplementedMethodsAndProperties() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ErrantIterableParameterizedImpl" );
    gsClass.isValid();
    assertTrue( gsClass.hasError() );
    ParseResultsException pre = gsClass.getParseResultsException();
    assertEquals( 2, pre.getParseExceptions().size() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, pre.getParseExceptions().get( 0 ).getMessageKey() );
    assertTrue( pre.getParseExceptions().get( 0 ).getConsoleMessage().contains( "iterator" ) );
    assertEquals( Res.MSG_FUNCTION_CLASH, pre.getParseExceptions().get( 1 ).getMessageKey() );
    assertTrue( pre.getParseExceptions().get( 1 ).getConsoleMessage().contains( "iterator" ) );
  }

  public void testInnerClassImplementsParameterizedInterfaceWithInnerClass() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.InnerClassImplementsParameterizedInterfaceWithInnerClass.StockListModel" );
    gsClass.isValid();
    System.out.println( gsClass.getParseResultsException() );
    assertTrue( gsClass.isValid() );
    IMethodInfo method = gsClass.getTypeInfo().getMethod( "iterator" );
    assertEquals( "iterator", method.getDisplayName() );
    assertEquals( 0, method.getParameters().length );
    IGosuClassInternal gsStockItemClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.InnerClassImplementsParameterizedInterfaceWithInnerClass.StockItem" );
    assertTrue( gsStockItemClass.isValid() );
    assertEquals( TypeSystem.get( Iterator.class ).getParameterizedType( gsStockItemClass ), method.getReturnType() );

    Object gsInstance = constructInstance( "gw.internal.gosu.parser.generics.gwtest.InnerClassImplementsParameterizedInterfaceWithInnerClass" );
    IPropertyInfo piModel = TypeSystem.getFromObject( gsInstance ).getTypeInfo().getProperty( "Model" );
    IGosuObject gsModelInstance = (IGosuObject)piModel.getAccessor().getValue( gsInstance );
    Iterator iterator = (Iterator)method.getCallHandler().handleCall( gsModelInstance );
    IGosuObject stockItem = (IGosuObject)iterator.next();
    assertEquals( gsStockItemClass, stockItem.getIntrinsicType() );
    String strValue = (String)gsStockItemClass.getTypeInfo().getProperty( "Symbol" ).getAccessor().getValue( stockItem );
    assertEquals( "goog", strValue );
  }

  public void testClassImplementsParameterizedJavaInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.MapImpl" );
    assertTrue( gsClass.isValid() );
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "put", TypeSystem.get( String.class ), TypeSystem.get( Integer.class ) );
    assertNotNull( mi );
    
    Object gsInstance = constructInstance( gsClass.getName() );
    mi.getCallHandler().handleCall( gsInstance, "A", 2 );
  }

  private Object constructInstance(String name) {
    return  ReflectUtil.construct(name);
  }

  public void testSuperIsBounded() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.TestSuperIsBounded" );
    assertTrue( gsClass.isValid() );
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "superIsBounded" );
    assertNotNull( mi );
    assertEquals( JavaTypes.CHAR_SEQUENCE(), mi.getReturnType().getTypeParameters()[0] );
  }

  public void testClassExtendsAbstractList() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ListImpl" );
    gsClass.isValid();
     System.out.println( gsClass.getParseResultsException() );
    assertTrue( gsClass.isValid() );
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "get", TypeSystem.get( int.class ) );
    assertNotNull( mi );

    gsClass = (IGosuClassInternal)gsClass.getParameterizedType( TypeSystem.get( StringBuilder.class ) );
    assertTrue( gsClass.isValid() );
    mi = gsClass.getTypeInfo().getMethod( "get", TypeSystem.get( int.class ) );
    assertNotNull( mi );
    IMethodInfo miAdd = gsClass.getTypeInfo().getMethod( "add", TypeSystem.get( StringBuilder.class ) );
    assertNotNull( miAdd );

    IGosuObject gsInstance = (IGosuObject)gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    miAdd.getCallHandler().handleCall( gsInstance, new StringBuilder( "Hello" ) );
    StringBuilder sb = (StringBuilder)mi.getCallHandler().handleCall( gsInstance, 0 );
    assertEquals( "Hello", sb.toString() );
  }

  public void testHasBlockWithRefToClassTypeVarInCtor() throws ClassNotFoundException, ParseResultsException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.HasBlockWithRefToClassTypeVarInCtor" );
    assertTrue( gsClass.isValid() );

    Integer value = (Integer)exec(
      "uses gw.internal.gosu.parser.generics.gwtest.HasBlockWithRefToClassTypeVarInCtor\n" +
      "\n" +
      "var g = new HasBlockWithRefToClassTypeVarInCtor<String>( \\s->s.length+1 )\n" +
      "return g.callF( \"ab\" )" );
    assertEquals( 3, (int)value );
  }

  public void testCallMethodFromBasePerspective() throws ClassNotFoundException, ParseResultsException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ExtendsAbstractGeneric" );
    assertTrue( gsClass.isValid() );

    String value = (String)exec(
      "uses gw.internal.gosu.parser.generics.gwtest.ExtendsAbstractGeneric\n" +
      "uses gw.internal.gosu.parser.generics.gwtest.AbstractGenericWithOneMethod\n" +
      "\n" +
      "var a : AbstractGenericWithOneMethod<String>\n" +
      "a = new ExtendsAbstractGeneric()\n" +
      "return a.foo( null )" );
    assertEquals( "hello", value );
  }

  public void testExtendsJavaGenericClassHasParameterizedSuper() throws ClassNotFoundException, ParseResultsException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ExtendsJavaGenericClass" );
    assertTrue( gsClass.isValid() );
    gsClass = (IGosuClassInternal)gsClass.getParameterizedType( TypeSystem.get( String.class ) );
    assertEquals( 1, gsClass.getTypeParameters().length );
    assertEquals( TypeSystem.get( String.class ), gsClass.getTypeParameters()[0] );
    assertEquals( 1, gsClass.getSupertype().getTypeParameters().length );
    assertEquals( TypeSystem.get( String.class ), gsClass.getSupertype().getTypeParameters()[0] );
  }

  public void testLocalSymbolDeclaredAsTypeVariableInitializesAtRuntimeWithCorrectlyParameterizedType() throws ParseResultsException
  {
    IType type =
      (IType)exec(
        "uses java.math.BigDecimal\n" +
        "uses java.util.ArrayList\n" +
        "\n" +
        "var l = new ArrayList<BigDecimal>()\n" +
        // Here l.sum() exercises type-var on local in the GWBaseCollectionOfNumberEnhancement
        "var s = l.sum()\n" +
        "return typeof s" );
    assertEquals( TypeSystem.get( BigDecimal.class ), type );
  }

  public void testBooleanReturnOnProperty()
  {
    Boolean ret =
      (Boolean)exec(
        "class MyClass<A> {\n" +
        "  var _a : A\n" +
        "  \n" +
        "  construct( a: A ) {\n" +
        "    _a = a\n" +
        "    print( a.toString().equals( \"false\" ) )\n" +
        "  }\n" +
        "  \n" +
        "  property get MyValue() : A {\n" +
        "    return _a\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "return new MyClass<Boolean>( true ).MyValue" );
    assertEquals( true, ret.booleanValue() );
  }

  private Object exec(String s) {
    return GosuTestUtil.eval(s);
  }

  public void testErrantHasGenericStaticField() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ErrantHasGenericStaticField" );
    gsClass.isValid();
    assertTrue( gsClass.hasError() );
    ParseResultsException pre = gsClass.getParseResultsException();
    assertEquals( 1, pre.getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT, pre.getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantHasGenericStaticField2() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.ErrantHasGenericStaticField2" );
    gsClass.isValid();
    assertTrue( gsClass.hasError() );
    ParseResultsException pre = gsClass.getParseResultsException();
    assertEquals( 1, pre.getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT, pre.getParseExceptions().get( 0 ).getMessageKey() );
  }

  private IGosuClassInternal loadClass(String s) {
    return (IGosuClassInternal) TypeSystem.getByFullName(s);
  }

//  public void testSubclassOfJavaClassWithProtectedMembers() throws ClassNotFoundException
//  {
//    GosuClass gsClass = loadClass( "gw.internal.gosu.parser.generics.gwtest.SubclassOfJavaClassWithProtectedMembers" );
//    assertTrue( gsClass.isValid() );
//    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "getProtectedFieldFromBase" );
//    IGosuObject gsInstance = constructInstance( "gw.internal.gosu.parser.generics.gwtest.SubclassOfJavaClassWithProtectedMembers" );
//    Number n = (Number)mi.getCallHandler().handleCall( gsInstance );
//    assertEquals( 5, n );
//  }
}
