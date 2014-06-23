/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

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
    if( type == JavaTypes.BIG_DECIMAL() ||
        type == JavaTypes.BIG_INTEGER() )
    {
      return buildNewExpression( getDescriptor( type ), Arrays.asList(IRTypeConstants.STRING()), Collections.singletonList( stringLiteral( _expr().getValue().toString() ) ) );
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
