/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodList;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 */
public class TypeInPackageTypeInfo extends BaseFeatureInfo implements ITypeInfo {
  private final TypeInPackageType _type;

  public TypeInPackageTypeInfo(TypeInPackageType type) {
    super(type);
    _type = type;
  }

  public boolean isStatic() {
    return false;
  }

  public String getName() {
    return _type.getName();
  }

  public List<? extends IPropertyInfo> getProperties() {
    return Collections.emptyList();
  }

  @Nullable
  public IPropertyInfo getProperty(CharSequence propName) {
    return null;
  }

  @Nullable
  public IMethodInfo getCallableMethod(CharSequence strMethod, IType... params) {
    return null;
  }

  @Nullable
  public IConstructorInfo getCallableConstructor(IType... params) {
    return null;
  }

  public MethodList/*<IMethodInfo>*/ getMethods() {
    return MethodList.EMPTY;
  }

  @Nullable
  public IMethodInfo getMethod(CharSequence methodName, IType... params) {
    return null;
  }

  public List/*<IConstructorInfo>*/ getConstructors() {
    return Collections.EMPTY_LIST;
  }

  @Nullable
  public IConstructorInfo getConstructor(IType... params) {
    return null;
  }

  public List/*<IEventInfo>*/ getEvents() {
    return Collections.EMPTY_LIST;
  }

  @Nullable
  public IEventInfo getEvent(CharSequence strEvent) {
    return null;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }
}
