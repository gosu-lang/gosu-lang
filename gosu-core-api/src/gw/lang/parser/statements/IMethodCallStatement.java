/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IHasArguments;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IMethodCallExpression;

public interface IMethodCallStatement extends IStatement, IHasArguments
{
  IMethodCallExpression getMethodCall();
}
