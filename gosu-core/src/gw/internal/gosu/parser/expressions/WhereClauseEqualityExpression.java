/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseEqualityExpression;

/**
 * Represents an where-clause-equality-expression in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseEqualityExpression extends ConditionalExpression implements IWhereClauseEqualityExpression
{
  /**
   * Is this an equals or a not-equals?
   */
  protected boolean _bEquals;

  /**
   * @return True if this is an equals expression (as apposed to not-eqauls).
   */
  public boolean isEquals()
  {
    return _bEquals;
  }

  /**
   * @param bEquals True if this is an equals expression (as apposed to not-eqauls).
   */
  public void setEquals( boolean bEquals )
  {
    _bEquals = bEquals;
  }

  /**
   */
  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addEqualityExpression( this );
  }

  @Override
  public String toString()
  {
    return getLHS().toString()
           + (isEquals() ? " == " : " != " )
           + getRHS().toString();
  }

}
