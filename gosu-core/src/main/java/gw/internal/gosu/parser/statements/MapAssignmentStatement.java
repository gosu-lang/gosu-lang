/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.MapAccess;

import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.ITerminalStatement;

public final class MapAssignmentStatement extends Statement implements IMapAssignmentStatement
{
  /**
   * The left-hand-side expression
   */
  protected MapAccess _mapAccessExpression;

  /**
   * The right-hand-side expression
   */
  protected Expression _expression;

  private boolean _compoundStatement;

  public MapAssignmentStatement()
  {
  }

  public MapAccess getMapAccessExpression()
  {
    return _mapAccessExpression;
  }

  public void setMapAccessExpression( MapAccess lhsExpression )
  {
    _mapAccessExpression = lhsExpression;
  }

  /**
   * @return The right-hand-side expression of the assignment.
   */
  public Expression getExpression()
  {
    return _expression;
  }

  /**
   * @param expression The right-hand-side expression of the assignment.
   */
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  /**
   * Execute the expression. Evaluates the RHS and assigns the resulting value
   * to the symbol referenced by the LHS identifier.
   */
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new IllegalStateException( "Can't execute this parsed element directly" );
  }

  @Override
  public String toString()
  {
    return getMapAccessExpression().toString() + " = " + getExpression().toString();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  public void setCompoundStatement( boolean compoundStatement )
  {
    _compoundStatement = compoundStatement;
  }

  public boolean isCompoundStatement()
  {
    return _compoundStatement;
  }
}
