/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.compiler.sample.annotations.AllTypesJavaAnnotation;
import gw.internal.gosu.compiler.sample.annotations.InheritedJavaAnnotation;
import gw.internal.gosu.compiler.sample.annotations.JavaSubWithAnnotations;
import gw.internal.gosu.compiler.sample.annotations.MultiArgJavaAnnotation;
import gw.internal.gosu.compiler.sample.annotations.NoArgJavaAnnotation;
import gw.internal.gosu.compiler.sample.annotations.OneArgJavaAnnotation;
import gw.internal.gosu.compiler.sample.annotations.SampleAnnotationEnum;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.function.IBlock;
import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IAnnotationInfo;
import gw.util.AnnotationUtil;
import manifold.api.templ.DisableStringLiteralTemplates;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class AnnotationsTest extends ByteCodeTestBase
{
  private static final String PKG_PREFIX = "gw.internal.gosu.compiler.sample.annotations.";

  public void testJavaNoArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "javaNoArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)java.get( 0 ); //just cast to make sure it works
    assertEquals( 1.0f, 1.0f );
  }

  public void testGosuNoArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "gosuNoArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedNoArgAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedNoArgAnnotationsMethodWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "javaOneArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testGosuOneArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "gosuOneArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
  }

  public void testMixedSimpleOneArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedOneArgAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testMixedSimpleOneArgAnnotationsWithTwoGosuAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedOneArgAnnotationsMethodWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testJavaMultiArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "javaMultiArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testGosuMultiArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "gosuMultiArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
  }

  public void testMixedSimpleMultiArgAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedMultiArgAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnMethodWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "allJavaAnnotationFieldTypesAnnotationMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testAdditiveExprOnMethod()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsAdditiveExprMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedAdditiveExprOnMethod()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsChainedAdditiveExprMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedStrConcatenationOnMethod()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsStrAdditiveExprMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testNoArgBlockOnMethod()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsNoArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnMethod() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsNoArgBlockWithProgramAndInnerClassMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnMethod()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsOneArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnMethod() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "whereExprIsCallableMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testJavaNoArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "JavaNoArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)java.get( 0 ); //just cast to make sure it works
  }

  public void testGosuNoArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "GosuNoArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedNoArgAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedNoArgAnnotationsPropertyWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "JavaOneArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testGosuOneArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "GosuOneArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
  }

  public void testMixedSimpleOneArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedOneArgAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testMixedSimpleOneArgAnnotationsWithTwoGosuAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedOneArgAnnotationsPropertyWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testJavaMultiArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "JavaMultiArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testGosuMultiArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "GosuMultiArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
  }

  public void testMixedSimpleMultiArgAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedMultiArgAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnPropertyWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "AllJavaAnnotationFieldTypesAnnotationProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testAdditiveExprOnProperty()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsAdditiveExprProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedAdditiveExprOnProperty()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsChainedAdditiveExprProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedStrConcatenationOnProperty()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsStrAdditiveExprProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testNoArgBlockOnProperty()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsNoArgBlockProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnProperty() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsNoArgBlockWithProgramAndInnerClassProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnProperty()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsOneArgBlockProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnProperty() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "WhereExprIsCallableProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testJavaNoArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 1 );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)java.get( 0 ); //just cast to make sure it works
  }


  public void testGosuNoArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 2 );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 3 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 4 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 5 );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testGosuOneArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 6 );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
  }

  public void testMixedSimpleOneArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 7 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testMixedSimpleOneArgAnnotationsWithTwoGosuAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 8 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testJavaMultiArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 9 );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testGosuMultiArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 10 );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
  }

  public void testMixedSimpleMultiArgAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 11 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 12 );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnConstructorWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 13 );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testAdditiveExprOnConstructor()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 14 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedAdditiveExprOnConstructor()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 15 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedStrConcatenationOnConstructor()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 16 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testNoArgBlockOnConstructor()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 17 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnConstructor() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 19 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnConstructor()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 18 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnConstructor() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForConstructor( o, 20 );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testJavaNoArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "JavaNoArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)java.get( 0 ); //just cast to make sure it works
  }

  public void testGosuNoArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "GosuNoArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedNoArgAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedNoArgAnnotationsVarWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "JavaOneArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testGosuOneArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "GosuOneArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
  }

  public void testMixedSimpleOneArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedOneArgAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testMixedSimpleOneArgAnnotationsWithTwoGosuAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedOneArgAnnotationsVarWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testJavaMultiArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "JavaMultiArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testGosuMultiArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "GosuMultiArgAnnotationsVar" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
  }

  public void testMixedSimpleMultiArgAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedMultiArgAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnVarWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "AllJavaAnnotationFieldTypesAnnotationVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testAdditiveExprOnVar()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsAdditiveExprVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedAdditiveExprOnVar()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsChainedAdditiveExprVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedStrConcatenationOnVar()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsStrAdditiveExprVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testNoArgBlockOnVar()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsNoArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnVar() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsNoArgBlockWithProgramAndInnerClassVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnVar()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsOneArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnVar() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "WhereExprIsCallableVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testJavaNoArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "JavaNoArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)java.get( 0 ); //just cast to make sure it works
  }

  public void testGosuNoArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "GosuNoArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedNoArgAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedNoArgAnnotationsClassWithTwoGosuAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "JavaOneArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testGosuOneArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "GosuOneArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
  }

  public void testMixedSimpleOneArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedOneArgAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testMixedSimpleOneArgAnnotationsWithTwoGosuAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedOneArgAnnotationsClassWithTwoGosuAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
  }

  public void testJavaMultiArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "JavaMultiArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testGosuMultiArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "GosuMultiArgAnnotationsClass" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
  }

  public void testMixedSimpleMultiArgAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedMultiArgAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    assertEquals( "gosuArg", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "MixedMultiArgAnnotationsClassWithTwoGosuAnnotationsClass" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnClassWork() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "AllJavaAnnotationFieldTypesAnnotationClass" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testAdditiveExprOnClass()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsAdditiveExprClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedAdditiveExprOnClass()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsChainedAdditiveExprClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testChainedStrConcatenationOnClass()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsStrAdditiveExprClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ), "Value" ) );
  }

  public void testNoArgBlockOnClass()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsNoArgBlockClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnClass() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsNoArgBlockWithProgramAndInnerClassClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnClass()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsOneArgBlockClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnClass() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForInnerClass( o, "WhereExprIsCallableClass" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  // Anonymous --------------------

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnAnonymousInnerMethodWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnAnonymousInnerMethodWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_allJavaAnnotationFieldTypesAnnotationMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnAnonymousInnerMethod()
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_whereExprIsNoArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnAnonymousInnerMethod() throws Exception
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_whereExprIsNoArgBlockWithProgramAndInnerClassMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnAnonymousInnerMethod()
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_whereExprIsOneArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnAnonymousInnerMethod() throws Exception
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "anon_whereExprIsCallableMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnAnonymousInnerPropertyWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "anon_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnAnonymousInnerPropertyWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "anon_AllJavaAnnotationFieldTypesAnnotationProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnAnonymousInnerProperty()
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "anon_WhereExprIsNoArgBlockProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnAnonymousInnerProperty() throws Exception
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "anon_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnAnonymousInnerVarWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnAnonymousInnerVarWork() {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_AllJavaAnnotationFieldTypesAnnotationVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnAnonymousInnerVar()
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_WhereExprIsNoArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnAnonymousInnerVar() throws Exception
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_WhereExprIsNoArgBlockWithProgramAndInnerClassVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnAnonymousInnerVar()
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_WhereExprIsOneArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnAnonymousInnerVar() throws Exception
  {
    Object o = newAnonymousInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "anon_WhereExprIsCallableVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  // Inner --------------------

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnInnerMethodWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnInnerMethodWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_allJavaAnnotationFieldTypesAnnotationMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnInnerMethod()
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_whereExprIsNoArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnInnerMethod() throws Exception
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_whereExprIsNoArgBlockWithProgramAndInnerClassMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnInnerMethod()
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_whereExprIsOneArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnInnerMethod() throws Exception
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "inner_whereExprIsCallableMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnInnerPropertyWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "inner_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnInnerPropertyWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "inner_AllJavaAnnotationFieldTypesAnnotationProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnInnerProperty()
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "inner_WhereExprIsNoArgBlockProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnInnerProperty() throws Exception
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "inner_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnInnerVarWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnInnerVarWork() {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_AllJavaAnnotationFieldTypesAnnotationVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnInnerVar()
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_WhereExprIsNoArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnInnerVar() throws Exception
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_WhereExprIsNoArgBlockWithProgramAndInnerClassVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnInnerVar()
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_WhereExprIsOneArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnInnerVar() throws Exception
  {
    Object o = newInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "inner_WhereExprIsCallableVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  // StaticInner --------------------

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnStaticInnerMethodWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnStaticInnerMethodWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_allJavaAnnotationFieldTypesAnnotationMethod" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnStaticInnerMethod()
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_whereExprIsNoArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndStaticInnerClassOnStaticInnerMethod() throws Exception
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_whereExprIsNoArgBlockWithProgramAndInnerClassMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnStaticInnerMethod()
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_whereExprIsOneArgBlockMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnStaticInnerMethod() throws Exception
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForMethod( o, "static_inner_whereExprIsCallableMethod" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnStaticInnerPropertyWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "static_inner_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnStaticInnerPropertyWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "static_inner_AllJavaAnnotationFieldTypesAnnotationProperty" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnStaticInnerProperty()
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "static_inner_WhereExprIsNoArgBlockProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndStaticInnerClassOnStaticInnerProperty() throws Exception
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForProperty( o, "static_inner_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testMixedSimpleMultiArgAnnotationsWithTwoGosuAnnotationsOnStaticInnerVarWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    assertEquals( "gosuArg1", getProp( gosu.get( 0 ), "StrValue" ) );
    assertEquals( 42, getProp( gosu.get( 0 ), "IntValue" ) );
    assertEquals( "gosuArg2", getProp( gosu.get( 1 ), "StrValue" ) );
    assertEquals( 43, getProp( gosu.get( 1 ), "IntValue" ) );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "MultiArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    MultiArgJavaAnnotation val = (MultiArgJavaAnnotation)java.get( 0 );
    assertEquals( "javaArg", val.strVal() );
    assertEquals( 42, val.intVal() );
  }

  public void testAllSupportedJavaAnnotationValueTypesOnStaticInnerVarWork() {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_AllJavaAnnotationFieldTypesAnnotationVar" );
    assertEquals( 1, map.size() );
    List java = map.get( TypeSystem.get( AllTypesJavaAnnotation.class ) );
    assertEquals( 1, java.size() );
    AllTypesJavaAnnotation val = (AllTypesJavaAnnotation)java.get( 0 );

    assertTrue( val.a_boolValue() );
    assertEquals( 3, val.a_boolValueArr().length );
    assertEquals( true, val.a_boolValueArr()[0] );
    assertEquals( false, val.a_boolValueArr()[1] );
    assertEquals( true, val.a_boolValueArr()[2] );

    assertEquals( (byte) 1, val.b_byteValue() );
    assertEquals( 3, val.b_byteValueArr().length );
    assertEquals( (byte) 1, val.b_byteValueArr()[0] );
    assertEquals( (byte) 0, val.b_byteValueArr()[1] );
    assertEquals( (byte) 1, val.b_byteValueArr()[2] );

    assertEquals( 'a', val.c_charValue() );
    assertEquals( 3, val.c_charValueArr().length );
    assertEquals( 'a', val.c_charValueArr()[0] );
    assertEquals( 'b', val.c_charValueArr()[1] );
    assertEquals( 'c', val.c_charValueArr()[2] );

    assertEquals( (short) 42, val.d_shortValue() );
    assertEquals( 3, val.d_shortValueArr().length );
    assertEquals( (short) 40, val.d_shortValueArr()[0] );
    assertEquals( (short) 41, val.d_shortValueArr()[1] );
    assertEquals( (short) 42, val.d_shortValueArr()[2] );

    assertEquals( 42, val.e_intValue() );
    assertEquals( 3, val.e_intValueArr().length );
    assertEquals( 40, val.e_intValueArr()[0] );
    assertEquals( 41, val.e_intValueArr()[1] );
    assertEquals( 42, val.e_intValueArr()[2] );

    assertEquals( (long) 42, val.f_longValue() );
    assertEquals( 3, val.f_longValueArr().length );
    assertEquals( (long) 40, val.f_longValueArr()[0] );
    assertEquals( (long) 41, val.f_longValueArr()[1] );
    assertEquals( (long) 42, val.f_longValueArr()[2] );

    assertEquals( (float) 42.0, val.g_floatValue() );
    assertEquals( 3, val.g_floatValueArr().length );
    assertEquals( (float) 40.0, val.g_floatValueArr()[0] );
    assertEquals( (float) 41.0, val.g_floatValueArr()[1] );
    assertEquals( (float) 42.0, val.g_floatValueArr()[2] );

    assertEquals( 42.0, val.h_doubleValue() );
    assertEquals( 3, val.h_doubleValueArr().length );
    assertEquals( 40.0, val.h_doubleValueArr()[0] );
    assertEquals( 41.0, val.h_doubleValueArr()[1] );
    assertEquals( 42.0, val.h_doubleValueArr()[2] );

    assertEquals( "foo", val.i_strValue() );
    assertEquals( 3, val.i_strValueArr().length );
    assertEquals( "foo", val.i_strValueArr()[0] );
    assertEquals( "bar", val.i_strValueArr()[1] );
    assertEquals( "doh", val.i_strValueArr()[2] );

    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValue() );
    assertEquals( 3, val.j_enumValueArr().length );
    assertEquals( SampleAnnotationEnum.FIRST_VAL, val.j_enumValueArr()[0] );
    assertEquals( SampleAnnotationEnum.SECOND_VAL, val.j_enumValueArr()[1] );
    assertEquals( SampleAnnotationEnum.THIRD_VAL, val.j_enumValueArr()[2] );
  }

  public void testNoArgBlockOnStaticInnerVar()
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_WhereExprIsNoArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndStaticInnerClassOnStaticInnerVar() throws Exception
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_WhereExprIsNoArgBlockWithProgramAndInnerClassVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testOneArgBlockOnStaticInnerVar()
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_WhereExprIsOneArgBlockVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "asdfasdf", prop.invokeWithArgs( "asdfasdf" ) );
  }

  public void testCallableOnStaticInnerVar() throws Exception
  {
    Object o = newStaticInnerAnnotationHolder();
    Map<IType, List> map = getAnnotationMapForVar( o, "static_inner_WhereExprIsCallableVar" );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    Callable prop = (Callable)getProp( gosu.get( 0 ), "Value" );
    assertEquals( "From callable", prop.call() );
  }

  //  TypeInfo tests ========================================================================

  public void testJavaNoArgAnnotationsOnMethodWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForMethodThroughTypeInfo( o, "javaNoArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List<IAnnotationInfo> tmp = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, tmp.size() );
    IAnnotationInfo annotationInfo = tmp.get( 0 );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)annotationInfo.getInstance(); //just cast to make sure it works
  }

  public void testGosuNoArgAnnotationsOnMethodWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForMethodThroughTypeInfo( o, "gosuNoArgAnnotationsMethod" );
    assertEquals( 1, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnMethodWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForMethodThroughTypeInfo( o, "mixedNoArgAnnotationsMethod" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnMethodWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForMethodThroughTypeInfo( o, "mixedNoArgAnnotationsMethodWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaNoArgAnnotationsOnPropertyWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForPropertyThroughTypeInfo( o, "JavaNoArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List<IAnnotationInfo> java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    IAnnotationInfo info = java.get( 0 );
    NoArgJavaAnnotation val = (NoArgJavaAnnotation)info.getInstance(); //just cast to make sure it works
  }

  public void testGosuNoArgAnnotationsOnPropertyWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForPropertyThroughTypeInfo( o, "GosuNoArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
  }

  public void testMixedSimpleNoArgAnnotationsOnPropertyWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForPropertyThroughTypeInfo( o, "MixedNoArgAnnotationsProperty" );
    assertEquals( 2, map.size() );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 1, gosu.size() );
    List<IAnnotationInfo> java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testMixedSimpleNoArgAnnotationsWithTwoGosuAnnotationsOnPropertyWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForPropertyThroughTypeInfo( o, "MixedNoArgAnnotationsPropertyWithTwoGosuAnnotations" );
    assertEquals( 2, map.size() );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgGosuAnnotation" ) );
    assertEquals( 2, gosu.size() );
    List<IAnnotationInfo> java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "NoArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
  }

  public void testJavaOneArgAnnotationsOnPropertyWorkThroughTypeInfo() {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForPropertyThroughTypeInfo( o, "JavaOneArgAnnotationsProperty" );
    assertEquals( 1, map.size() );
    List<IAnnotationInfo> java = map.get( TypeSystem.getByFullName( PKG_PREFIX + "OneArgJavaAnnotation" ) );
    assertEquals( 1, java.size() );
    IAnnotationInfo info = java.get( 0 );
    OneArgJavaAnnotation val = (OneArgJavaAnnotation)info.getInstance();
    assertEquals( "javaArg", val.strVal() );
  }

  public void testAdditiveExprOnConstructorThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForConstructorThroughTypeInfo( o, 14 );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  public void testChainedAdditiveExprOnConstructorThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForConstructorThroughTypeInfo( o, 15 );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  public void testChainedStrConcatenationOnConstructorThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForConstructorThroughTypeInfo( o, 16 );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  public void testNoArgBlockOnConstructorThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForConstructorThroughTypeInfo( o, 17 );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ).getInstance(), "Value" );
    assertEquals( "test", prop.invokeWithArgs() );
  }

  public void testNoArgBlockWithProgramAndInnerClassOnConstructorThroughTypeInfo() throws Exception
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForConstructorThroughTypeInfo( o, 19 );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    IBlock prop = (IBlock)getProp( gosu.get( 0 ).getInstance(), "Value" );
    Callable callable = (Callable)prop.invokeWithArgs( true );
    assertEquals( false, callable.call() );
    callable = (Callable)prop.invokeWithArgs( false );
    assertEquals( true, callable.call() );
  }

  public void testAdditiveExprOnClassThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForInnerClassThroughTypeInfo( o, "WhereExprIsAdditiveExprClass" );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 2, getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  public void testChainedAdditiveExprOnClassThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForInnerClassThroughTypeInfo( o, "WhereExprIsChainedAdditiveExprClass" );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( 3, getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  public void testChainedStrConcatenationOnClassThroughTypeInfo()
  {
    Object o = newSampleAnnotationHolder();
    Map<IType, List<IAnnotationInfo>> map = getAnnotationMapForInnerClassThroughTypeInfo( o, "WhereExprIsStrAdditiveExprClass" );
    List<IAnnotationInfo> gosu = map.get( TypeSystem.getByFullName( PKG_PREFIX + "AnyArgGosuAnnotation" ) );
    assertEquals( "abc", getProp( gosu.get( 0 ).getInstance(), "Value" ) );
  }

  //## todo: kill this test after we remove old-style gosu annotations
  public void testExceptionsThrownInAnnotationsAreNotIgnored()
  {
    IType type = TypeSystem.getByFullName( PKG_PREFIX + "SampleBadAnnotationHolder" );

    // Deprecated annotation is ok and should work w/o exception
    List<IAnnotationInfo> infoList = type.getTypeInfo().getMethod( "hasBadAnnotations" ).getAnnotationsOfType( TypeSystem.get( gw.lang.Deprecated.class ) );
    IAnnotationInfo iAnnotationInfo = infoList.get( 0 );
    gw.lang.Deprecated anno = (gw.lang.Deprecated)iAnnotationInfo.getInstance();
    assertEquals( gw.lang.Deprecated.class, anno.annotationType() );

    // AnnotationThatThrowsInConstructor annotation throws in it's ctor and should fail
    infoList = type.getTypeInfo().getMethod( "hasBadAnnotations" ).getAnnotationsOfType( TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.annotations.AnnotationThatThrowsInConstructor" ) );
    iAnnotationInfo = infoList.get( 0 );
    try
    {
      iAnnotationInfo.getInstance();
      fail( "Should have caused an exception" );
    }
    catch( RuntimeException e )
    {
      assertEquals( "I hate method overloading", e.getMessage() );
    }
  }

  public void testAnnotationsWorkOnInterfaces()
  {
    IType type = TypeSystem.getByFullName( PKG_PREFIX + "SampleAnnotatedInterface" );

    //class
    List<IAnnotationInfo> infoList = type.getTypeInfo().getAnnotationsOfType( TypeSystem.get( gw.lang.Deprecated.class ) );
    assertEquals( 1, infoList.size() );
    assertEquals( "foo", ((gw.lang.Deprecated)infoList.get( 0 ).getInstance()).value() );

    //method
    infoList = type.getTypeInfo().getMethod("hasAnnotations").getAnnotationsOfType( TypeSystem.get( gw.lang.Deprecated.class ) );
    assertEquals( 1, infoList.size() );
    assertEquals( "bar", ((gw.lang.Deprecated)infoList.get( 0 ).getInstance()).value() );
  }

  public void testAnnotationInfoMergesInheritedInformationCorrectly() {
    IType type = TypeSystem.get( JavaSubWithAnnotations.class );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo() );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getConstructor() );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getProperty( "PropertyWithAnnotation" ) );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getMethod( "methodWithAnnotation" ) );

    type = TypeSystem.getByFullName( PKG_PREFIX + "GosuSubWithAnnotations" );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo() );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getConstructor() );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getProperty( "PropertyWithAnnotation" ) );
    assertInheritedJavaAnnotationHasValue( 42, type.getTypeInfo().getMethod( "methodWithAnnotation" ) );
  }

  private void assertInheritedJavaAnnotationHasValue( int value, IFeatureInfo featureInfo )
  {
    IAnnotationInfo a = ((IAttributedFeatureInfo)featureInfo).getAnnotation( TypeSystem.get( InheritedJavaAnnotation.class ) );
    InheritedJavaAnnotation val = (InheritedJavaAnnotation)a.getInstance();
    assertEquals( value, val.value() );
  }

  private Map<IType, List> getAnnotationMapForInnerClass( Object o, String varName )
  {
    IType iType = TypeSystem.getFromObject( o );
    IGosuClassInternal innerClass = (IGosuClassInternal)TypeSystem.getByFullName( iType.getName() + "." + varName );
    assertTrue( innerClass.isValid() );
    List<IAnnotationInfo> annotations = innerClass.getTypeInfo().getAnnotations();
    Map<IType, List> m = new HashMap<IType, List>();
    for( IAnnotationInfo ai: annotations ) {
      List annos = m.get( ai.getType() );
      if( annos == null ) {
        annos = new ArrayList();
        m.put( ai.getType(), annos );
      }
      annos.add( ai.getInstance() );
    }
    return m;

  }

  private Map<IType, List<IAnnotationInfo>> getAnnotationMapForInnerClassThroughTypeInfo( Object o, String varName )
  {
    IType iType = TypeSystem.getFromObject( o );
    IType innerClass = TypeSystem.getByFullName( iType.getName() + "." + varName );
    return AnnotationUtil.map( innerClass.getTypeInfo().getAnnotations() );
  }

  private Map<IType, List> getAnnotationMapForVar( Object o, String varName )
  {
    IType iType = TypeSystem.getFromObject( o );
    IAnnotatedFeatureInfo fi = ((IRelativeTypeInfo)iType.getTypeInfo()).getProperty( iType, varName );
    return getAnnotationsByTypeForFeature( fi );
  }

  private Map<IType, List> getAnnotationMapForConstructor( Object o, int index )
  {
    IGosuClassInternal iType = (IGosuClassInternal)TypeSystem.getFromObject( o );
    int i = 0;
    iType.isValid();
    for( DynamicFunctionSymbol dfs: iType.getParseInfo().getConstructorFunctions().values() )
    {
      if( i++ == index )
      {
        IAnnotatedFeatureInfo fi = dfs.getMethodOrConstructorInfo();
        return getAnnotationsByTypeForFeature( fi );
      }
    }
    throw new IllegalStateException( "Didn't find ctor at index " + index );
  }

  private Map<IType, List<IAnnotationInfo>> getAnnotationMapForConstructorThroughTypeInfo( Object o, int i )
  {
    IType type = TypeSystem.getFromObject( o );
    String paramTypeName = type.getName() + ".Class" + i;
    return AnnotationUtil.map( type.getTypeInfo().getConstructor( TypeSystem.getByFullName( paramTypeName ) ).getAnnotations() );
  }

  private Map<IType, List> getAnnotationMapForProperty( Object o, String propName  )
  {
    IType iType = TypeSystem.getFromObject( o );
    IAnnotatedFeatureInfo fi = ((IRelativeTypeInfo)iType.getTypeInfo()).getProperty( iType, propName );
    return getAnnotationsByTypeForFeature( fi );
  }

  private Map<IType, List<IAnnotationInfo>> getAnnotationMapForPropertyThroughTypeInfo( Object o, String propName  )
  {
    IType iType = TypeSystem.getFromObject( o );
    return AnnotationUtil.map( iType.getTypeInfo().getProperty( propName ).getAnnotations() );
  }

  private Map<IType, List> getAnnotationMapForMethod( Object o, String funcName, IType... args )
  {
    IType iType = TypeSystem.getFromObject( o );
    List<IAnnotationInfo> annotations = iType.getTypeInfo().getMethod( funcName, args ).getAnnotations();
    Map<IType, List> m = new HashMap<IType, List>();
    for( IAnnotationInfo ai: annotations ) {
      List annos = m.get( ai.getType() );
      if( annos == null ) {
        annos = new ArrayList();
        m.put( ai.getType(), annos );
      }
      annos.add( ai.getInstance() );
    }
    return m;
  }

  private Map<IType, List<IAnnotationInfo>> getAnnotationMapForMethodThroughTypeInfo( Object o, String funcName, IType... args )
  {
    IType iType = TypeSystem.getFromObject( o );
    return AnnotationUtil.map( iType.getTypeInfo().getMethod( funcName, args ).getAnnotations() );
  }

  @DisableStringLiteralTemplates
  public static Map<IType, List> map2(List annotations) {
    HashMap<IType, List> map = new HashMap<IType, List>();
    for (Object annotation : annotations) {
      IType type;
      if (annotation.getClass().getName().contains("$Proxy")) {
        type = TypeSystem.get(annotation.getClass().getInterfaces()[0]);
      } else {
        type = TypeSystem.getFromObject(annotation);
      }

      List infoList = map.get(type);
      if (infoList == null) {
        infoList = new ArrayList<IAnnotationInfo>();
        map.put(type, infoList);
      }
      infoList.add(annotation);
    }
    return map;
  }

  private Map<IType, List> getAnnotationsByTypeForFeature( IAnnotatedFeatureInfo o )
  {
    IType iType = TypeSystem.getFromObject( o );
    List<IAnnotationInfo> annotations = o.getAnnotations();
    Map<IType, List> m = new HashMap<IType, List>();
    for( IAnnotationInfo ai: annotations ) {
      List annos = m.get( ai.getType() );
      if( annos == null ) {
        annos = new ArrayList();
        m.put( ai.getType(), annos );
      }
      annos.add( ai.getInstance() );
    }
    return m;
  }

  Object newStaticInnerAnnotationHolder()
  {
    return constructFromGosuClassloader( PKG_PREFIX + "SampleAnnotationHolder.StaticInnerAnnotationHolder" );
  }

  Object newInnerAnnotationHolder()
  {
    Object o = constructFromGosuClassloader( PKG_PREFIX + "SampleAnnotationHolder" );
    try
    {
      return o.getClass().getMethod( "getAnnotatedInnerClass" ).invoke( o );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  Object newAnonymousInnerAnnotationHolder()
  {
    Object o = constructFromGosuClassloader( PKG_PREFIX + "SampleAnnotationHolder" );
    try
    {
      return o.getClass().getMethod( "getAnnotatedAnonymousInnerClass" ).invoke( o );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  Object newSampleAnnotationHolder()
  {
    return constructFromGosuClassloader( PKG_PREFIX + "SampleAnnotationHolder" );
  }

  Object newSampleBadAnnotationHolder()
  {
    return constructFromGosuClassloader( PKG_PREFIX + "SampleBadAnnotationHolder" );
  }

  private Object getProp( Object o, String propName )
  {
    try
    {
      Method method = o.getClass().getMethod( "get" + propName );
      return method.invoke( o );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}