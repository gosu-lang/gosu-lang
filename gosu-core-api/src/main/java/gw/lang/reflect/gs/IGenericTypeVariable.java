/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IType;

public interface IGenericTypeVariable
{
  String getName();

  String getNameWithBounds( boolean bRelative );

  ITypeVariableDefinition getTypeVariableDefinition();

  IType getBoundingType();

  IGenericTypeVariable clone();

  void createTypeVariableDefinition(IType enclosingType);

  IGenericTypeVariable remapBounds( TypeVarToTypeMap actualParamByVarName );
}
