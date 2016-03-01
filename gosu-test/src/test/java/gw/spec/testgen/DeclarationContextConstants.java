/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:34:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DeclarationContextConstants {
  String gosuClass();

  String gosuClassInnerClass();

  String gosuClassEnhancement();

  String gosuClassSubclass();

  String gosuClassSubclassInnerClass();
  
  String gosuClassSubclassStaticInnerClass();

  String gosuClassSubclassInOtherPackage();

  String unrelatedClass();

  String unrelatedEnhancement();

  String gosuClassStaticInnerClass();

  String gosuClassSubclassEnhancement();

  String gosuClassOtherEnhancement();

  String gosuInterface();

  String gosuInterfaceImplementor();

  String javaClass();

  String javaClassEnhancement();

  String javaClassSubclass();

  String javaClassSubclassInOtherPackage();

  String javaClassSubclassInnerClass();

  String javaInterface();

  String javaClassWrappedByPureGosuType();

  String javaClassWrappedByJavaBackedType();

}
