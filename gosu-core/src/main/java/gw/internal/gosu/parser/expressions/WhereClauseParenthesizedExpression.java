/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseParenthesizedExpression;
import gw.lang.parser.EvaluationException;

/**
 * @deprecated
 */
public class WhereClauseParenthesizedExpression extends Expression implements IWhereClauseParenthesizedExpression
{
  protected Expression _expr;

  public WhereClauseParenthesizedExpression( Expression expr )
  {
    _expr = expr;
  }

  public Expression getExpression()
  {
    return _expr;
  }

  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addParenthesizedExpression( this );
  }

  @Override
  public String toString()
  {
    return "!" + getExpression();
  }

}
