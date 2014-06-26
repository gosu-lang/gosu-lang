/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;
import gw.lang.reflect.IAnnotationInfo;

@UnstableAPI
public class IRAnnotation {
  private IRType _descriptor;
  private boolean _include;
  private IAnnotationInfo _annotation;

  public IRAnnotation( IRType descriptor, boolean include ) {
    this( descriptor, include, null );
  }

  public IRAnnotation( IRType descriptor, boolean include, IAnnotationInfo annotation ) {
    _descriptor = descriptor;
    _include = include;
    _annotation = annotation;
  }

  public IRType getDescriptor() {
    return _descriptor;
  }

  public boolean isInclude() {
    return _include;
  }

  public IAnnotationInfo getValue() {
    return _annotation;
  }
}
