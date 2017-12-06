/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Method;

public class MethodDescriptorJavaMethodDescriptor implements IJavaMethodDescriptor {
  private Method _method;
  private IModule _module;

  public MethodDescriptorJavaMethodDescriptor(Method method, IModule module) {
    _method = method;
    if( _method == null ) {
      throw new IllegalStateException( "MethodDescriptor without method." );
    }
    _module = module;
  }

  private MethodJavaClassMethod javaMethod;

  @Override
  public IJavaClassMethod getMethod() {
    if (javaMethod == null) {
      javaMethod = new MethodJavaClassMethod(_method, _module);
    }
    return javaMethod;
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
}