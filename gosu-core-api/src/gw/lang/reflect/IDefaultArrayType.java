/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;

public interface IDefaultArrayType extends IType, IEnhanceableType
{
  // returns the concrete (java) type of this array type or null if no
  // such type exists
  public IJavaClassInfo getConcreteClass();
}
