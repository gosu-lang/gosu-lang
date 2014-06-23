/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IBitwiseXorExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 * Represents a bitwise OR expression in the Gosu grammar:
 * <pre>
 * <i>bitwise-or-expression</i>
 *   &lt;bitwise-xor-expression&gt;
 *   &lt;bitwise-or-expression&gt; <b>|</b> &lt;bitwise-xor-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class BitwiseXorExpression extends ArithmeticExpression implements IBitwiseXorExpression
{
  @Override
  public String toString()
  {
    return getLHS().toString() + " ^ " + getRHS().toString();
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }


    IType type = getType();
    Object lhsValue;
    Object rhsValue;
    lhsValue = getLHS().evaluate();
    rhsValue = getRHS().evaluate();
    if( type == JavaTypes.pINT() )
    {
      return (Integer)lhsValue ^ (Integer)rhsValue;
    }
    if (type == JavaTypes.pLONG() )
    {
      return (Long)lhsValue ^ (Long)rhsValue;
    }

    throw new UnsupportedNumberTypeException( type );
  }

  public String getOperator()
  {
    return "^";      
  }
}