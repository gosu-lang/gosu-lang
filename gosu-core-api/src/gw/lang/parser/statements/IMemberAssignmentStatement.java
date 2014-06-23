/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IMemberAccessExpression;

public interface IMemberAssignmentStatement extends IStatement
{
  IExpression getRootExpression();

  String getMemberName();

  IExpression getExpression();

  IExpression getMemberExpression();

  IMemberAccessExpression getMemberAccess();
}
