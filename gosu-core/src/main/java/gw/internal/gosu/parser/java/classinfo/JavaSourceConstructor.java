/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.MethodTree;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.java.IJavaClassConstructor;

import java.lang.reflect.InvocationTargetException;

public class JavaSourceConstructor extends JavaSourceMethod implements IJavaClassConstructor {

  public JavaSourceConstructor(MethodTree method, JavaSourceType containingClass) {
    super(method, containingClass);
  }

  public boolean isConstructor() {
    return true;
  }

  @Override
  public IParameterInfo[] convertGenericParameterTypes(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName ) {
    return getActualParameterInfos(container, actualParamByVarName, true);
  }

  @Override
  public Object newInstance(Object... objects) throws InvocationTargetException, IllegalAccessException, InstantiationException {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean isDefault() {
    return false;
  }
}
