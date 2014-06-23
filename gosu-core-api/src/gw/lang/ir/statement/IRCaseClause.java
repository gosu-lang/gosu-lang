/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class IRCaseClause {
  private IRExpression _condition;
  private List<IRStatement> _statements;

  public IRCaseClause(IRExpression condition, List<IRStatement> statements) {
    _condition = condition;
    _statements = statements;
  }

  public IRExpression getCondition() {
    return _condition;
  }

  public List<IRStatement> getStatements() {
    return _statements;
  }
}
