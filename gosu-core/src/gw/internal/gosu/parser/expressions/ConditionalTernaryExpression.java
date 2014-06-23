/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IConditionalTernaryExpression;


/**
 * Conditional Ternary Expression as specified in the Gosu.
 */
public class ConditionalTernaryExpression extends Expression implements IConditionalTernaryExpression
{
  protected Expression _condition;
  protected Expression _first;
  protected Expression _second;

  public ConditionalTernaryExpression()
  {
  }

  /**
   * @return The condition expression for the ternary expression.
   */
  public Expression getCondition()
  {
    return _condition;
  }

  /**
   * @param e The condition expression for the ternary expression.
   */
  public void setCondition( Expression e )
  {
    _condition = e;
  }

  /**
   * @return The first choice expression for the ternary expression.
   */
  public Expression getFirst()
  {
    return _first;
  }

  /**
   * @param e The first choice expression for the ternary expression.
   */
  public void setFirst( Expression e )
  {
    _first = e;
  }

  /**
   * @return The second choice expression for the ternary expression.
   */
  public Expression getSecond()
  {
    return _second;
  }

  /**
   * @param e The second choice expression for the ternary expression.
   */
  public void setSecond( Expression e )
  {
    _second = e;
  }

  @Override
  public boolean isCompileTimeConstant() {
    return (getCondition() == null || getFirst() == null || getSecond() == null) ? false :
            (getCondition().isCompileTimeConstant() &&
                    getFirst().isCompileTimeConstant() &&
                    getSecond().isCompileTimeConstant());
  }

  /**
   * Evaluates this Expression and returns the result.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return (Boolean)getCondition().evaluate() ? getFirst().evaluate() : getSecond().evaluate();
  }

  @Override
  public String toString()
  {
    return getCondition()
           + " ? "
           + getFirst()
           + " : "
           + getSecond();
  }

}
