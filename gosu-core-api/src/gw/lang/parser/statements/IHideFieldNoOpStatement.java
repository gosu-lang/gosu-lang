/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;


import gw.lang.parser.expressions.IVarStatement;

public interface IHideFieldNoOpStatement extends INoOpStatement
{
  IVarStatement getVarStmt();
}
