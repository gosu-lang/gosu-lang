/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IBinaryExpression;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;


public abstract class BinaryExpression extends Expression implements IBinaryExpression
{
  private Expression _lhs;
  private Expression _rhs;
  private String _strOperator;


  public BinaryExpression()
  {
    setType( JavaTypes.pBOOLEAN() );
  }

  public Expression getLHS()
  {
    return _lhs;
  }
  public void setLHS( Expression e )
  {
    _lhs = e;
  }

  public Expression getRHS()
  {
    return _rhs;
  }
  public void setRHS( Expression e )
  {
    _rhs = e;
  }

  public String getOperator()
  {
    return _strOperator;
  }
  public void setOperator( String strOperator )
  {
    _strOperator = strOperator;
  }

  @Override
  public String toString()
  {
    return getLHS().toString()
         + " " + getOperator() + " "
         + getRHS().toString();
  }  
}