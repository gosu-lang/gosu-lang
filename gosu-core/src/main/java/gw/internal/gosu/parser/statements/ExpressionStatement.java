/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IExpressionStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 */
public final class ExpressionStatement extends Statement implements IExpressionStatement
{
  Expression _expression;

  public Expression getExpression()
  {
    return _expression;
  }
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  public Object execute()
  {
    return getExpression().evaluate();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public String toString()
  {
    return _expression.toString();
  }
}
