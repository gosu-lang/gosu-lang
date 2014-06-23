/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IConditionalAndExpression;

/**
 * Represents a conditional AND expression in the Gosu grammar:
 * <pre>
 * <i>conditional-and-expression</i>
 *   &lt;bitwise-or-expression&gt;
 *   &lt;conditional-and-expression&gt; <b>&&</b> &lt;bitwise-or-expression&gt;
 *   &lt;conditional-and-expression&gt; <b>and</b> &lt;bitwise-or-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ConditionalAndExpression extends ConditionalExpression implements IConditionalAndExpression
{
  @Override
  public String toString()
  {
    return getLHS().toString() + "&&" + getRHS().toString();
  }


  /**
   * Performs a logical AND operation. Note this operation is naturally short-
   * circuited by using && in conjunction with postponing RHS evaluation.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return (Boolean)getLHS().evaluate() && (Boolean)getRHS().evaluate();
  }

}
