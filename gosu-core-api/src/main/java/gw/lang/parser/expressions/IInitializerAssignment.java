/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

public interface IInitializerAssignment extends IStatement
{
  String getPropertyName();
  IPropertyInfo getPropertyInfo();
  IExpression getRhs();
  IType getOwnerType();
}
