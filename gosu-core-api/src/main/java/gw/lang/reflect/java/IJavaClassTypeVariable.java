/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public interface IJavaClassTypeVariable extends IJavaClassType {
  String getName();
  IJavaClassType[] getBounds();
}