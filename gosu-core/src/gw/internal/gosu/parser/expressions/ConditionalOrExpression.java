/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IConditionalOrExpression;

/**
 * Represents a conditional OR expression in the Gosu grammar:
 * <pre>
 * <i>conditional-or-expression</i>
 *   &lt;conditional-and-expression&gt;
 *   &lt;conditional-or-expression&gt; <b>||</b> &lt;conditional-and-expression&gt;
 *   &lt;conditional-and-expression&gt; <b>or</b> &lt;equality-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ConditionalOrExpression extends ConditionalExpression implements IConditionalOrExpression
{
  /**
   * Performs a logical OR operation. Note this operation is naturally short-
   * circuited by using || in conjunction with postponing RHS evaluation.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return (Boolean)getLHS().evaluate() || (Boolean)getRHS().evaluate();
  }

  @Override
  public String toString()
  {
    return getLHS().toString() + "||" + getRHS().toString();
  }

}
