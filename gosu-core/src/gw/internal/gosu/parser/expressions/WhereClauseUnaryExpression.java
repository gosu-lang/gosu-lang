/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;


import gw.lang.parser.EvaluationException;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseUnaryExpression;

/**
 * Represents a where-clause-unary-expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseUnaryExpression extends Expression implements IWhereClauseUnaryExpression
{
  protected Expression _expression;

  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression e )
  {
    _expression = e;
  }

  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addUnaryExpression( this );
  }

  @Override
  public String toString()
  {
    return "!" + getExpression();
  }

}
