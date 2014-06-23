/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;

@UnstableAPI
public class IRImplicitReturnStatement extends IRReturnStatement
{
  public IRImplicitReturnStatement()
  {
    super();
  }

  public IRImplicitReturnStatement( IRStatement tempVarAssignment, IRExpression returnValue )
  {
    super( tempVarAssignment, returnValue );
  }

  public boolean isImplicit()
  {
    return true;
  }
}
