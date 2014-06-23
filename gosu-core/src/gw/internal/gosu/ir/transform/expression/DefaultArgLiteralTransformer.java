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
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

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
        if( primType != null )
        {
          expression = boxValue( type, expression );
        }
      }
    }
    return expression;
  }
}
