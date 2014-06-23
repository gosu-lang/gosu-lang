/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.Deprecated;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaPropertyDescriptor;

public class JavaSourcePropertyDescriptor implements IJavaPropertyDescriptor {
  private String _name;
  private IJavaClassInfo _type;
  private IJavaClassMethod _readMethod;
  private IJavaClassMethod _writeMethod;

  public JavaSourcePropertyDescriptor(String propName, IJavaClassInfo type, IJavaClassMethod getter, IJavaClassMethod setter) {
    _name = propName;
    _type = type;
    _readMethod = getter;
    _writeMethod = setter;
  }

  @Override
  public String getDisplayName() {
    return _name;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public IType getPropertyType() {
    return _type.getJavaType();
  }

  @Override
  public IJavaClassInfo getPropertyClassInfo() {
    return _type;
  }

  @Override
  public IJavaClassMethod getReadMethod() {
    return _readMethod;
  }

  @Override
  public String getShortDescription() {
    return _name;
  }

  @Override
  public IJavaClassMethod getWriteMethod() {
    return _writeMethod;
  }

  @Override
  public boolean isDeprecated() {
    return (_readMethod != null && (_readMethod.isAnnotationPresent(Deprecated.class) || _readMethod.isAnnotationPresent(java.lang.Deprecated.class))) ||
        (_writeMethod != null && (_writeMethod.isAnnotationPresent(Deprecated.class) || _writeMethod.isAnnotationPresent(java.lang.Deprecated.class)));
  }

  @Override
  public boolean isHidden() {
    return false;
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
