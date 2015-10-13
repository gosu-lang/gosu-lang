/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;

public interface ITypeVariableDefinition
{
  IType getEnclosingType();
  void setEnclosingType( IType enclosingType );

  String getName();

  IGenericTypeVariable getTypeVar();

  ITypeVariableType getType();

  IType getBoundingType();

  ITypeVariableDefinition clone();

  Variance getVariance();
  void setVariance( Variance variance );
}
