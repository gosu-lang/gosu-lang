/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.IExpression;

/**
 */
public final class NotAStatement extends Statement implements INotAStatement
{
  protected Expression _expr;

  /**
   * @return The identifier that is "lonely," i.e. it is not part of a Statement
   */
  public Expression getExpression()
  {
    return _expr;
  }

  public void setExpression( IExpression expr )
  {
    _expr = (Expression) expr;
  }

  /**
   * Execute the expression. Evaluates the RHS and assigns the resulting value
   * to the symbol referenced by the LHS identifier.
   */
  public Object execute()
  {
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  public String toString()
  {
    return "";
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

}
