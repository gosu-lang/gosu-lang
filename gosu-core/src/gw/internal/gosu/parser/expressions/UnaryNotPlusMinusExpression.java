/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.config.CommonServices;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IUnaryNotPlusMinusExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 * Represents a unary-not-plus-minus expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class UnaryNotPlusMinusExpression extends Expression implements IUnaryNotPlusMinusExpression
{
  private boolean _bNot;
  private boolean _bBitNot;
  private Expression _expression;


  public boolean isNot()
  {
    return _bNot;
  }

  public void setNot( boolean bNot )
  {
    _bNot = bNot;
  }

  public boolean isBitNot()
  {
    return _bBitNot;
  }

  public void setBitNot( boolean bBitNot )
  {
    _bBitNot = bBitNot;
  }

  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression e )
  {
    _expression = e;
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

    Object value = getExpression().evaluate();
    if( value == null )
    {
      value = Boolean.FALSE;
    }

    if( isNot() )
    {
      return CommonServices.getCoercionManager().makePrimitiveBooleanFrom( value ) ? Boolean.FALSE : Boolean.TRUE;
    }
    else if( isBitNot() )
    {
      IType type = getExpression().getType();
      if( type == JavaTypes.pBOOLEAN() )
      {
        Object obj = !CommonServices.getCoercionManager().makeBooleanFrom( value );
        return CommonServices.getCoercionManager().makePrimitiveBooleanFrom( obj );
      }
      if( type == JavaTypes.pINT() )
      {
        int d = ~CommonServices.getCoercionManager().makeIntegerFrom( value );
        return Integer.valueOf( d );
      }
      if( type == JavaTypes.pLONG() )
      {
        return makeLong( ~CommonServices.getCoercionManager().makeLongFrom( value ) );
      }
      if( type == JavaTypes.pSHORT() )
      {
        int d = ~CommonServices.getCoercionManager().makeIntegerFrom( value );
        return Integer.valueOf( d ).shortValue();
      }
      if( type == JavaTypes.pBYTE() )
      {
        return (byte)( ~(CommonServices.getCoercionManager().makeIntegerFrom( value ).intValue()) );
      }
      if( type == JavaTypes.pDOUBLE() )
      {
        return Double.longBitsToDouble( ~Double.doubleToRawLongBits( makeDoubleValue( value ) ) );
      }
      if( type == JavaTypes.pFLOAT() )
      {
        return Float.intBitsToFloat( ~Float.floatToRawIntBits( makeFloatValue( value ) ) );
      }
      throw new UnsupportedNumberTypeException(type);
    }
    else
    {
      return value;
    }
  }

  @Override
  public String toString()
  {
    return (isNot() ? "!" : "~" ) + getExpression().toString();
  }

}
