/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.internal.gosu.parser.IGosuAnnotation;

public interface IAnnotationExpression extends INewExpression {
  IGosuAnnotation getAnnotation();
  void setAnnotation( IGosuAnnotation annotation );
}
