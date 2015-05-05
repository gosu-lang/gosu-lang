/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

import java.util.List;

public interface ISwitchStatement extends IStatement
{
  IExpression getSwitchExpression();

  ICaseClause[] getCases();

  List<? extends IStatement> getDefaultStatements();

  boolean isCoveredEnumSwitch();
}
