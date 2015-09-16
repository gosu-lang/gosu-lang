/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.expressions.DefaultArgLiteral;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

/**
 */
public class DefaultArgLiteralTransformer extends AbstractExpressionTransformer<DefaultArgLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, DefaultArgLiteral expr )
  {
    DefaultArgLiteralTransformer gen = new DefaultArgLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private DefaultArgLiteralTransformer( TopLevelTransformationContext cc, DefaultArgLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression expression;

    Object value = _expr().getValue();
    IType type = _expr().getType();
    if( value == null )
    {
      expression = pushNull();
    }
    else if( type.isEnum() )
    {
      expression = new IRFieldGetExpression( null, (String)value, getDescriptor( type ), getDescriptor( type ) );
    }
    else if( value.getClass().isArray() )
    {
      expression = NewExpressionTransformer.compile( _cc(), (NewExpression)_expr().getExpression() );
    }
    else if( MetaType.class.isAssignableFrom( type.getClass() ) )
    {
      expression = TypeLiteralTransformer.compile( _cc(), (TypeLiteral)_expr().getExpression() );
    }
    else
    {
      expression = pushConstant( value );

      if( !type.isPrimitive() )
      {
        IType primType = TypeSystem.getPrimitiveType( type );
        if( primType != null && StandardCoercionManager.isBoxed( type ) )
        {
          expression = boxValue( primType, expression );
        }
        else if( type == JavaTypes.BIG_DECIMAL() )
        {
          IRType bd = getDescriptor( BigDecimal.class );
          if( BigDecimal.ZERO.compareTo( new BigDecimal( (String)value ) ) == 0 )
          {
            return buildFieldGet( bd, "ZERO", bd, null );
          }
          return buildNewExpression( bd, Collections.singletonList( IRTypeConstants.STRING() ), Collections.singletonList( pushConstant( value ) ) );
        }
        else if( type == JavaTypes.BIG_INTEGER() )
        {
          IRType bd = getDescriptor( BigInteger.class );
          if( BigInteger.ZERO.compareTo( new BigInteger( (String)value ) ) == 0 )
          {
            return buildFieldGet( bd, "ZERO", bd, null );
          }
          return buildNewExpression( bd, Collections.singletonList( IRTypeConstants.STRING() ), Collections.singletonList( pushConstant( value ) ) );
        }
      }
    }
    return expression;
  }
}
