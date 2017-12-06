/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaMethodDescriptor;

public class JavaSourceMethodDescriptor implements IJavaMethodDescriptor {

  private IJavaClassMethod _method;

  public JavaSourceMethodDescriptor(IJavaClassMethod method) {
    _method = method;
  }

  @Override
  public IJavaClassMethod getMethod() {
    return _method;
  }

  @Override
  public String getName() {
    return _method.getName();
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return false;
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return true;
  }

  public String toString() {
    return _method.toString();
  }

}
