/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IVarStatement;

import java.util.List;

public interface IUsingStatement extends IStatement
{
  IExpression getExpression();

  IStatement getStatement();

  List<IVarStatement> getVarStatements();

  boolean hasVarStatements();
}
