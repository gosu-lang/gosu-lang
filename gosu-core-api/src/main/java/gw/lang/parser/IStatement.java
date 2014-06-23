/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.gs.IExternalSymbolMap;

public interface IStatement extends IParsedElement
{
  /**
   * Execute this statement.
   */
  Object execute();

  /**
   * Execute this statement.
   */
  Object execute(IExternalSymbolMap externalSymbols);

  boolean hasContent();

  /**
   * Indicates whether or not control flow is terminal at this statement.
   */
  ITerminalStatement getLeastSignificantTerminalStatement( boolean[] bAsolute );
}
