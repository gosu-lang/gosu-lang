/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BitwiseXorExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.IRExpression;

/**
 */
public class BitwiseXorExpressionTransformer extends AbstractBitwiseExpressionTransformer<BitwiseXorExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BitwiseXorExpression expr )
  {
    BitwiseXorExpressionTransformer gen = new BitwiseXorExpressionTransformer( cc, expr );
    return gen.compile();
  }

  protected BitwiseXorExpressionTransformer( TopLevelTransformationContext cc, BitwiseXorExpression expr )
  {
    super( cc, expr );
  }

  @Override
  protected IRArithmeticExpression.Operation getOp() {
    return IRArithmeticExpression.Operation.BitwiseXor;
  }
}