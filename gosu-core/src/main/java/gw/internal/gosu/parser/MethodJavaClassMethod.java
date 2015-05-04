/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.*;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MethodJavaClassMethod implements IJavaClassMethod, IJavaClassBytecodeMethod {
  private Method _method;
  private IModule _module;

  public MethodJavaClassMethod(Method method, IModule module) {
    _method = method;
    _module = module;
  }

  @Override
  public IType getReturnType() {
    return TypeSystem.get(_method.getReturnType());
  }

  @Override
  public IJavaClassInfo getReturnClassInfo() {
    return JavaSourceUtil.getClassInfo( _method.getReturnType(), _module );
  }

  @Override
  public String getName() {
    return _method.getName();
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo( _method.getDeclaringClass(), _module );
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    Class<?>[] rawTypes = _method.getParameterTypes();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      types[i] = JavaSourceUtil.getClassInfo(rawTypes[i], _module);
    }
    return types;
  }

  @Override
  public int getModifiers() {
    return _method.getModifiers();
  }

  @Override
  public boolean isSynthetic() {
    return _method.isSynthetic();
  }

  @Override
  public boolean isBridge() {
    return _method.isBridge();
  }

  @Override
  public IJavaClassInfo[] getExceptionTypes() {
    Class<?>[] rawTypes = _method.getExceptionTypes();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      types[i] = JavaSourceUtil.getClassInfo(rawTypes[i], _module);
    }
    return types;
  }

  @Override
  public Object getDefaultValue() {
    return _method.getDefaultValue();
  }

  @Override
  public String getReturnTypeName() {
    return _method.getReturnType().getName();
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return _method.isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    Annotation annotation = _method.getAnnotation(annotationClass);
    return annotation != null ? new ClassAnnotationInfo(annotation, this) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    Annotation[] annotations = _method.getDeclaredAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.length];
    for (int i = 0; i < declaredAnnotations.length; i++) {
      declaredAnnotations[i] = new ClassAnnotationInfo(annotations[i], this);
    }
    return declaredAnnotations;
  }

  public void setAccessible(boolean accessible) {
    _method.setAccessible(accessible);
  }

  @Override
  public Object invoke(Object ctx, Object[] args) throws InvocationTargetException, IllegalAccessException {
    return _method.invoke(ctx, args);
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables( IJavaMethodInfo mi ) {
    TypeVariable<Method>[] rawTypeVariables = _method.getTypeParameters();
    IJavaClassTypeVariable[] typeVariables = new IJavaClassTypeVariable[rawTypeVariables.length];
    for( int i = 0; i < rawTypeVariables.length; i++ ) {
      typeVariables[i] = new TypeVariableJavaTypeVariable( rawTypeVariables[i], _module );
    }
    FunctionType functionType = new FunctionType( mi, true );
    return GenericTypeVariable.convertTypeVars( functionType, typeVariables );
  }

  @Override
  public IJavaClassType[] getGenericParameterTypes() {
    Type[] rawTypes = _method.getGenericParameterTypes();
    IJavaClassType[] types = new IJavaClassType[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      Type rawType = rawTypes[i];
      IJavaClassType type = TypeJavaClassType.createType(rawType, _module);
      types[i] = type;
    }
    return types;
  }

  @Override
  public IJavaClassType getGenericReturnType() {
    return TypeJavaClassType.createType(_method.getGenericReturnType(), _module);
  }

  public Class[] getJavaParameterTypes() {
    return _method.getParameterTypes();
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode( getParameterTypes() );
    result = 31 * result + getReturnType().hashCode();
    result = 31 * result + getName().hashCode();
    return result;
  }

  public boolean equals( Object o ) {
    if( !(o instanceof IJavaClassMethod) ) {
      return false;
    }

    IJavaClassMethod jcm = (IJavaClassMethod)o;
    return getName().equals( jcm.getName() ) &&
           getReturnType() == jcm.getReturnType() &&
           Arrays.equals( getParameterTypes(), jcm.getParameterTypes() );
  }

  @Override
  public int compareTo( IJavaClassMethod o ) {
    return getName().compareTo( o.getName() );
  }

  public Method getJavaMethod() {
    return _method;
  }

  public String toString() {
    return _method.toString();
  }
}
