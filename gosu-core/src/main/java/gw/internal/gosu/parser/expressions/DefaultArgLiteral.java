/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 * The root (marker) class for all Literal expressions.
 */
public class DefaultArgLiteral extends Expression implements ILiteralExpression
{
  private IExpression _expr;
  
  public DefaultArgLiteral( IType type, IExpression expr )
  {
    setType( type );
    _expr = expr;
  }

  public Object getValue()
  {
    if( !(_expr instanceof NullExpression) && getType() == JavaTypes.BIG_DECIMAL() || getType() == JavaTypes.BIG_INTEGER() )
    {
      return _expr.evaluate().toString();
    }
    else
    {
      return _expr.evaluate();
    }
  }

  public IExpression getExpression()
  {
    return _expr;
  }

  @Override
  public boolean isCompileTimeConstant()
  {
    return _expr.isCompileTimeConstant();
  }

  @Override
  public Object evaluate() {
    return _expr.evaluate();
  }

  @Override
  public String toString()
  {
    return "Named Arg Value: " + _expr.toString();
  }

}
