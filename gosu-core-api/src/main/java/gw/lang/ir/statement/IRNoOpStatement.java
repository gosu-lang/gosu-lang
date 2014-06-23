/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNoOpStatement extends IRStatement {
  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }

  @Override
  public boolean isImplicit() {
    return true;
  }
}
