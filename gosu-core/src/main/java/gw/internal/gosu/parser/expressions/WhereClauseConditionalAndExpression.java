/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseConditionalAndExpression;

/**
 * Represents a where-clause-conditional AND expression in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseConditionalAndExpression extends ConditionalExpression implements IWhereClauseConditionalAndExpression
{
  /**
   */
  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addConditionalAndExpression( this );
  }

  @Override
  public String toString()
  {
    return getLHS().toString() + " && " + getRHS().toString();
  }

}
