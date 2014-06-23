/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.test.TestClass;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.*;
import java.util.*;

public class GeneratedCoreAnnotationInstancesTest extends TestClass {

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
  public void test_gosu_deprecated_relative__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_after_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_relative_comment_before_and_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_relative_comment_before_and_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_relative_comment_before_and_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_relative_comment_before_and_after_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_relative_comment_before_and_after_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_before_and_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_relative_comment_before_and_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_fully_qualified_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_fully_qualified_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_fully_qualified_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_fully_qualified_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_fully_qualified_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_fully_qualified_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_fully_qualified_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_w_text_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_w_text_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_w_text_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_w_text_before_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_w_text_before_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_w_text_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_w_text_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_deprecated_in_comment_multi_line_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_deprecated_in_comment_multi_line_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_deprecated_in_comment_multi_line_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_deprecated_in_comment_multi_line_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_deprecated_in_comment_multi_line_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_multi_line_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_deprecated_in_comment_multi_line_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_deprecated_fully_qualified_no_parens__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_no_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_no_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_no_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_no_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_no_parens_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_deprecated_fully_qualified_no_parens_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_deprecated_fully_qualified_no_parens_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_deprecated_fully_qualified_w_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_deprecated_fully_qualified_w_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_deprecated_fully_qualified_w_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_deprecated_fully_qualified_w_parens_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_deprecated_fully_qualified_w_parens_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_deprecated_fully_qualified_w_parens_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_deprecated_fully_qualified_w_parens_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_local_relative_no_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_no_parens_no_args_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_no_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_no_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_no_args_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_java_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_java_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_java_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_java_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_java_local_relative_parens_with_arg_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_parens_with_arg_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_java_local_relative_parens_with_arg_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_local_relative_no_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_no_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_no_parens_no_args_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_no_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_no_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_no_args_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_no_args_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_parens_no_args_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_parens_no_args_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_local_relative_parens_with_arg_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_local_relative_parens_with_arg_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_parens_with_arg_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_local_relative_parens_with_arg_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_no_comment_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_no_comment_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_no_comment_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_no_comment_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment__static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment__").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_private_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_private_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_internal_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_no_comment_protected_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_no_comment_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_no_comment_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before__static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before__").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_private_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_private_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_internal_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_protected_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_before_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_before_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between__static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between__").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_private_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_private_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_internal_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_protected_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_between_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_between_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after__static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after__").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_public_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_public_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_private_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_private_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_internal_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_internal_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_protected_static").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_protected_").getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after__static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after__").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_private_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_private_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_internal_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_protected_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_after_public_static").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_after_public_").getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_before_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_before_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_before_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_before_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_before_w_annotation_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_before_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_before_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_between_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_between_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_between_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_between_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_between_w_annotation_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_between_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_between_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation__static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation___func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected_static_func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected__func(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getMethod(gsClass, "func_gosu_multi_comment_after_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation__static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation___prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected_static_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected__prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "prop_gosu_multi_comment_after_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation__static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation___var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected_static_var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected__var(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_gosu_multi_comment_after_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation__static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation__static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation___var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation__").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_public_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_public_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_private_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_private_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_internal_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_internal_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected_static_var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_protected_static").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected__var_w_prop(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getTypeInfo().getProperty(gsClass, "var_prop_gosu_multi_comment_after_w_annotation_protected_").getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation__static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation__static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation___class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation__").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_private_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_private__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_private_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_internal_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_internal_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected_static_class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_protected_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_protected__class(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Class_gosu_multi_comment_after_w_annotation_protected_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public_static_iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_after_w_annotation_public_static").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public__iface(){
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.instances.GeneratedAnnotationHolderClass" );
    Map<IType, List<IAnnotationInfo>> map = map(gsClass.getInnerClass("Interface_gosu_multi_comment_after_w_annotation_public_").getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

}