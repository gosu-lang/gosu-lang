/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;
import gw.lang.ir.expression.IRNewExpression;

@UnstableAPI
public class IRNewStatement extends IRStatement {
  private IRNewExpression _expression;

  public IRNewStatement( IRNewExpression expression ) {
    _expression = expression;
    expression.setParent( this );
  }

  public IRNewExpression getNewExpression() {
    return _expression;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
