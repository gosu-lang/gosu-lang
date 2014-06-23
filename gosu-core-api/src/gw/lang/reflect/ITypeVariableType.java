/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.expressions.ITypeVariableDefinition;

public interface ITypeVariableType extends INonLoadableType
{
  ITypeVariableDefinition getTypeVarDef();

  String getNameWithEnclosingType();

  IType getBoundingType();

  boolean isFunctionStatement();
}
