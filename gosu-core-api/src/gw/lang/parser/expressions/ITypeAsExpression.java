/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.ICoercer;

public interface ITypeAsExpression extends IExpression
{
  IExpression getLHS();
  ICoercer getCoercer();
}
