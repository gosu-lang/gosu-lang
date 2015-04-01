/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.ITerminalStatement;

/**
 * Represents an array assignment statement in the Gosu grammar:
 * <pre>
 * <i>array-assignment</i>
 *   &lt;array-reference&gt; [ &lt;member&gt; ] <b>=</b> expression
 * <p/>
 * <i>array-reference</i>
 *   &lt;expression&gt;
 * <p/>
 * <i>member</i>
 *   &lt;array-access&gt;
 *   &lt;expression&gt;
 * </pre>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ArrayAssignmentStatement extends Statement implements IArrayAssignmentStatement
{
  /**
   * The left-hand-side expression
   */
  protected ArrayAccess _arrayAccessExpression;
  /**
   * The right-hand-side expression
   */
  protected Expression _expression;
  private boolean _compoundStatement;


  public ArrayAssignmentStatement()
  {
  }

  public ArrayAccess getArrayAccessExpression()
  {
    return _arrayAccessExpression;
  }

  public void setArrayAccessExpression( ArrayAccess lhsExpression )
  {
    _arrayAccessExpression = lhsExpression;
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
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public String toString()
  {
    return getArrayAccessExpression().toString() + " = " + getExpression().toString();
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
