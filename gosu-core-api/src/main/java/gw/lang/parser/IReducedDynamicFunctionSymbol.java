/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.List;

public interface IReducedDynamicFunctionSymbol extends IReducedSymbol {

  IType getType();

  String getName();

  IScriptPartId getScriptPart();

  boolean isStatic();

  int getModifiers();

  String getDisplayName();

  IGosuClass getGosuClass();

  IType[] getArgTypes();

  IType getReturnType();

  String getFullDescription();

  List<IReducedSymbol> getArgs();

  IReducedDynamicFunctionSymbol getSuperDfs();

  boolean isSuperOrThisConstructor();

  IAttributedFeatureInfo getMethodOrConstructorInfo();

  IReducedDynamicFunctionSymbol getBackingDfs();

  Object invoke( Object[] args );

  boolean isConstructor();
}
