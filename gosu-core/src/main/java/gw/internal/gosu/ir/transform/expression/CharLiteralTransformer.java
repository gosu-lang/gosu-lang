/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.CharLiteral;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class CharLiteralTransformer extends AbstractExpressionTransformer<CharLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, CharLiteral expr )
  {
    CharLiteralTransformer gen = new CharLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private CharLiteralTransformer( TopLevelTransformationContext cc, CharLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression expression = pushConstant( _expr().getValue() );

    if( !_expr().getType().isPrimitive() )
    {
      expression = boxValue( JavaTypes.pCHAR(), expression );
    }
    return expression;
  }
}
