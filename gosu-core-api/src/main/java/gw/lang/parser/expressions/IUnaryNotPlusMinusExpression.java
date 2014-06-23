/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IUnaryNotPlusMinusExpression extends IExpression
{
  boolean isNot();

  boolean isBitNot();

  IExpression getExpression();
}
