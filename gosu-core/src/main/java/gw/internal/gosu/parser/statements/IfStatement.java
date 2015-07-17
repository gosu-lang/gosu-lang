/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents an if-statement as specified in the Gosu grammar:
 * <pre>
 * <i>if-statement</i>
 *   <b>if</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt; [ <b>else</b> &lt;statement&gt; ] [ <b>unless</b> <b>(</b> &lt;expression&gt; <b>)</b> ]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class IfStatement extends Statement implements IIfStatement
{
  protected Expression _expression;
  protected Statement _statement;
  protected Statement _elseStatement;

  /**
   * @return The conditional expression.
   */
  public Expression getExpression()
  {
    return _expression;
  }

  /**
   * @param expression The conditional expression.
   */
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  /**
   * @return The statement to execute if the conditional expression evaluates
   *         to true.
   */
  public Statement getStatement()
  {
    return _statement;
  }

  /**
   * @param statement The statement to execute if the conditional expression
   *                  evaluates to true.
   */
  public void setStatement( Statement statement )
  {
    _statement = statement;
  }

  /**
   * @return The else statement to execute if the conditional expression evaluates
   *         to false.
   */
  public Statement getElseStatement()
  {
    return _elseStatement;
  }

  /**
   * @return <tt>true</tt> if this if statement has an else statement.
   */
  public boolean hasElseStatement()
  {
    return _elseStatement != null;
  }

  /**
   * @param elseStatement The else statement to execute if the conditional expression
   *                      evaluates to false.
   */
  public void setElseStatement( Statement elseStatement )
  {
    _elseStatement = elseStatement;
  }

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
    boolean[] bAbsoluteStmt = {false};
    ITerminalStatement ifStmtTerminal = getStatement() == null ? null : getStatement().getLeastSignificantTerminalStatement( bAbsoluteStmt );
    boolean[] bAbsoluteElse = {false};
    ITerminalStatement elseStmtTerminal = getElseStatement() == null ? null : getElseStatement().getLeastSignificantTerminalStatement( bAbsoluteElse );
    bAbsolute[0] = bAbsoluteStmt[0] && bAbsoluteElse[0];
    return getLeastSignificant( ifStmtTerminal, elseStmtTerminal );
  }

  @Override
  public String toString()
  {
    String strElseStmt = getElseStatement() == null ? "" : ("else\n" + getElseStatement());

    //noinspection deprecation
    return "if( " + toString(getExpression()) + " )\n" +
           toString(getStatement()) +
           strElseStmt;
  }
  
  private String toString(Object o) {
    return o == null ? "" : o.toString();
  }

}
