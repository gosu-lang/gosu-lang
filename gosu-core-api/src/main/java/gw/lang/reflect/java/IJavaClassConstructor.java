/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface IJavaClassConstructor extends IJavaAnnotatedElement, Serializable {
  IJavaClassInfo[] getExceptionTypes();

  int getModifiers();

  IParameterInfo[] convertGenericParameterTypes(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars);

  IJavaClassInfo[] getParameterTypes();

  Object newInstance(Object... objects) throws InvocationTargetException, IllegalAccessException, InstantiationException;

  boolean isDefault();
}
