/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.ITerminalStatement;

/**
 * Represents a bean-method-call statement as specified in the Gosu grammar:
 * <pre>
 * <i>bean-method-call</i>
 *   &lt;member-access&gt; <b>(</b> [&lt;argument-list&gt;] <b>)</b>
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class BeanMethodCallStatement extends Statement implements IBeanMethodCallStatement
{
  protected BeanMethodCallExpression _methodCall;


  /**
   * Constructs a BeanMethodCallStatement.
   */
  public BeanMethodCallStatement()
  {
  }

  /**
   * @return The method call corresponding to this statement.
   */
  public BeanMethodCallExpression getBeanMethodCall()
  {
    return _methodCall;
  }

  /**
   * @param methodCall The method call corresponding to this statement.
   */
  public void setBeanMethodCall( BeanMethodCallExpression methodCall )
  {
    _methodCall = methodCall;
  }

  /**
   * Executes the bean method call.
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

  /**
   * @return
   */
  @Override
  public String toString()
  {
    return _methodCall.toString();
  }

}
