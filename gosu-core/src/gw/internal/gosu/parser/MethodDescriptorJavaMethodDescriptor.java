/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaParameterDescriptor;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.BeanInfoUtil;
import gw.lang.reflect.module.IModule;

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;

public class MethodDescriptorJavaMethodDescriptor implements IJavaMethodDescriptor {
  private GWMethodDescriptor _md;
  private Method _method;
  private IModule _module;

  public MethodDescriptorJavaMethodDescriptor(GWMethodDescriptor md, IModule module) {
    _md = md;
    _method = md.getMethod();
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
    return _md.getName();
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return _md.isHidden();
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _md.isVisible(constraint);
  }
}