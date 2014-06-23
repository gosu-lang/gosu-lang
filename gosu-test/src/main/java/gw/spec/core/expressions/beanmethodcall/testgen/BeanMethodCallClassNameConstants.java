/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.DeclarationContextConstants;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:48:39 PM
 * To change this template use File | Settings | File Templates.
 */
class BeanMethodCallClassNameConstants implements DeclarationContextConstants {

  public static final BeanMethodCallClassNameConstants INSTANCE = new BeanMethodCallClassNameConstants();

  private BeanMethodCallClassNameConstants() { }

  @Override
  public String gosuClass() {
    return "BeanMethodCall_GosuClass";
  }

  @Override
  public String unrelatedClass() {
    return "BeanMethodCall_UnrelatedClass";
  }

  public String unrelatedEnhancement() {
    return "BeanMethodCall_UnrelatedEnhancement";
  }

  @Override
  public String gosuClassInnerClass() {
    return "InnerClass";
  }

  @Override
  public String gosuClassEnhancement() {
    return "BeanMethodCall_GosuClassEnhancement";
  }

  @Override
  public String gosuClassSubclass() {
    return "BeanMethodCall_GosuClassSubclass";
  }

  @Override
  public String gosuClassSubclassInOtherPackage() {
    return "BeanMethodCall_GosuClassSubclassInOtherPackage";
  }

  @Override
  public String gosuClassSubclassInnerClass() {
    return "SubclassInnerClass";
  }

  @Override
  public String gosuClassSubclassStaticInnerClass() {
    return "SubclassStaticInnerClass";
  }

  @Override
  public String gosuClassStaticInnerClass() {
    return "StaticInnerClass";
  }

  @Override
  public String gosuClassSubclassEnhancement() {
    return "BeanMethodCall_GosuSubclassEnhancement";
  }

  public String gosuClassOtherEnhancement() {
    return "BeanMethodCall_OtherEnhancement";
  }

  public String gosuInterface() {
    return "BeanMethodCall_GosuInterface";
  }

  public String gosuInterfaceImplementor() {
    return "BeanMethodCall_GosuInterfaceImplementor";
  }

  public String javaClass() {
    return "BeanMethodCall_JavaClass";
  }

  public String javaClassWrappedByPureGosuType() {
    return "BeanMethodCall_WrappedJavaClass";
  }

  @Override
  public String javaClassWrappedByJavaBackedType() {
    return "BeanMethodCall_WrappedJavaClassByJavaBackedType";
  }

  @Override
  public String javaClassEnhancement() {
    return "BeanMethodCall_JavaClassEnhancement";
  }

  @Override
  public String javaClassSubclass() {
    return "BeanMethodCall_JavaSubclass";
  }

  @Override
  public String javaClassSubclassInOtherPackage() {
    return "BeanMethodCall_JavaClassSubclassInOtherPackage";
  }

  @Override
  public String javaClassSubclassInnerClass() {
    return "SubclassInnerClass";
  }

  @Override
  public String javaInterface() {
    return "BeanMethodCall_JavaInterface";
  }
}