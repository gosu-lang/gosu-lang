/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;

public interface IThrowStatement extends ITerminalStatement
{
  IExpression getExpression();
}
