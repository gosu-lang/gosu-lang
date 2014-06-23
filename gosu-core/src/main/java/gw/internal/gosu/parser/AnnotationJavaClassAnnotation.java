/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassAnnotation;
import gw.util.GosuExceptionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationJavaClassAnnotation implements IJavaClassAnnotation {
  private Annotation _annotation;

  public AnnotationJavaClassAnnotation(Annotation annotation) {
    _annotation = annotation;
  }

  @Override
  public String annotationTypeName() {
    return _annotation.annotationType().getName();
  }

  @Override
  public Object getValue(String name) {
    try {
      Method method = _annotation.annotationType().getMethod(name);
      return method.invoke(_annotation);
    } catch (Exception e) {
      throw GosuExceptionUtil.forceThrow(e);
    }
  }
}