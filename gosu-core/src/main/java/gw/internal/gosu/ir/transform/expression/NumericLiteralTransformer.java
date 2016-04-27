/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Arrays;

/**
 */
public class NumericLiteralTransformer extends AbstractExpressionTransformer<NumericLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, NumericLiteral expr )
  {
    NumericLiteralTransformer gen = new NumericLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private NumericLiteralTransformer( TopLevelTransformationContext cc, NumericLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    final IType type = _expr().getType();
    if( type == JavaTypes.BIG_INTEGER() )
    {
      Number value = _expr().getValue();
      if( value.equals( BigInteger.ZERO ) ||
          value.equals( BigDecimal.ZERO ) ||
          !(value instanceof BigInteger || value instanceof BigDecimal || value instanceof Rational) && value.longValue() == 0 )
      {
        return getStaticField( JavaTypes.BIG_INTEGER(), "ZERO", getDescriptor( JavaTypes.BIG_INTEGER() ), IRelativeTypeInfo.Accessibility.PUBLIC );
      }
      if( value.equals( BigInteger.ONE ) ||
          value.equals( BigDecimal.ONE ) ||
          !(value instanceof BigInteger || value instanceof BigDecimal || value instanceof Rational) && value.longValue() == 1 )
      {
        return getStaticField( JavaTypes.BIG_INTEGER(), "ONE", getDescriptor( JavaTypes.BIG_INTEGER() ), IRelativeTypeInfo.Accessibility.PUBLIC );
      }
      return buildNewExpression( getDescriptor( type ), Arrays.asList(IRTypeConstants.STRING()), Collections.singletonList( stringLiteral( _expr().getValue().toString() ) ) );
    }
    else if( type == JavaTypes.BIG_DECIMAL() )
    {
      Number value = _expr().getValue();
      if( value.equals( BigInteger.ZERO ) ||
          value.equals( BigDecimal.ZERO ) ||
          !(value instanceof BigInteger || value instanceof BigDecimal || value instanceof Rational) && value.longValue() == 0 )
      {
        return getStaticField( JavaTypes.BIG_DECIMAL(), "ZERO", getDescriptor( JavaTypes.BIG_DECIMAL() ), IRelativeTypeInfo.Accessibility.PUBLIC );
      }
      if( value.equals( BigInteger.ONE ) ||
          value.equals( BigDecimal.ONE ) ||
          !(value instanceof BigInteger || value instanceof BigDecimal || value instanceof Rational) && value.longValue() == 1 )
      {
        return getStaticField( JavaTypes.BIG_DECIMAL(), "ONE", getDescriptor( JavaTypes.BIG_DECIMAL() ), IRelativeTypeInfo.Accessibility.PUBLIC );
      }
      return buildNewExpression( getDescriptor( type ), Arrays.asList(IRTypeConstants.STRING()), Collections.singletonList( stringLiteral( _expr().getValue().toString() ) ) );
    }
    else if( type == JavaTypes.RATIONAL() )
    {
      Number value = _expr().getValue();
      if( value instanceof Rational )
      {
        if( ((Rational)value).compareTo( Rational.ZERO ) == 0 )
        {
          return getStaticField( JavaTypes.RATIONAL(), "ZERO", getDescriptor( JavaTypes.RATIONAL() ), IRelativeTypeInfo.Accessibility.PUBLIC );
        }
        else if( ((Rational)value).compareTo( Rational.ONE ) == 0 )
        {
          return getStaticField( JavaTypes.RATIONAL(), "ONE", getDescriptor( JavaTypes.RATIONAL() ), IRelativeTypeInfo.Accessibility.PUBLIC );
        }
        else if( ((Rational)value).isInteger() )
        {
          BigInteger numerator = ((Rational)value).getNumerator();
          if( numerator.compareTo( BigInteger.valueOf( Long.MAX_VALUE ) ) <= 0 &&
              numerator.compareTo( BigInteger.valueOf( Long.MIN_VALUE ) ) >= 0 )
          {
            return buildMethodCall( Rational.class, "get", Rational.class, new Class[]{long.class}, null,
                                    Collections.singletonList( pushConstant( numerator.longValue() ) ) );
          }
          else
          {
            return buildMethodCall( Rational.class, "get", Rational.class, new Class[]{BigInteger.class}, null,
                                    Collections.singletonList( buildNewExpression( BigInteger.class, new Class[]{String.class},
                                                                                   Collections.singletonList( pushConstant( ((Rational)value).getNumerator().toString() ) ) ) ) );
          }
        }
        else
        {
          BigInteger numerator = ((Rational)value).getNumerator();
          BigInteger denominator = ((Rational)value).getDenominator();
          if( numerator.compareTo( BigInteger.valueOf( Long.MAX_VALUE ) ) <= 0 &&
              numerator.compareTo( BigInteger.valueOf( Long.MIN_VALUE ) ) >= 0 &&
              denominator.compareTo( BigInteger.valueOf( Long.MAX_VALUE ) ) <= 0 &&
              denominator.compareTo( BigInteger.valueOf( Long.MIN_VALUE ) ) >= 0 )
          {
            return buildMethodCall( Rational.class, "get", Rational.class, new Class[]{long.class, long.class}, null,
                                    Arrays.asList( pushConstant( numerator.longValue() ), pushConstant( denominator.longValue() ) ) );
          }
          else
          {
            return buildMethodCall( Rational.class, "get", Rational.class, new Class[]{BigInteger.class, BigInteger.class}, null,
                                    Arrays.asList( buildNewExpression( BigInteger.class, new Class[]{String.class},
                                                                       Collections.singletonList( pushConstant( numerator.toString() ) ) ),
                                                   buildNewExpression( BigInteger.class, new Class[]{String.class},
                                                                       Collections.singletonList( pushConstant( denominator.toString() ) ) ) ) );
          }
        }
      }
      return buildMethodCall( getDescriptor( type ), "get", false, getDescriptor( JavaTypes.RATIONAL() ), Collections.singletonList( IRTypeConstants.STRING() ), null, Collections.singletonList( stringLiteral( _expr().getValue().toString() ) ) );
    }
    else
    {
      Number value = getValueOfProperType( type );
      IRExpression expression = pushConstant( value );
      if( !type.isPrimitive() )
      {
        expression = boxValueToType( type, expression );
      }
      return expression;
    }
  }

  private Number getValueOfProperType( IType type )
  {
    Number value = _expr().getValue();
    if( type == JavaTypes.pDOUBLE() || type == JavaTypes.DOUBLE() )
    {
      value = new Double( value.doubleValue() );
    }
    if( type == JavaTypes.pLONG() || type == JavaTypes.LONG() )
    {
      value = new Long( value.longValue() );
    }
    return value;
  }
}
