/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.IType;

/**
 */
public class TypeLiteralTransformer extends AbstractExpressionTransformer<TypeLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, TypeLiteral expr )
  {
    TypeLiteralTransformer gen = new TypeLiteralTransformer( cc, expr );
    return gen.compile();
  }

  private TypeLiteralTransformer( TopLevelTransformationContext cc, TypeLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType type = _expr().getType().getType();

    // Push the IType
    return pushType( type );
  }
}
