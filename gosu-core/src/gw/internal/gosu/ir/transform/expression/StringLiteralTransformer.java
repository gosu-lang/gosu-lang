/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;

/**
 */
public class StringLiteralTransformer extends AbstractExpressionTransformer<StringLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, StringLiteral expr )
  {
    StringLiteralTransformer gen = new StringLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private StringLiteralTransformer( TopLevelTransformationContext cc, StringLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    return pushConstant( _expr().getValue() );
  }
}
