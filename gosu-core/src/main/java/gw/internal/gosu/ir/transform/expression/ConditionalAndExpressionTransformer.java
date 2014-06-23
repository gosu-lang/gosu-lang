/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.ConditionalAndExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.expression.IRConditionalAndExpression;

/**
 */
public class ConditionalAndExpressionTransformer extends AbstractExpressionTransformer<ConditionalAndExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ConditionalAndExpression expr )
  {
    ConditionalAndExpressionTransformer gen = new ConditionalAndExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private ConditionalAndExpressionTransformer( TopLevelTransformationContext cc, ConditionalAndExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression lhs = IRArgConverter.castOrConvertIfNecessary(IRTypeConstants.pBOOLEAN(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRExpression rhs = IRArgConverter.castOrConvertIfNecessary(IRTypeConstants.pBOOLEAN(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    return new IRConditionalAndExpression( lhs, rhs );
  }

}
