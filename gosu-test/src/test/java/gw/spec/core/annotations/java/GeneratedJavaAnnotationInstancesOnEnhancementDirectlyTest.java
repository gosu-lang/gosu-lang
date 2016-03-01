/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

import java.lang.annotation.Annotation;

public class GeneratedJavaAnnotationInstancesOnEnhancementDirectlyTest extends TestClass {

  public void test_enhancement_directly_simple_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( "foo", ((SingleStringAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_class_retention_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string__static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string___enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_public_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_public__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_private_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_private__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_internal_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_internal__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_protected_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_class_retention_string_protected__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string__static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string___enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_public_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_public__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_private_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_private__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_internal_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_internal__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_protected_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_class_retention_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_class_retention_string_protected__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string__static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string___enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_public_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_public__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_private_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_private__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_internal_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_internal__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_protected_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_source_retention_string_protected__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string__static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string___enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_public_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_public__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_private_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_private__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_internal_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_internal__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_protected_static_enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_string_source_retention_string_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_source_retention_string_protected__enhancement" );
    assertEquals( 0, annotations.length );  }

  public void test_enhancement_directly_simple_int__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_fully_qualified_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_fully_qualified_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_fully_qualified_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_annotation__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation__static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation___enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_public__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_private__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_internal__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_protected__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation__static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation___enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_public__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_private__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_internal__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_annotation_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_protected__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa.value() );
  }

  public void test_enhancement_directly_simple_string_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_explicit_new_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_explicit_new_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_explicit_new_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[]{"foo", "bar"}, ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_string_array_no_values_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_string_array_no_values_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_string_array_no_values_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_int_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_int_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_int_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_int_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );
    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );
  }

  public void test_enhancement_directly_simple_enum_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_enum_array_fully_qualified_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_enum_array_fully_qualified_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_enum_array_fully_qualified_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_class_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array___enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_class_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_class_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );
  }

  public void test_enhancement_directly_simple_annotation_array__static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array___func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array___enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_public_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_public__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_private_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_private__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_internal_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_internal__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_protected_static_func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_protected__func() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "func_simple_annotation_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array__static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array__static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array___prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array___enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_public_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_public_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_public__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_public__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_private_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_private_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_private__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_private__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_internal_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_internal_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_internal__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_internal__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_protected_static_prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_protected_static_enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

  public void test_enhancement_directly_simple_annotation_array_protected__prop() throws Exception {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( "gw.spec.core.annotations.java.GeneratedJavaAnnotationHolderClassEnhancement" );
    Annotation[] annotations = JavaAnnotationUtil.getMethodAnnotations( gsClass, "getprop_simple_annotation_array_protected__enhancement" );
    assertEquals( 1, annotations.length );
    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();
    assertEquals( "foo",  ssa[0].value() );
    assertEquals( "bar",  ssa[1].value() );
  }

}