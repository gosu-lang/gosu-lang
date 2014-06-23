/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaTypeInfo;

public interface ITypeInfoFactory
{
  IJavaTypeInfo create(IType intrType, Class<?> backingClass);

  IJavaTypeInfo create(IType intrType, IJavaClassInfo backingClass);
}
