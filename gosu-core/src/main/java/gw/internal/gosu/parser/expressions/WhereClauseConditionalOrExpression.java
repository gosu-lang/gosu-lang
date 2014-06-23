/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseConditionalOrExpression;

/**
 * Represents a where-clause-conditional OR expression in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseConditionalOrExpression extends ConditionalExpression implements IWhereClauseConditionalOrExpression
{
  /**
   */
  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  @Override
  public String toString()
  {
    return getLHS().toString() + " || " + getRHS().toString();
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addConditionalOrExpression( this );
  }

}
