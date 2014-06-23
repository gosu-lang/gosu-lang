/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

import java.util.List;

public interface ICaseClause extends IExpression
{
  IExpression getExpression();

  List<? extends IStatement> getStatements();
}
