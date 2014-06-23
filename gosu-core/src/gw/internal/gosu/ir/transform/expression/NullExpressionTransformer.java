/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;

public class NullExpressionTransformer extends AbstractExpressionTransformer<NullExpression> {

  public static IRExpression compile( TopLevelTransformationContext cc, NullExpression expr )
  {
    NullExpressionTransformer compiler = new NullExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private NullExpressionTransformer( TopLevelTransformationContext cc, NullExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    return pushNull();
  }
}
