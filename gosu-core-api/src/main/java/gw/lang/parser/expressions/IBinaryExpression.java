/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IBinaryExpression extends IExpression
{
  IExpression getLHS();
  IExpression getRHS();
  String getOperator();
}
