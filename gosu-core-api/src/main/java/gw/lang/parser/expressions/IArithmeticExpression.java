/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IArithmeticExpression extends IExpression, IOverridableOperation
{
  IExpression getLHS();

  IExpression getRHS();

  String getOperator();
}
