/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.parser.expressions.IParenthesizedExpression;

/**
 */
public class ParenthesizedExpression extends Expression implements IParenthesizedExpression
{
  private Expression _expr;

  public ParenthesizedExpression( Expression expr )
  {
    _expr = expr;
    _type = _expr.getType();
  }

  public Expression getExpression()
  {
    return _expr;
  }

  public boolean isCompileTimeConstant()
  {
    return getExpression().isCompileTimeConstant();
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return getExpression().evaluate();
  }

  public String toString()
  {
    return "(" + getExpression() + ")";
  }
}