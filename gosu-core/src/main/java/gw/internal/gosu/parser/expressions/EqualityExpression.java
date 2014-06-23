/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.BeanAccess;
import gw.lang.parser.expressions.IEqualityExpression;
import gw.lang.reflect.IType;

/**
 * Represents an equality expression in the Gosu grammar:
 * <pre>
 * <i>equality-expression</i>
 *   &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b>==</b> &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b>!=</b> &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b><></b> &lt;relational-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class EqualityExpression extends ConditionalExpression implements IEqualityExpression
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
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    Object lhsValue = getLHS().evaluate();
    Object rhsValue = getRHS().evaluate();

    IType lhsType = getLHS().getType();
    IType rhsType = getRHS().getType();

    boolean bValue;
    if( lhsValue != null && rhsValue != null && BeanAccess.isNumericType( getLHS().getType() ) )
    {
      bValue = compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    else
    {
      bValue = BeanAccess.areValuesEqual( lhsType, lhsValue, rhsType, rhsValue );
    }
    return isEquals() ? bValue : !bValue;
  }

  @Override
  public String toString()
  {
    return getLHS().toString()
           + (isEquals() ? "==" : "!=" )
           + getRHS().toString();
  }

}
