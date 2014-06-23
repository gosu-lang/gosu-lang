/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import java.io.Serializable;

public interface IJavaClassField extends IJavaAnnotatedElement, Serializable
{
  boolean isSynthetic();

  int getModifiers();

  String getName();

  IJavaClassInfo getType();

  IJavaClassType getGenericType();

  boolean isEnumConstant();
}
