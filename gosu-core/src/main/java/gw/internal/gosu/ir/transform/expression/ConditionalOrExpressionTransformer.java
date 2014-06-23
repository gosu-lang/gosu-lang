/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.ConditionalOrExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.expression.IRConditionalOrExpression;

/**
 */
public class ConditionalOrExpressionTransformer extends AbstractExpressionTransformer<ConditionalOrExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ConditionalOrExpression expr )
  {
    ConditionalOrExpressionTransformer gen = new ConditionalOrExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private ConditionalOrExpressionTransformer( TopLevelTransformationContext cc, ConditionalOrExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression lhs = IRArgConverter.castOrConvertIfNecessary(IRTypeConstants.pBOOLEAN(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRExpression rhs = IRArgConverter.castOrConvertIfNecessary(IRTypeConstants.pBOOLEAN(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    return new IRConditionalOrExpression( lhs, rhs );
  }

}
