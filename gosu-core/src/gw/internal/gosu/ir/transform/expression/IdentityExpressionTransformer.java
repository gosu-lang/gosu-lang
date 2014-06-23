/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ParserBase;
import gw.internal.gosu.parser.expressions.IdentityExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.reflect.IType;

/**
 */
public class IdentityExpressionTransformer extends AbstractExpressionTransformer<IdentityExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, IdentityExpression expr )
  {
    IdentityExpressionTransformer gen = new IdentityExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private IdentityExpressionTransformer( TopLevelTransformationContext cc, IdentityExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();

    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    // If we have primitives, make sure to convert them to the upper-bound type first
    if( lhsType.isPrimitive() && rhsType.isPrimitive() )
    {
      IType type = ParserBase.resolveType( lhsType, '>', rhsType );
      if( type.isPrimitive() )
      {
        lhs = numberConvert( _expr().getLHS().getType(), type, lhs );
        rhs = numberConvert( _expr().getLHS().getType(), type, rhs );
      }
    }
    else if( lhsType.isPrimitive() )
    {
      lhs = boxValue( lhsType, lhs );
    }
    else if( rhsType.isPrimitive() )
    {
      rhs = boxValue( rhsType, rhs );
    }

    return new IREqualityExpression( lhs, rhs, _expr().isEquals() );
  }
}
