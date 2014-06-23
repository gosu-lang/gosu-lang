/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IAssertStatement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.lang.parser.statements.ILoopStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IThrowStatement;
import gw.lang.parser.statements.TerminalType;


/**
 * Represents an do-while-statement as specified in the Gosu grammar:
 * <pre>
 * <i>do-while-statement</i>
 *   <b>while</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class DoWhileStatement extends LoopStatement implements IDoWhileStatement
{
  protected Expression _expression;
  protected Statement _statement;

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
   * @return The statement to execute while the conditional expression evaluates
   *         to true.
   */
  public Statement getStatement()
  {
    return _statement;
  }

  /**
   * @param statement The statement to execute while the conditional expression
   *                  evaluates to true.
   */
  public void setStatement( Statement statement )
  {
    _statement = statement;
  }

  /**
   * Execute the do...while statement
   */
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    Statement statement = getStatement();
    if( statement != null )
    {
      ITerminalStatement terminalStmt = statement.getLeastSignificantTerminalStatement( bAbsolute );
      if( terminalStmt instanceof IReturnStatement ||
          terminalStmt instanceof IAssertStatement ||
          terminalStmt instanceof IThrowStatement ||
          terminalStmt instanceof ILoopStatement )
      {
        if( !bAbsolute[0] ) {
          if( isConditionLiteralTrue() ) {
            bAbsolute[0] = true;
            return this;
          }
        }
        return terminalStmt;
      }
      else if( !(terminalStmt instanceof IBreakStatement) && isConditionLiteralTrue() ) {
        bAbsolute[0] = true;
        return this;
      }
    }
    return null;
  }

  @Override
  public TerminalType getTerminalType() {
    boolean[] bAbsolute = {false};
    ITerminalStatement lst = getLeastSignificantTerminalStatement( bAbsolute );
    return lst == this ? TerminalType.ForeverLoop : null;  // a loop can be a terminal if and only if it is a forever loop
  }

  @Override
  public String toString()
  {
    return "do\n" + (getStatement() == null ? "" : getStatement().toString()) +
           "\nwhile( " + (getExpression() == null ? "" : getExpression().toString()) + " )";
  }

}
