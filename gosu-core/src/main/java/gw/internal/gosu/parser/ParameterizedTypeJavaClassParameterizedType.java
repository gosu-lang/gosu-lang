/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassParameterizedType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeJavaClassParameterizedType extends TypeJavaClassType implements IJavaClassParameterizedType {
  private ParameterizedType _parameterizedType;

  public ParameterizedTypeJavaClassParameterizedType( ParameterizedType parameterizedType ) {
    super(parameterizedType);
    _parameterizedType = parameterizedType;
  }

  @Override
  public IJavaClassType[] getActualTypeArguments() {
    Type[] rawTypes = _parameterizedType.getActualTypeArguments();
    IJavaClassType[] types = new IJavaClassType[rawTypes.length];
    for (int i = 0; i < rawTypes.length; i++) {
      types[i] = TypeJavaClassType.createType( _parameterizedType.getRawType(), rawTypes[i]);
    }
    return types;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return TypeJavaClassType.createType(_parameterizedType.getRawType());
  }

  @Override
  public String getSimpleName() {
    return getName();
  }
}