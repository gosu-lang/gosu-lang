/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaMethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public class SyntheticJavaMethod implements IJavaClassMethod {

  private IJavaClassInfo _enclosingClass;
  private IJavaClassInfo _returnClassInfo;
  private IJavaClassType _returnType;
  private String _name;
  private int _modifiers;
  private IJavaClassInfo[] _params = new IJavaClassInfo[0];
  private IJavaClassInfo[] _exceptions = _params;

  public SyntheticJavaMethod(IJavaClassInfo enclosingclass, 
                             IJavaClassInfo returnclassinfo, 
                             IJavaClassType returntype, 
                             String name, 
                             int modifiers, 
                             IJavaClassInfo[] params, 
                             IJavaClassInfo[] exceptions) {
    _enclosingClass = enclosingclass;
    _returnClassInfo = returnclassinfo;
    _returnType = returntype;
    _name = name;
    _modifiers = modifiers;
    _params = params;
    _exceptions = exceptions;
  }

  @Override
  public IJavaClassInfo getReturnClassInfo() {
    return _returnClassInfo;
  }

  @Override
  public IJavaClassType getGenericReturnType() {
    return _returnType;
  }

  @Override
  public String getReturnTypeName() {
    return _returnType.getName();
  }

  @Override
  public IType getReturnType() {
    return TypeSystem.getByFullName(_returnType.getName());
  }

  @Override
  public IJavaClassType[] getGenericParameterTypes() {
    return _params;
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    return _params;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public int getModifiers() {
    return _modifiers;
  }

  @Override
  public IJavaClassInfo[] getExceptionTypes() {
    return _exceptions;
  }

  @Override
  public Object getDefaultValue() {
    return null;
  }

  @Override
  public Object invoke(Object ctx, Object[] args) throws InvocationTargetException, IllegalAccessException {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables(IJavaMethodInfo javaMethodInfo) {
    return new IGenericTypeVariable[0];
  }

  @Override
  public boolean isSynthetic() {
    return true;
  }

  @Override
  public boolean isBridge() {
    return false;
  }

  @Override
  public int compareTo(IJavaClassMethod o) {
    return getName().compareTo(o.getName());
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return false;
  }

  @Override
  public IAnnotationInfo getAnnotation(Class<? extends Annotation> annotationClass) {
    return null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return new IAnnotationInfo[0];
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return _enclosingClass;
  }
}
