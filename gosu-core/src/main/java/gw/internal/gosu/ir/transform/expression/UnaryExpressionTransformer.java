/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.expressions.UnaryExpression;
import gw.internal.gosu.parser.expressions.UnsupportedNumberTypeException;
import gw.lang.IDimension;
import gw.lang.ir.IRExpression;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 */
public class UnaryExpressionTransformer extends AbstractExpressionTransformer<UnaryExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, UnaryExpression expr )
  {
    UnaryExpressionTransformer gen = new UnaryExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private UnaryExpressionTransformer( TopLevelTransformationContext cc, UnaryExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression root = ExpressionTransformer.compile( _expr().getExpression(), _cc() );

    if( _expr().getType().isPrimitive() && BeanAccess.isNumericType( _expr().getType() ) )
    {
      return negateSimple( root );
    }
    else
    {
      return negateComplex(root );
    }
  }

  private IRExpression negateSimple( IRExpression root )
  {
    if (_expr().isNegated()) {
      return buildNegation( root );
    } else {
      // Nothing to do if it's not a negation
      return root;
    }
  }

  private IRExpression negateComplex( IRExpression root )
  {
    // Call into Gosu's runtime for the answer
    IRExpression negateCall = callStaticMethod( getClass(), "negateComplex", new Class[]{Object.class, boolean.class},
            exprList( boxValue( _expr().getExpression().getType(), root ),
                      pushConstant( _expr().isNegated() ) ) );

    // Ensure value is unboxed if type is primitive
    return unboxValueToType( _expr().getType(), negateCall );
  }

  public static Object negateComplex( Object value, boolean bNegated )
  {
    if( value == null )
    {
      return null;
    }

    if( bNegated )
    {
      IDimension dimension = null;
      if( value instanceof IDimension )
      {
        dimension = (IDimension)value;
        value = ((IDimension)value).toNumber();
      }

      if( value instanceof BigDecimal )
      {
        value = ((BigDecimal)value).negate();
      }
      else if( value instanceof BigInteger )
      {
        value = ((BigInteger)value).negate();
      }
      else if( value instanceof Integer )
      {
        value = -(Integer)value;
      }
      else if( value instanceof Long )
      {
        value = -(Long)value;
      }
      else if( value instanceof Double )
      {
        value = -(Double)value;
      }
      else if( value instanceof Float )
      {
        value = -(Float)value;
      }
      else if( value instanceof Short )
      {
        value = Short.valueOf( (short)-((Short)value).shortValue() );
      }
      else if( value instanceof Byte )
      {
        value = Byte.valueOf( (byte)-((Byte)value).byteValue() );
      }
      else
      {
        throw new UnsupportedNumberTypeException( value.getClass() );
      }

      if( dimension != null )
      {
        //noinspection unchecked
        value = dimension.fromNumber( (Number)value );
      }
    }
    return value;
  }

}
