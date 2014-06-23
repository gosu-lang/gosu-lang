/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;

public interface IMapAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IExpression getKeyExpression();

  IType getComponentType();

  IType getKeyType();
}
