/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IPropertyInfo;

public interface IFieldAccessExpression extends IMemberAccessExpression
{
  IPropertyInfo getPropertyInfo();

  IExpression getMemberExpression();
}
