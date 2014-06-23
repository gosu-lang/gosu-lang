/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.WildcardType;
import java.lang.reflect.Type;

public class WildcardTypeJavaClassWildcardType extends TypeJavaClassType implements IJavaClassWildcardType {
  private WildcardType _wildcardType;

  public WildcardTypeJavaClassWildcardType(WildcardType wildcardType, IModule module) {
    super(wildcardType, module);
    _wildcardType = wildcardType;
  }

  @Override
  public IJavaClassType getUpperBound() {
    // we only support one bound in Gosu
    Type rawType = _wildcardType.getUpperBounds()[0];
    return TypeJavaClassType.createType(rawType, _module);
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getUpperBound();
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