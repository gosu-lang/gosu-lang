/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.module.IModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldJavaClassField implements IJavaClassField {
  private Field _field;
  private IModule _module;

  public FieldJavaClassField(Field field, IModule module) {
    _field = field;
    _module = module;
  }

  @Override
  public boolean isSynthetic() {
    return _field.isSynthetic();
  }

  @Override
  public int getModifiers() {
    return _field.getModifiers();
  }

  @Override
  public String getName() {
    return _field.getName();
  }

  @Override
  public IJavaClassInfo getType() {
    return JavaSourceUtil.getClassInfo(_field.getType(), _module);
  }

  @Override
  public IJavaClassType getGenericType() {
    IJavaClassType type = TypeJavaClassType.createType(_field.getGenericType(), _module);
    if (type == null) {
      throw new IllegalStateException("Unable to create a generic type for the field " + _field.getName() + " on " + _field.getDeclaringClass().getName() + " in module " + _module.getName() + "\n" +
        "Type : " + _field.getType() + ", Type.class " + _field.getType().getClass().getName() + " GenericType : " + _field.getGenericType() + ", GenericType.class : " + _field.getGenericType().getClass().getName() );
    }
    return type;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo(_field.getDeclaringClass(), _module);
  }

  @Override
  public boolean isEnumConstant() {
    return _field.isEnumConstant();
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return _field.isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    Annotation annotation = _field.getAnnotation(annotationClass);
    return annotation != null ? new ClassAnnotationInfo(annotation, this) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    Annotation[] annotations = _field.getDeclaredAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.length];
    for (int i = 0; i < declaredAnnotations.length; i++) {
      declaredAnnotations[i] = new ClassAnnotationInfo(annotations[i], this);
    }
    return declaredAnnotations;
  }

  public void setAccessible(boolean accessible) {
    _field.setAccessible(accessible);
  }

  public Object get(Object o) throws IllegalAccessException {
    return _field.get(o);
  }

  public void set(Object o, Object value) throws IllegalAccessException {
    _field.set(o, value);
  }

  public String toString() {
    return getName() + " in " + _field.getDeclaringClass().getSimpleName();
  }
}
