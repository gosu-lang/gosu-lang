/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BitwiseOrExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.IRExpression;

/**
 */
public class BitwiseOrExpressionTransformer extends AbstractBitwiseExpressionTransformer<BitwiseOrExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BitwiseOrExpression expr )
  {
    BitwiseOrExpressionTransformer gen = new BitwiseOrExpressionTransformer( cc, expr );
    return gen.compile();
  }

  protected BitwiseOrExpressionTransformer( TopLevelTransformationContext cc, BitwiseOrExpression expr )
  {
    super( cc, expr );
  }

  @Override
  protected IRArithmeticExpression.Operation getOp() {
    return IRArithmeticExpression.Operation.BitwiseOr;
  }
}