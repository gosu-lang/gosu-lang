/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.java.IJavaClassConstructor;

import java.lang.reflect.InvocationTargetException;

public class JavaSourceConstructor extends JavaSourceMethod implements IJavaClassConstructor {

  public JavaSourceConstructor(IJavaASTNode methodNode, JavaSourceType containingClass) {
    super(methodNode, containingClass);
  }

  public boolean isConstructor() {
    return true;
  }

  @Override
  public IParameterInfo[] convertGenericParameterTypes(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars) {
    return getActualParameterInfos(container, actualParamByVarName, bKeepTypeVars);
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
