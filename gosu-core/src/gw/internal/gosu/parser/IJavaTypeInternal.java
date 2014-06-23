/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaArrayType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.ITypeInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public interface IJavaTypeInternal extends IJavaArrayType, IJavaType
{
  @SuppressWarnings({"unchecked"})
  Map<Class<?>, IJavaType> TYPES_BY_CLASS = new ConcurrentHashMap();

  Object writeReplace();

  IGosuClassInternal getAdapterClass();
  IGosuClassInternal getAdapterClassDirectly();

  void setAdapterClass( IGosuClassInternal adapterClass );

  ITypeInfo getExplicitTypeInfo();

  GenericTypeVariable[] assignGenericTypeVariables();

  boolean isDefiningGenericTypes();

  void setComponentType( IJavaTypeInternal javaType );

  int getTypeInfoChecksum();

  boolean hasAncestorBeenUpdated();

}
