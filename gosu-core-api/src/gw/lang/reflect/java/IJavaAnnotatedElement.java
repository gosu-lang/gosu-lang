/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;

import java.lang.annotation.Annotation;

public interface IJavaAnnotatedElement {

  /**
   * Returns true if an annotation for the specified type
   * is present on this element, else false.  This method
   * is designed primarily for convenient access to marker annotations.
   *
   * @param annotationClass the Class object corresponding to the
   *                        annotation type
   * @return true if an annotation for the specified annotation
   *         type is present on this element, else false
   * @throws NullPointerException if the given annotation class is null
   * @since 1.5
   */
  boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

  /**
   * Returns this element's annotation for the specified type if
   * such an annotation is present, else null.
   *
   * @param annotationClass the Class object corresponding to the
   *                        annotation type
   * @return this element's annotation for the specified annotation type if
   *         present on this element, else null
   * @throws NullPointerException if the given annotation class is null
   * @since 1.5
   */
  IAnnotationInfo getAnnotation(Class<? extends Annotation> annotationClass);

  /**
   * Returns all annotations that are directly present on this
   * element.  Unlike the other methods in this interface, this method
   * ignores inherited annotations.  (Returns an array of length zero if
   * no annotations are directly present on this element.)  The caller of
   * this method is free to modify the returned array; it will have no
   * effect on the arrays returned to other callers.
   *
   * @return All annotations directly present on this element
   * @since 1.5
   */
  IAnnotationInfo[] getDeclaredAnnotations();


  IJavaClassInfo getEnclosingClass();
}
