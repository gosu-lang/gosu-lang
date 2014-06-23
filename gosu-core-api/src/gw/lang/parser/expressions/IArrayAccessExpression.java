/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.reflect.IType;
import gw.lang.parser.IExpression;

public interface IArrayAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IExpression getMemberExpression();

  IType getComponentType();
}
