/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.IDimension;
import gw.lang.parser.expressions.IUnaryExpression;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.config.CommonServices;

/**
 * Represents a unary expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class UnaryExpression extends Expression implements IUnaryExpression
{
  protected boolean _bNegated;
  protected Expression _expression;

  public boolean isNegated()
  {
    return _bNegated;
  }

  public void setNegated( boolean bNegated )
  {
    _bNegated = bNegated;
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
      return null;
    }

    IType type = getExpression().getType();

    if( isNegated() )
    {
      IDimension dimension = null;
      if( JavaTypes.IDIMENSION().isAssignableFrom( type ) )
      {
        dimension = (IDimension)value;
        type = TypeSystem.get( ((IDimension)value).numberType() );
        value = ((IDimension)value).toNumber();
      }

      if( type == JavaTypes.BIG_DECIMAL() )
      {
        value = CommonServices.getCoercionManager().makeBigDecimalFrom( value ).negate();
      }
      else if( type == JavaTypes.BIG_INTEGER() )
      {
        value = CommonServices.getCoercionManager().makeBigIntegerFrom( value ).negate();
      }
      else if( type == JavaTypes.INTEGER() || type == JavaTypes.pINT() )
      {
        value = makeInteger( -CommonServices.getCoercionManager().makeIntegerFrom( value ) );
      }
      else if( type == JavaTypes.LONG() || type == JavaTypes.pLONG() )
      {
        value = makeLong( -CommonServices.getCoercionManager().makeLongFrom( value ) );
      }
      else if( type == JavaTypes.DOUBLE() || type == JavaTypes.pDOUBLE() )
      {
        value = makeDoubleValue( -makeDoubleValue( value ) );
      }
      else if( type == JavaTypes.FLOAT() || type == JavaTypes.pFLOAT() )
      {
        value = makeFloatValue( -makeFloatValue( value ) );
      }
      else if( type == JavaTypes.SHORT() || type == JavaTypes.pSHORT() )
      {
        value = makeInteger( -CommonServices.getCoercionManager().makeIntegerFrom( value ) ).shortValue();
      }
      else if( type == JavaTypes.BYTE() || type == JavaTypes.pBYTE() )
      {
        value = makeInteger( -CommonServices.getCoercionManager().makeIntegerFrom( value ) ).byteValue();
      }
      else
      {
        throw new UnsupportedNumberTypeException(type);
      }

      if( dimension != null )
      {
        //noinspection unchecked
        value = dimension.fromNumber( (Number)value );
      }
    }
    return value;
  }

  public boolean isSupportedType( IType type )
  {
    return
      type == JavaTypes.BIG_DECIMAL() ||
      type == JavaTypes.BIG_INTEGER() ||
      type == JavaTypes.INTEGER() || type == JavaTypes.pINT() ||
      type == JavaTypes.LONG() || type == JavaTypes.pLONG() ||
      type == JavaTypes.DOUBLE() || type == JavaTypes.pDOUBLE() ||
      type == JavaTypes.FLOAT() || type == JavaTypes.pFLOAT() ||
      type == JavaTypes.SHORT() || type == JavaTypes.pSHORT() ||
      type == JavaTypes.BYTE() || type == JavaTypes.pBYTE() ||
      type == JavaTypes.CHARACTER() || type == JavaTypes.pCHAR() ||
      JavaTypes.IDIMENSION().isAssignableFrom( type ) ||
      (type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder());
  }

  @Override
  public String toString()
  {
    return (isNegated() ? "-" : "+" ) + getExpression().toString();
  }

}
