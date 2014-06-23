/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IArrayAccessExpression;

public interface IArrayAssignmentStatement extends IStatement
{
  IArrayAccessExpression getArrayAccessExpression();

  IExpression getExpression();
}
