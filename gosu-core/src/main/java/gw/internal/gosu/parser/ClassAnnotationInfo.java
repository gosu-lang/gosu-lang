/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Holds annotation information for a feature
 */
public class ClassAnnotationInfo implements IAnnotationInfo {
  private Annotation _annotation;
  private IType _containerType;

  public ClassAnnotationInfo(Annotation annotation, IType containerType) {
    _annotation = annotation;
    _containerType = containerType;
  }

  public ClassAnnotationInfo(Annotation annotation, IJavaAnnotatedElement element) {
    _annotation = annotation;
    if (element instanceof IJavaClassInfo) {
      _containerType = ((IJavaClassInfo) element).getJavaType();
    } else {
      _containerType = element.getEnclosingClass().getJavaType();
    }
  }

  public String getName() {
    return _annotation.annotationType().getName();
  }

  public IType getOwnersType() {
    return _containerType;
  }

  public String getDisplayName() {
    return getName();
  }

  public String getDescription() {
    return getName();
  }

  public Annotation getInstance() {
    return _annotation;
  }

  @Override
  public Object getFieldValue(String fieldName) {
    try {
      Method method = _annotation.annotationType().getMethod(fieldName);
      Object value = method.invoke(_annotation);
      return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, getOwnersType().getTypeInfo() );
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public IType getType() {
    return TypeSystem.getByFullName(getName());
  }

  public String toString() {
    return getName();
  }
}
