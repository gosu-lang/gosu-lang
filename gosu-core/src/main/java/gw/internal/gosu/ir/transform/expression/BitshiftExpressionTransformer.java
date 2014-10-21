/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BitshiftExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class BitshiftExpressionTransformer extends AbstractExpressionTransformer<BitshiftExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BitshiftExpression expr )
  {
    BitshiftExpressionTransformer gen = new BitshiftExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private BitshiftExpressionTransformer( TopLevelTransformationContext cc, BitshiftExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType type = _expr().getType();


    // Push LHS (parser guarantees type is int or long)
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );

    // Push RHS (parser guarantees type is int)
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    // Do the bitshift
    IRArithmeticExpression.Operation op;
    if( _expr().getOperator().equals( "<<" ) ||
        _expr().getOperator().equals( "<<=" ) )
    {
      op = IRArithmeticExpression.Operation.ShiftLeft;
    }
    else if( _expr().getOperator().equals( ">>" ) ||
             _expr().getOperator().equals( ">>=") )
    {
      op = IRArithmeticExpression.Operation.ShiftRight;
    }
    else if( _expr().getOperator().equals( ">>>" ) ||
             _expr().getOperator().equals( ">>>=" ) )
    {
      op = IRArithmeticExpression.Operation.UnsignedShiftRight;
    }
    else
    {
      throw new IllegalStateException("Unrecognized BitshiftExpression operator " + _expr().getOperator());
    }

    if( isIntType( type ) || type == JavaTypes.pLONG() )
    {
      return new IRArithmeticExpression( getDescriptor( type ), lhs, rhs, op);
    }
    else
    {
      throw new IllegalStateException( "Expression type not handled: " + type.getName() );
    }
  }
}
