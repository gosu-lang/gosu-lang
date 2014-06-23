/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.IExpression;
import gw.internal.gosu.parser.expressions.MethodCallExpression;


/**
 * Represents a method-call statement as specified in the Gosu grammar:
 * <pre>
 * <i>method-call</i>
 *   &lt;name&gt; <b>(</b> [&lt;argument-list&gt;] <b>)</b>
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class MethodCallStatement extends Statement implements IMethodCallStatement
{
  MethodCallExpression _methodCall;

  /**
   * @return The MethodCallExpression corresponding to this method call statement.
   */
  public MethodCallExpression getMethodCall()
  {
    return _methodCall;
  }

  /**
   * @param methodCall The MethodCallExpression corresponding to this method call statement.
   */
  public void setMethodCall( MethodCallExpression methodCall )
  {
    _methodCall = methodCall;
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
    return _methodCall.toString();
  }

  public int getArgPosition() {
    return _methodCall.getArgPosition();
  }

  @Override
  public IExpression[] getArgs() {
    return _methodCall.getArgs();
  }

}
