/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassGenericArrayType;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.GenericArrayType;

public class GenericArrayTypeJavaClassGenericArrayType extends TypeJavaClassType implements IJavaClassGenericArrayType {
  private GenericArrayType _genericArrayType;

  public GenericArrayTypeJavaClassGenericArrayType(GenericArrayType genericArrayType, IModule module) {
    super(genericArrayType, module);
    _genericArrayType = genericArrayType;
  }

  @Override
  public IJavaClassType getGenericComponentType() {
    return TypeJavaClassType.createType(_genericArrayType.getGenericComponentType(), _module);
  }

  @Override
  public IJavaClassType getConcreteType() {
    return null;
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public IModule getModule() {
    return _module;
  }
}