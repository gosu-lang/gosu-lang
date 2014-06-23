/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.GosuShop;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.Map;

public class ClassInfoUtil {
  public static IType getPublishedType(IType type, IJavaClassInfo classInfo) {
    Map<IType, IType> map = GosuShop.getPublishedTypeMap(classInfo);
    if (map == null) {
      return type;
    } else {
      IType publishedType = map.get(type);
      return publishedType != null ? publishedType : type;
    }
  }

  public static IType getActualReturnType(IJavaClassType genericType, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars) {
    return genericType.getActualType(actualParamByVarName, bKeepTypeVars);
  }

  public static IType[] getActualTypes(IJavaClassType[] genericTypes, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars) {
    IType[] types = new IType[genericTypes.length];
    for (int i = 0; i < types.length; i++) {
      types[i] = genericTypes[i].getActualType(actualParamByVarName, bKeepTypeVars);
    }
    return types;
  }

}
