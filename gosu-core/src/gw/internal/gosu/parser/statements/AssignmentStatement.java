/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ISymbol;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.ITerminalStatement;

/**
 * Represents an assignment statement in the Gosu grammar:
 * <pre>
 * <i>assignment-statement</i>
 *   &lt;identifier&gt; <b>=</b> &lt;expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class AssignmentStatement extends Statement implements IAssignmentStatement
{
  protected Identifier _identifier;
  protected Expression _expression;

  /**
   * @return The left-hand-side identifier of the expression.
   */
  public Identifier getIdentifier()
  {
    return _identifier;
  }

  /**
   * @param identifier The left-hand-side identifier of the expression.
   */
  public void setIdentifier( Identifier identifier )
  {
    _identifier = identifier;
    resetIdentifierType();
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
    resetIdentifierType();
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

    throw new CannotExecuteGosuException();
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
    return getIdentifier().getSymbol().getName() + " = " + getExpression().toString();
  }

  /**
   * If the identifier was initialized without a type specification
   * and a null value, the identifier's type becomes that of it's first
   * assignment.
   * TODO cgross: I don't think this is necessary any more.  Remove it.
   */
  private void resetIdentifierType()
  {
    Expression expression = getExpression();
    if( expression == null )
    {
      return;
    }

    Identifier identifier = getIdentifier();
    if( identifier == null )
    {
      return;
    }

    ISymbol symbol = identifier.getSymbol();
    if( symbol.getType() == GosuParserTypes.NULL_TYPE() )
    {
      symbol.setType( expression.getType() );
    }
  }

}
