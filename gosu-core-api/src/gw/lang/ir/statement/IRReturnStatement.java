/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRReturnStatement extends IRStatement implements IRTerminalStatement {
  private IRExpression _returnValue;
  private IRStatement _tempVarAssignment;

  public IRReturnStatement() {
  }
  
  public IRReturnStatement(IRStatement tempVarAssignment, IRExpression returnValue) {
    _tempVarAssignment = tempVarAssignment;
    _returnValue = returnValue;
    if (returnValue != null) {
      returnValue.setParent( this );
    }
  }

  public IRExpression getReturnValue() {
    return _returnValue;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }

  public boolean hasTempVar()
  {
    return _tempVarAssignment != null;
  }

  public IRStatement getTempVarAssignment()
  {
    return _tempVarAssignment;
  }
}
