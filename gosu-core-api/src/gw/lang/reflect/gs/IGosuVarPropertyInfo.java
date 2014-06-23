/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.ICompileTimeConstantValue;

public interface IGosuVarPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IGenericMethodInfo, ICompileTimeConstantValue
{
  IType assignActualType( IType type );

  void assignSymbolType( IType type );

  boolean hasDeclaredProperty();

  boolean isScopedField();

  IType getScopedSymbolType();

  String getSymbolScopeString();

  String getSymbolAttributeName();
}
