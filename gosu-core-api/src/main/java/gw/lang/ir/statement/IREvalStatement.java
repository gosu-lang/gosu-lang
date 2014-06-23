/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;


@UnstableAPI
public class IREvalStatement extends IRStatement
{
  private IRExpression _expression;

  public IREvalStatement( IRExpression expression )
  {
    _expression = expression;
    expression.setParent( this );
  }

  public IRExpression getExpression()
  {
    return _expression;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }
}