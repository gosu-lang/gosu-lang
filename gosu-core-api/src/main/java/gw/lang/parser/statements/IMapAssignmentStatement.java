/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IMapAccessExpression;

public interface IMapAssignmentStatement extends IStatement
{
  IMapAccessExpression getMapAccessExpression();

  IExpression getExpression();
}
