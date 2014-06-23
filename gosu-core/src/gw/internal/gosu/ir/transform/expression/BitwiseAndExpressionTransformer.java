/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BitwiseAndExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.IRExpression;

/**
 */
public class BitwiseAndExpressionTransformer extends AbstractBitwiseExpressionTransformer<BitwiseAndExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BitwiseAndExpression expr )
  {
    BitwiseAndExpressionTransformer gen = new BitwiseAndExpressionTransformer( cc, expr );
    return gen.compile();
  }

  protected BitwiseAndExpressionTransformer( TopLevelTransformationContext cc, BitwiseAndExpression expr )
  {
    super( cc, expr );
  }

  @Override
  protected IRArithmeticExpression.Operation getOp() {
    return IRArithmeticExpression.Operation.BitwiseAnd;
  }
}