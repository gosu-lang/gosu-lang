/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.BeanInfoUtil;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.module.IModule;

import java.beans.PropertyDescriptor;

public class PropertyDescriptorJavaPropertyDescriptor implements IJavaPropertyDescriptor {
  private PropertyDescriptor _pd;
  private IModule _module;

  public PropertyDescriptorJavaPropertyDescriptor(PropertyDescriptor propertyDescriptor, IModule module) {
    _pd = propertyDescriptor;
    _module = module;
  }

  @Override
  public String getName() {
    return _pd.getName();
  }

  @Override
  public IJavaClassMethod getReadMethod() {
    return _pd.getReadMethod() == null ? null : new MethodJavaClassMethod(_pd.getReadMethod(), _module);
  }

  @Override
  public IJavaClassMethod getWriteMethod() {
    return _pd.getWriteMethod() == null ? null : new MethodJavaClassMethod(_pd.getWriteMethod(), _module);
  }

  @Override
  public IType getPropertyType() {
    return TypeSystem.get(_pd.getPropertyType());
  }

  @Override
  public IJavaClassInfo getPropertyClassInfo() {
    return JavaSourceUtil.getClassInfo(_pd.getPropertyType(), _module);
  }

  @Override
  public boolean isHidden() {
    return _pd.isHidden();
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return BeanInfoUtil.isVisible(_pd, constraint);
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return _pd.isHidden();
  }

  @Override
  public boolean isDeprecated() {
    return _pd.getReadMethod().isAnnotationPresent( gw.lang.Deprecated.class ) ||
              BeanInfoUtil.isDeprecated( _pd ) ||
              (_pd.getReadMethod() != null && _pd.getReadMethod().isAnnotationPresent( java.lang.Deprecated.class ));
  }

  @Override
  public String getDisplayName() {
    return _pd.getDisplayName();
  }

  @Override
  public String getShortDescription() {
    return _pd.getShortDescription();
  }
}