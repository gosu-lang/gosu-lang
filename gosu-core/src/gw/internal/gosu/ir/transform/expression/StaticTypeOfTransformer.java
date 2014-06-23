/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.StaticTypeOfExpression;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.parser.IExpression;

/**
 */
public class StaticTypeOfTransformer extends AbstractExpressionTransformer<StaticTypeOfExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, StaticTypeOfExpression expr )
  {
    StaticTypeOfTransformer gen = new StaticTypeOfTransformer( cc, expr );
    return gen.compile();
  }

  private StaticTypeOfTransformer( TopLevelTransformationContext cc, StaticTypeOfExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IExpression rhs = _expr().getExpression();
    return pushType( rhs.getType() );
  }
}
