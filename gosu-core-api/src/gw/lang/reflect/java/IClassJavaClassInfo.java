/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public interface IClassJavaClassInfo extends IJavaClassInfo {
  Class getJavaClass();
  boolean isTypeGosuClassInstance();
}