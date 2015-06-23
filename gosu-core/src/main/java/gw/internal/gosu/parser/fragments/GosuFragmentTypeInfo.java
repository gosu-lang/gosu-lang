/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.fragments;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodList;

import java.util.Collections;
import java.util.List;

public class GosuFragmentTypeInfo implements IRelativeTypeInfo {
  private GosuFragment _owner;

  public GosuFragmentTypeInfo(GosuFragment fragment) {
    _owner = fragment;
  }

  @Override
  public List<IAnnotationInfo> getAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public List<IAnnotationInfo> getAnnotationsOfType(IType type) {
    return Collections.emptyList();
  }

  @Override
  public boolean hasAnnotation(IType type) {
    return false;
  }

  @Override
  public boolean isDeprecated() {
    return false;
  }

  @Override
  public String getDeprecatedReason() {
    return null;
  }

  @Override
  public boolean isDefaultImpl() {
    return false;
  }

  @Override
  public IFeatureInfo getContainer() {
    return null;
  }

  @Override
  public IType getOwnersType() {
    return _owner;
  }

  @Override
  public String getName() {
    return _owner.getName();
  }

  @Override
  public String getDisplayName() {
    return _owner.getName();
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public List<? extends IPropertyInfo> getProperties() {
    return Collections.emptyList();
  }

  @Override
  public IPropertyInfo getProperty(CharSequence propName) {
    return null;
  }

  @Override
  public MethodList getMethods() {
    return MethodList.EMPTY;
  }

  @Override
  public IMethodInfo getMethod(CharSequence methodName, IType... params) {
    return null;
  }

  @Override
  public IMethodInfo getCallableMethod(CharSequence method, IType... params) {
    return null;
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors() {
    return Collections.emptyList();
  }

  @Override
  public IConstructorInfo getConstructor(IType... params) {
    return null;
  }

  @Override
  public IConstructorInfo getCallableConstructor(IType... params) {
    return null;
  }

  @Override
  public List<? extends IEventInfo> getEvents() {
    return Collections.emptyList();
  }

  @Override
  public IEventInfo getEvent(CharSequence event) {
    return null;
  }

  @Override
  public Accessibility getAccessibilityForType(IType whosaskin) {
    if (whosaskin == _owner) {
      return Accessibility.PRIVATE;
    } else {
      return Accessibility.PUBLIC;
    }
  }

  @Override
  public List<? extends IPropertyInfo> getProperties(IType whosaskin) {
    return Collections.emptyList();
  }

  @Override
  public IPropertyInfo getProperty(IType whosaskin, CharSequence propName) {
    return null;
  }

  @Override
  public MethodList getMethods(IType whosaskin) {
    return MethodList.EMPTY;
  }

  @Override
  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    return null;
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    return Collections.emptyList();
  }

  @Override
  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return null;
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return false;
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return Collections.emptyList();
  }

}
