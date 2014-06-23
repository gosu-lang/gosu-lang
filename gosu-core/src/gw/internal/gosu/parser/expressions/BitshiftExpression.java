/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.config.CommonServices;
import gw.lang.parser.expressions.IBitshiftExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;


/**
 * Represents a bitshift expression in the Gosu grammar:
 * <pre>
 * <i>bitshift-expression</i>
 *   &lt;additive-expression&gt;
 *   &lt;bitshift-expression&gt; <b>&lt;&lt;</b> &lt;additive-expression&gt;
 *   &lt;bitsfhit-expression&gt; <b>&gt;&gt;</b> &lt;additive-expression&gt;
 *   &lt;bitshift-expression&gt; <b>&gt;&gt;&gt;</b> &lt;additive-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class BitshiftExpression extends ArithmeticExpression implements IBitshiftExpression
{
  /**
   * Evaluate the expression.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }
    Object lhsValue = getLHS().evaluate();
    if( lhsValue == null )
    {
      return null;
    }
    Object rhsValue = getRHS().evaluate();
    if( rhsValue == null )
    {
      return null;
    }

    IType type = getType();
    if( getOperator().equals( "<<" ) )
    {
      if( type == JavaTypes.pINT() )
      {
        return CommonServices.getCoercionManager().makeIntegerFrom( lhsValue ) << CommonServices.getCoercionManager().makeIntegerFrom( rhsValue );
      }
      if( type == JavaTypes.pLONG() )
      {
        return makeLong( CommonServices.getCoercionManager().makeLongFrom( lhsValue ) << CommonServices.getCoercionManager().makeIntegerFrom( rhsValue ) );
      }

      throw new UnsupportedNumberTypeException(type);
    }

    if( getOperator().equals( ">>" ) )
    {
      if( type == JavaTypes.pINT() )
      {
        return CommonServices.getCoercionManager().makeIntegerFrom( lhsValue ) >> CommonServices.getCoercionManager().makeIntegerFrom( rhsValue );
      }
      if( type == JavaTypes.pLONG() )
      {
        return makeLong( CommonServices.getCoercionManager().makeLongFrom( lhsValue ) >> CommonServices.getCoercionManager().makeIntegerFrom( rhsValue ) );
      }

      throw new UnsupportedNumberTypeException(type);
    }

    if( getOperator().equals( ">>>" ) )
    {
      if( type == JavaTypes.pINT() )
      {
        return CommonServices.getCoercionManager().makeIntegerFrom( lhsValue ) >>> CommonServices.getCoercionManager().makeIntegerFrom( rhsValue );
      }
      if( type == JavaTypes.pLONG() )
      {
        return makeLong( CommonServices.getCoercionManager().makeLongFrom( lhsValue ) >>> CommonServices.getCoercionManager().makeIntegerFrom( rhsValue ) );
      }

      throw new UnsupportedNumberTypeException(type);
    }

    throw new UnsupportedNumberTypeException(type);
  }

}