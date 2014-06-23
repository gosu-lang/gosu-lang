/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IArithmeticExpression;
import gw.lang.reflect.IMethodInfo;

/**
 * The base class for arithmetic expressions with binary operators e.g., + - * / %.
 * Models arithmetic expressions by encapsulating the left and right hand side
 * operands.
 */
public abstract class ArithmeticExpression extends Expression implements IArithmeticExpression
{
  /**
   * An expression for the operand on the left-hand-side of the operator.
   */
  protected Expression _lhs;
  /**
   * An expression for the operand on the right-hand-side of the operator.
   */
  protected Expression _rhs;

  private String _strOperator;

  private IMethodInfo _override;

  /**
   * @return The expression for the left-hand-side operand.
   */
  public Expression getLHS()
  {
    return _lhs;
  }
  public void setLHS( Expression e )
  {
    _lhs = e;
  }

  /**
   * @return The expression for the right-hand-side operand.
   */
  public Expression getRHS()
  {
    return _rhs;
  }
  public void setRHS( Expression e )
  {
    _rhs = e;
  }

  @Override
  public String getOperator()
  {
    return _strOperator;
  }
  public void setOperator( String strOperator )
  {
    _strOperator = strOperator;
  }

  public IMethodInfo getOverride()
  {
    return _override;
  }
  public void setOverride( IMethodInfo overrideMi )
  {
    _override = overrideMi;
  }

  @Override
  public boolean isNullSafe()
  {
    return getOperator() != null &&
           getOperator().length() > 0 &&
           getOperator().charAt( 0 ) == '?';
  }

  public boolean isCompileTimeConstant()
  {
    return getLHS().isCompileTimeConstant() && getRHS().isCompileTimeConstant();
  }

  @Override
  public String toString()
  {
    return getLHS() + getOperator() + getRHS();
  }
}
