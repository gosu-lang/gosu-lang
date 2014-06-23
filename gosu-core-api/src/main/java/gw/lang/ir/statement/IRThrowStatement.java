/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRThrowStatement extends IRStatement implements IRTerminalStatement {
  private IRExpression _exception;

  public IRThrowStatement(IRExpression exception) {
    _exception = exception;
    exception.setParent( this );
  }

  public IRExpression getException() {
    return _exception;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }
}
