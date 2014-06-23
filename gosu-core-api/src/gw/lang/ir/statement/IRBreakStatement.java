/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRBreakStatement extends IRStatement implements IRTerminalStatement {
  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }
}