/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import java.lang.annotation.Annotation;

/**
 * @author mmatthews
 */
public class AnnotationPlaceholder implements Annotation {
  private String _name;

  public AnnotationPlaceholder(String name) {
    _name = name;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return AnnotationPlaceholder.class;
  }
}
