/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public interface IJavaClassWildcardType extends IJavaClassType {
  IJavaClassType getUpperBound();
}