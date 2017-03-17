/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.List;

public interface IDynamicFunctionSymbol extends IDynamicSymbol
{
  List<ISymbol> getArgs();

  IType[] getArgTypes();

  IType getReturnType();

  String getMethodSignature();

  String getParameterDisplay( boolean bRelative );

  IAttributedFeatureInfo getMethodOrConstructorInfo( boolean acceptNone );

  IAttributedFeatureInfo getMethodOrConstructorInfo();

  ITypeInfo getDeclaringTypeInfo();

  IFunctionStatement getDeclFunctionStmt();

  Object getValueDirectly();

  boolean isOverride();

  boolean isStatic();

  boolean isConstructor();

  IDynamicFunctionSymbol getSuperDfs();
  
  IMethodCallStatement getInitializer();

  IDynamicFunctionSymbol getBackingDfs();
  
  IReducedDynamicFunctionSymbol createReducedSymbol();

}
