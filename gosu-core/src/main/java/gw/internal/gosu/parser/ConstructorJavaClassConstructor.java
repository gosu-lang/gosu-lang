/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.*;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

public class ConstructorJavaClassConstructor implements IJavaClassConstructor, IJavaClassBytecodeConstructor {
  private Constructor _ctor;
  private IModule _module;

  public ConstructorJavaClassConstructor(Constructor ctor, IModule module) {
    _ctor = ctor;
    _module = module;
  }

  public void setAccessible(boolean accessible) {
    _ctor.setAccessible(accessible);
  }

  public Class getDeclaringJavaClass() {
    return _ctor.getDeclaringClass();
  }

  @Override
  public IJavaClassInfo[] getExceptionTypes() {
    Class[] rawTypes = _ctor.getExceptionTypes();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      types[i] = JavaSourceUtil.getClassInfo(rawTypes[i], _module);
    }
    return types;
  }

  @Override
  public int getModifiers() {
    return _ctor.getModifiers();
  }

  @Override
  public boolean isSynthetic() {
    return _ctor.isSynthetic();
  }

  @Override
  public IParameterInfo[] convertGenericParameterTypes(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName) {
    return JavaMethodInfo.convertGenericParameterTypes(container, actualParamByVarName, getGenericParameterTypes(), getEnclosingClass());
  }

  private IJavaClassType[] getGenericParameterTypes() {
    Type[] rawTypes = _ctor.getGenericParameterTypes();
    IJavaClassType[] types = new IJavaClassType[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      types[i] = TypeJavaClassType.createType(rawTypes[i], _module);
    }
    return types;
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    Class[] rawParamTypes = getJavaParameterTypes();
    IJavaClassInfo[] paramTypes = new IJavaClassInfo[rawParamTypes.length];
    for (int i = 0; i < rawParamTypes.length; i++) {
      paramTypes[i] = JavaSourceUtil.getClassInfo(rawParamTypes[i], _module);
    }
    return paramTypes;
  }

  public Class[] getJavaParameterTypes() {
    return _ctor.getParameterTypes();
  }

  public Object newInstance(Object[] objects) throws InvocationTargetException, IllegalAccessException, InstantiationException {
    return _ctor.newInstance(objects);
  }

  @Override
  public boolean isDefault() {
    return false;
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return _ctor.isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    Annotation annotation = _ctor.getAnnotation(annotationClass);
    return annotation != null ? new ClassAnnotationInfo(annotation, this) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    Annotation[] annotations = _ctor.getDeclaredAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.length];
    for (int i = 0; i < declaredAnnotations.length; i++) {
      declaredAnnotations[i] = new ClassAnnotationInfo(annotations[i], this);
    }
    return declaredAnnotations;
  }

  public Constructor getJavaConstructor() {
    return _ctor;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo(_ctor.getDeclaringClass(), _module);
  }
}
