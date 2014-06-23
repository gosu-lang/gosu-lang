/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IParenthesizedExpression extends IExpression
{
  IExpression getExpression();
}
