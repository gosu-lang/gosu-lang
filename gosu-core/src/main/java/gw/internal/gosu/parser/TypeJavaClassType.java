/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.IType;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;

public abstract class TypeJavaClassType implements IJavaClassType {
  private Type _type;

  public TypeJavaClassType(Type type) {
    _type = type;
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap) {
    return TypeLord.getActualType(_type, typeMap);
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return TypeLord.getActualType(_type, typeMap, bKeepTypeVars);
  }

  public static IJavaClassType createType(Type rawType) {
    return createType( null, rawType );
  }
  public static IJavaClassType createType(Type genType, Type rawType) {
    IJavaClassType type = null;
    if (rawType instanceof TypeVariable) {
      type = new TypeVariableJavaClassTypeVariable((TypeVariable) rawType);
    }
    if (rawType instanceof GenericArrayType) {
      type = new GenericArrayTypeJavaClassGenericArrayType((GenericArrayType) rawType);
    }
    if (rawType instanceof ParameterizedType) {
      type = new ParameterizedTypeJavaClassParameterizedType((ParameterizedType) rawType);
    }
    if (rawType instanceof WildcardType) {
      type = new WildcardTypeJavaClassWildcardType( genType, (WildcardType) rawType);
    }
    if (rawType instanceof Class) {
      type = JavaSourceUtil.getClassInfo((Class) rawType);
    }
    return type;
  }

  @Override
  public String getName() {
    return _type.toString();
  }

  @Override
  public String getNamespace() {
    return null;
  }

  @Override
  public int hashCode() {
    return IJavaClassType.hashCode( this );
  }

  @Override
  public boolean equals( Object obj ) {
    return IJavaClassType.equals( this, obj );
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public IJavaClassType getComponentType() {
    return null;
  }
}

