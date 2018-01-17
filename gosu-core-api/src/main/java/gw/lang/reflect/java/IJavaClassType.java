/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;

import gw.util.GosuObjectUtil;
import java.io.Serializable;

public interface IJavaClassType extends Serializable {
  IJavaClassInfo[] EMPTY_ARRAY = new IJavaClassInfo[0];
  ErrorJavaClassInfo NULL_TYPE = new ErrorJavaClassInfo();
  ErrorJavaClassInfo ERROR_TYPE = new ErrorJavaClassInfo();

  IType getActualType( TypeVarToTypeMap typeMap );
  IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars );

  IJavaClassType getConcreteType();

  String getName();
  String getSimpleName();
  
  boolean isArray();
  IJavaClassType getComponentType();

  IModule getModule();

  String getNamespace();

  default boolean isAssignableFrom( IJavaClassType from )
  {
    if( equals( from ) ||
        GosuObjectUtil.equals( getConcreteType(), from ) )
    {
      return true;
    }

    IType actualFrom = from.getActualType( TypeVarToTypeMap.EMPTY_MAP );
    IType actualTo = getActualType( TypeVarToTypeMap.EMPTY_MAP );

    return actualTo != null && actualFrom != null && actualTo.isAssignableFrom( actualFrom ) ||
           getConcreteType().getActualType( TypeVarToTypeMap.EMPTY_MAP ).isAssignableFrom( actualFrom );
  }

  static boolean equals(IJavaClassType thisObj, Object that) {
    if (!(that instanceof IJavaClassType)) {
      return false;
    }
    if (thisObj.isArray()) {
      return ((IJavaClassType) that).isArray() && thisObj.getComponentType().equals(((IJavaClassType) that).getComponentType());
    }
    return thisObj.getName().equals(((IJavaClassType) that).getName()) &&
        thisObj.getModule().equals(((IJavaClassType) that).getModule());
  }

  static int hashCode(IJavaClassType thisObj) {
    return thisObj.isArray() ?
        thisObj.getComponentType().hashCode() :
        thisObj.getName().hashCode() * 31 + thisObj.getModule().hashCode();
  }
}