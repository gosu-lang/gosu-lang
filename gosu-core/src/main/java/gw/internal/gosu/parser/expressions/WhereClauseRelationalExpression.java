/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IWhereClauseRelationalExpression;

/**
 * Represents a where-clause-relational-expresson in the Gosu grammar:
 * <pre>
 * <i>where-clause-relational-expression</i>
 *   &lt;where-clause-unary-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&lt;</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&gt;</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&lt;=</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&gt;=</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>in</b> &lt;where-clause-in-expression&gt;
 * <p/>
 * <i>where-clause-in-expression</i>
 *   &lt;query-expression&gt;
 *   &lt;expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class WhereClauseRelationalExpression extends ConditionalExpression implements IWhereClauseRelationalExpression
{
  /**
   * An attribute specifying the type of operation e.g., > >= < <=
   */
  protected String _strOperator;

  public String getOperator()
  {
    return _strOperator;
  }

  public void setOperator( String strOperator )
  {
    _strOperator = strOperator;
  }

  public Object evaluate()
  {
    throw new EvaluationException( "Query expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addRelationalExpression( this );
  }

  @Override
  public String toString()
  {
    return getLHS() + " " + _strOperator + " " + getRHS();
  }

}
