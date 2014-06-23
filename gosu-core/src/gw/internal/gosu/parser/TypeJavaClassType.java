/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;

public abstract class TypeJavaClassType implements IJavaClassType {
  private Type _type;
  protected IModule _module;

  public TypeJavaClassType(Type type, IModule module) {
    _type = type;
    _module = module;
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap) {
    return TypeLord.getActualType(_type, typeMap);
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return TypeLord.getActualType(_type, typeMap, bKeepTypeVars);
  }

  public static IJavaClassType createType(Type rawType, IModule module) {
    IJavaClassType type = null;
    if (rawType instanceof TypeVariable) {
      type = new TypeVariableJavaClassTypeVariable((TypeVariable) rawType, module);
    }
    if (rawType instanceof GenericArrayType) {
      type = new GenericArrayTypeJavaClassGenericArrayType((GenericArrayType) rawType, module);
    }
    if (rawType instanceof ParameterizedType) {
      type = new ParameterizedTypeJavaClassParameterizedType((ParameterizedType) rawType, module);
    }
    if (rawType instanceof WildcardType) {
      type = new WildcardTypeJavaClassWildcardType((WildcardType) rawType, module);
    }
    if (rawType instanceof Class) {
      type = JavaSourceUtil.getClassInfo((Class) rawType, module);
    }
    return type;
  }

  @Override
  public String getName() {
    return _type.toString();
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  @Override
  public String getNamespace() {
    return null;
  }
}

