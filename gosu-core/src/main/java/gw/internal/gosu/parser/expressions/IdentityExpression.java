/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IIdentityExpression;

/**
 * Represents an identity expression in the Gosu grammar:
 * <pre>
 * <i>identity-expression</i>
 *   &lt;relational-expression&gt;
 *   &lt;identity-expression&gt; <b>===</b> &lt;relational-expression&gt;
 *   &lt;identity-expression&gt; <b>!==</b> &lt;relational-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class IdentityExpression extends ConditionalExpression implements IIdentityExpression
{
  private boolean _bEquals;


  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return isEquals()
           ? getLHS().evaluate() == getRHS().evaluate()
           : getLHS().evaluate() != getRHS().evaluate();
  }

  public String toString()
  {
    return getLHS().toString()
           + (isEquals() ? " === " : " !== ")
           + getRHS().toString();
  }

  public boolean isEquals()
  {
    return _bEquals;
  }
  public void setEquals( boolean bEquals )
  {
    _bEquals = bEquals;
  }
}