/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

public interface IExpressionStatement extends IStatement
{
  IExpression getExpression();
}
