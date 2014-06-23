/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseExistsExpression;


/**
 * Represents a 'query' expression in the Gosu grammar:
 * <pre>
 * <i>where-clause-exists-expression</i>
 *   <b>exists</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; <b>where</b> &lt;where-clause-expression&gt; <b>)</b>
 * </pre>
 * <p/>
 *
 * @see gw.internal.gosu.parser.QueryExpressionParser
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseExistsExpression extends QueryExpression implements IWhereClauseExistsExpression
{
  public WhereClauseExistsExpression()
  {
    super();
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addExistsExpression( this );
  }

  @Override
  public String toString()
  {
    return "exists( " + getIdentifier() + " in " + getInExpression().toString() + " where " + getWhereClauseExpression().toString() + " )";
  }

}
