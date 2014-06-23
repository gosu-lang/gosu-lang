/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;
import gw.lang.ir.statement.IRLoopStatement;
import gw.lang.ir.statement.IRReturnStatement;

@UnstableAPI
public abstract class IRAbstractLoopStatement extends IRStatement implements IRLoopStatement
{
  private IRReturnStatement _returnStmt;

  public void setImplicitReturnStatement( IRReturnStatement returnStmt )
  {
    _returnStmt = returnStmt;
  }

  public IRReturnStatement getImplicitReturnStatement()
  {
    return _returnStmt;
  }
}
