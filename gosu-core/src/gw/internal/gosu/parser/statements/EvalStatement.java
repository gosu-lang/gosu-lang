/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.EvalExpression;
import gw.lang.parser.statements.IEvalStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 */
public final class EvalStatement extends Statement implements IEvalStatement
{
  private EvalExpression _evalExpr;

  public EvalStatement( EvalExpression evalExpr )
  {
    _evalExpr = evalExpr;
  }

  public EvalExpression getEvalExpression()
  {
    return _evalExpr;
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
    return _evalExpr.toString();
  }

}