/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BooleanLiteral;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class BooleanLiteralTransformer extends AbstractExpressionTransformer<BooleanLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BooleanLiteral expr )
  {
    BooleanLiteralTransformer gen = new BooleanLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private BooleanLiteralTransformer( TopLevelTransformationContext cc, BooleanLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression expression = pushConstant( _expr().getValue() );

    if( !_expr().getType().isPrimitive() )
    {
      expression = boxValue( JavaTypes.pBOOLEAN(), expression );
    }
    return expression;
  }
}
