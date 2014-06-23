/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.test.TestClass;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.*;
import java.util.*;

public class GeneratedCoreAnnotationInstancesOnEnhancementThroughEnhancedTest extends TestClass {

  private Map<IType, List<IAnnotationInfo>> map(List<IAnnotationInfo> annotations) {
    HashMap<IType, List<IAnnotationInfo>> map = new HashMap<IType, List<IAnnotationInfo>>();
    for (IAnnotationInfo annotation : annotations) {
      List<IAnnotationInfo> infoList = map.get(annotation.getType());
      if (infoList == null) {
        infoList = new ArrayList<IAnnotationInfo>();
        map.put(annotation.getType(), infoList);
      }
      infoList.add(annotation);
    }
    return map;
  }
  public void test_enhancement_through_enhanced_gosu_deprecated_relative__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_relative_comment_before_and_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_fully_qualified_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_w_text_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_deprecated_in_comment_multi_line_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_no_parens_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_deprecated_fully_qualified_w_parens_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_no_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_java_local_relative_parens_with_arg_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_no_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_local_relative_parens_with_arg_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_no_comment_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after__static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after___enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_public_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_public__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_internal_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_internal__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_protected_static_enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_protected__enhancement").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_before_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_between_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation__static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation___enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_public_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_public__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_internal_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_internal__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_protected_static_enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_enhancement_through_enhanced_gosu_multi_comment_after_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_protected__enhancement").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

}