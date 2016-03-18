/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IBindingExpression extends IExpression
{
  IExpression getLhsExpr();
  IExpression getRhsExpr();
}
