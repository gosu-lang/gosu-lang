/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.ConditionalTernaryExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;

/**
 */
public class ConditionalTernaryExpressionTransformer extends AbstractExpressionTransformer<ConditionalTernaryExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ConditionalTernaryExpression expr )
  {
    ConditionalTernaryExpressionTransformer compiler = new ConditionalTernaryExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private ConditionalTernaryExpressionTransformer( TopLevelTransformationContext cc, ConditionalTernaryExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression condition;
    if( _expr().getCondition() == null )
    {
      // Handle the "elvis" version of the ternary
      // e.g., first ?: second  which is short for:  first != null ? first : second
      condition = buildNotEquals( ExpressionTransformer.compile( _expr().getFirst(), _cc() ), nullLiteral() );
    }
    else
    {
      condition = ExpressionTransformer.compile( _expr().getCondition(), _cc() );
    }
    return buildTernary(
      condition,
      ExpressionTransformer.compile( _expr().getFirst(), _cc() ),
      ExpressionTransformer.compile( _expr().getSecond(), _cc() ),
      getDescriptor( _expr().getType() )
    );
  }
}
