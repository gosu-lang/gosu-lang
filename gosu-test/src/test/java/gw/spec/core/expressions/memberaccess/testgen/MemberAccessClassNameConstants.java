/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.memberaccess.testgen;

import gw.spec.testgen.DeclarationContextConstants;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:48:39 PM
 * To change this template use File | Settings | File Templates.
 */
class MemberAccessClassNameConstants implements DeclarationContextConstants {

  public static final MemberAccessClassNameConstants INSTANCE = new MemberAccessClassNameConstants();

  private MemberAccessClassNameConstants() { }

  @Override
  public String gosuClass() {
    return "MemberAccess_DeclaringGosuClass";
  }

  @Override
  public String unrelatedClass() {
    return "MemberAccess_UnrelatedClass";
  }

  public String unrelatedEnhancement() {
    return "MemberAccess_UnrelatedEnhancement";
  }

  @Override
  public String gosuClassInnerClass() {
    return "InnerClass";
  }

  @Override
  public String gosuClassEnhancement() {
    return "MemberAccess_DeclaringClassEnhancement";
  }

  @Override
  public String gosuClassSubclass() {
    return "MemberAccess_DeclaringClassSubclass";
  }

  @Override
  public String gosuClassSubclassInOtherPackage() {
    return "MemberAccess_DeclaringClassSubclassInOtherPackage";
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
    return "MemberAccess_GosuSubclassEnhancement";
  }

  public String gosuClassOtherEnhancement() {
    return "MemberAccess_OtherEnhancement";
  }

  public String gosuInterface() {
    return "MemberAccess_DeclaringGosuInterface";
  }

  public String gosuInterfaceImplementor() {
    return "MemberAccess_DeclaringGosuInterfaceImplementor";
  }

  public String javaClass() {
    return "MemberAccess_JavaDeclaringClass";
  }

  @Override
  public String javaClassEnhancement() {
    return "MemberAccess_DeclaringJavaClassEnhancement";
  }

  @Override
  public String javaClassSubclass() {
    return "MemberAccess_JavaSubclass";
  }

  @Override
  public String javaClassSubclassInOtherPackage() {
    return "MemberAccess_JavaSubclassInOtherPackage";
  }

  @Override
  public String javaClassSubclassInnerClass() {
    return "SubclassInnerClass";
  }

  @Override
  public String javaInterface() {
    return "MemberAccess_DeclaringJavaInterface";
  }

  @Override
  public String javaClassWrappedByPureGosuType() {
    return "MemberAccess_WrappedJavaClass";
  }

  @Override
  public String javaClassWrappedByJavaBackedType() {
    return "MemberAccess_WrappedJavaClassByJavaBackedType";
  }
}
