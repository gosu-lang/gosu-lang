/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

public interface ITerminalStatement extends IStatement
{

  /**
   * Mostly this is important so we can weight the terminal types i.e., the ordinal in the enum is significant
   */
  TerminalType getTerminalType();
}
