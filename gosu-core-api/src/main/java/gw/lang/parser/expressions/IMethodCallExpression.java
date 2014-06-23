/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IHasArguments;
import gw.lang.reflect.IFunctionType;

public interface IMethodCallExpression extends IExpression, IHasArguments
{
  IFunctionSymbol getFunctionSymbol();

  IExpression[] getArgs();

  int getArgPosition();

  IFunctionType getFunctionType();
}
