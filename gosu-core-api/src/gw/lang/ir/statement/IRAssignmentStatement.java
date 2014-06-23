/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRAssignmentStatement extends IRStatement {
  private IRSymbol _symbol;
  private IRExpression _value;

  public IRAssignmentStatement(IRSymbol symbol, IRExpression value) {
    _symbol = symbol;
    _value = value;

    value.setParent( this );
  }

  public IRSymbol getSymbol() {
    return _symbol;
  }

  public IRExpression getValue() {
    return _value;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
