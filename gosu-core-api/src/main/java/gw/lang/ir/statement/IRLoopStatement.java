/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IRLoopStatement
{
  public void setImplicitReturnStatement( IRReturnStatement returnStmt );
  public IRReturnStatement getImplicitReturnStatement();
}
