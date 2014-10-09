/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.ArithmeticExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public abstract class AbstractBitwiseExpressionTransformer<T extends ArithmeticExpression> extends AbstractExpressionTransformer<T>
{
  protected AbstractBitwiseExpressionTransformer( TopLevelTransformationContext cc, T expr )
  {
    super( cc, expr );
  }

  protected abstract IRArithmeticExpression.Operation getOp();

  protected IRExpression compile_impl()
  {
    // Push LHS (parser guarantees type is int or long)
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );

    // Push RHS (parser guarantees type is int)
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    verifyTypes();

    return new IRArithmeticExpression( getDescriptor( _expr().getType() ), lhs, rhs, getOp() );
  }

  private void verifyTypes()
  {
    if( _expr().getLHS().getType() != _expr().getRHS().getType() )
    {
      throw new IllegalStateException( "Bitwise operands are not the same type:" +
                                       " lhs=" + _expr().getLHS().getType().getName() +
                                       " rhs=" + _expr().getRHS().getType().getName() );
    }
    if( _expr().getLHS().getType() != JavaTypes.pINT() && _expr().getLHS().getType() != JavaTypes.pLONG() )
    {
      throw new IllegalStateException( "Operands must be int or long, found: " + _expr().getLHS().getType().getName() );
    }
  }
}
