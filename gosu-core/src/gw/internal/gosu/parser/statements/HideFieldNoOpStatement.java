/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.lang.parser.statements.IHideFieldNoOpStatement;

public class HideFieldNoOpStatement extends NoOpStatement implements IHideFieldNoOpStatement
{
  private VarStatement _varStmt;

  public HideFieldNoOpStatement( VarStatement varStmt )
  {
    _varStmt = varStmt;
  }

  public VarStatement getVarStmt()
  {
    return _varStmt;
  }
}
