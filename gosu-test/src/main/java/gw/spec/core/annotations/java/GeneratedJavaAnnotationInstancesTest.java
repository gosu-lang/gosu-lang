/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import gw.test.TestClass;
import gw.lang.*;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.*;
import java.util.*;
import gw.internal.gosu.parser.GosuClass;
import java.lang.annotation.Annotation;

public class GeneratedJavaAnnotationInstancesTest extends TestClass {

  public void test_simple_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_class_retention_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_class_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_class_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );  }

  public void test_simple_string_class_retention_string_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_internal_").getAnnotations();
    assertEquals( 1, annotations.length );  }

  public void test_simple_string_class_retention_string_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_class_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_class_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string__static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string__")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_private_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_private_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_internal_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_class_retention_string_protected_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_class_retention_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_class_retention_string_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_class_retention_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_source_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_source_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );  }

  public void test_simple_string_source_retention_string_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_internal_").getAnnotations();
    assertEquals( 1, annotations.length );  }

  public void test_simple_string_source_retention_string_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_source_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string__static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string__").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_public_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_public_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_private_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_private_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_internal_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_internal_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_protected_static").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_source_retention_string_protected_").getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string__static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string__")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_private_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_private_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_internal_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_source_retention_string_protected_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_source_retention_string_public_static")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_string_source_retention_string_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_source_retention_string_public_")).getBackingClass().getAnnotations();
    assertEquals( 0, annotations.length );  }

  public void test_simple_int__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_int_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_int_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_fully_qualified_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_fully_qualified_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_fully_qualified_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_fully_qualified_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_simple_class__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_class_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_class_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_simple_annotation__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_annotation_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_annotation_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_annotation_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_simple_string_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_explicit_new_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_explicit_new_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_explicit_new_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_explicit_new_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_explicit_new_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_explicit_new_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_explicit_new_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_explicit_new_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_string_array_no_values_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_string_array_no_values_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_string_array_no_values_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_string_array_no_values_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_string_array_no_values_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_no_values_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_string_array_no_values_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_string_array_no_values_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_int_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_int_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_int_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_int_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_int_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_int_array_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_int_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_int_array_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_int_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_simple_enum_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_enum_array_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_enum_array_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_enum_array_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_enum_array_fully_qualified_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_enum_array_fully_qualified_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_array_fully_qualified_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_enum_array_fully_qualified_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_enum_array_fully_qualified_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_class_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_class_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_class_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_class_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_class_array_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_class_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_class_array_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_class_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_simple_annotation_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "func_simple_annotation_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getprop_simple_annotation_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array__static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array___var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_internal_static").getAnnotations();
    assertEquals( 2, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_internal_").getAnnotations();
    assertEquals( 2, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected_static_var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected__var() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = gsClass.getBackingClass().getDeclaredField("var_simple_annotation_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array__static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array__static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array___var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array__").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_public_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_public_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_private_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_private_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_internal_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_internal_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected_static_var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_protected_static").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected__var_w_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = JavaAnnotationUtil.getMethod(gsClass, "getvar_prop_simple_annotation_array_protected_").getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array__static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array__static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array___class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array__")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_private_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_private__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_private_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_internal_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_internal__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_internal_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected_static_class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_protected_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_protected__class() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Class_simple_annotation_array_protected_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public_static_iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_annotation_array_public_static")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_simple_annotation_array_public__iface() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClass" );
    Annotation[] annotations = ((IGosuClass) gsClass.getInnerClass("Interface_simple_annotation_array_public_")).getBackingClass().getAnnotations();
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

}