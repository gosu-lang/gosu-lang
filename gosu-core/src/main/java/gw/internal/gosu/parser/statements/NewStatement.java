/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.statements.INewStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents a method-call statement as specified in the Gosu grammar:
 * <pre>
 * <i>new-statement</i>
 *   &lt;new-expression&gt;]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class NewStatement extends Statement implements INewStatement
{
  NewExpression _newExpression;

  /**
   * @return The NewExpression corresponding to this method call statement.
   */
  public NewExpression getNewExpression()
  {
    return _newExpression;
  }

  /**
   * @param newExpression The NewExpression corresponding to this method call statement.
   */
  public void setNewExpression( NewExpression newExpression )
  {
    _newExpression = newExpression;
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
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public String toString()
  {
    return _newExpression.toString();
  }

  public int getArgPosition() {
    return _newExpression.getArgPosition();
  }

  @Override
  public IExpression[] getArgs() {
    return _newExpression.getArgs();
  }

}
