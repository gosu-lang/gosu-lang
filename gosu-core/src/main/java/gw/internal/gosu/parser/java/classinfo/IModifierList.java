/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.reflect.IAnnotationInfo;

import java.lang.annotation.Annotation;

public interface IModifierList {

  IAnnotationInfo[] getAnnotations();

  IAnnotationInfo getAnnotation(Class annotationClass);

  boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

  /**
   * Returns true if this list has the modifier and false otherwise.
   * @param modifierType one of the constants of java.lang.reflect.Modifier
   */
  boolean hasModifier(int modifierType);

  int getModifiers();
}
