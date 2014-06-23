/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.ir.statement.IRTerminalStatement;
import gw.lang.UnstableAPI;

@UnstableAPI
public abstract class IRStatement extends IRElement {
  private String _originalSourceStatement;

  public String getOriginalSourceStatement() {
    return _originalSourceStatement;
  }

  public void setOriginalSourceStatement(String originalSourceStatement) {
    _originalSourceStatement = originalSourceStatement;
  }

  public abstract IRTerminalStatement getLeastSignificantTerminalStatement();
}
