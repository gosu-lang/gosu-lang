/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.parser.expressions.Variance;

public interface IJavaClassTypeVariable extends IJavaClassType {
  String getName();
  IJavaClassType[] getBounds();
  boolean isFunctionTypeVar();

  Variance getVariance();
  void setVariance( Variance contravariant );
}