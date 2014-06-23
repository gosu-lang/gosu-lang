/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.ITypeInfoFactory;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class TypeInfoFactoryImpl implements ITypeInfoFactory
{
  @Override
  public IJavaTypeInfo create(IType intrType, Class<?> backingClass)
  {
    return new JavaTypeInfo( intrType, TypeSystem.getJavaClassInfo(backingClass));
  }

  @Override
  public IJavaTypeInfo create(IType intrType, IJavaClassInfo backingClass)
  {
    return new JavaTypeInfo( intrType, backingClass);
  }
}