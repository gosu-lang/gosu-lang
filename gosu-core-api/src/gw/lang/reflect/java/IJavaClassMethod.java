/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface IJavaClassMethod extends IJavaAnnotatedElement, Comparable<IJavaClassMethod>, Serializable {

  IJavaClassInfo getReturnClassInfo();

  IJavaClassType getGenericReturnType();

  String getReturnTypeName();

  IType getReturnType();


  IJavaClassType[] getGenericParameterTypes();

  IJavaClassInfo[] getParameterTypes();


  String getName();

  int getModifiers();

  IJavaClassInfo[] getExceptionTypes();

  Object getDefaultValue();

  Object invoke(Object ctx, Object[] args) throws InvocationTargetException, IllegalAccessException;

  IGenericTypeVariable[] getTypeVariables(IJavaMethodInfo javaMethodInfo);

  boolean isSynthetic();

  boolean isBridge();
}
