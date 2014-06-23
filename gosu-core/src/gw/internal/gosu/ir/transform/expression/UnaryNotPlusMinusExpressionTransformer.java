/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.UnaryNotPlusMinusExpression;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;

/**
 */
public class UnaryNotPlusMinusExpressionTransformer extends AbstractExpressionTransformer<UnaryNotPlusMinusExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, UnaryNotPlusMinusExpression expr )
  {
    UnaryNotPlusMinusExpressionTransformer gen = new UnaryNotPlusMinusExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private UnaryNotPlusMinusExpressionTransformer( TopLevelTransformationContext cc, UnaryNotPlusMinusExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression root = ExpressionTransformer.compile( _expr().getExpression(), _cc() );

    IType type = _expr().getExpression().getType();
    if( _expr().isNot() )
    {
      if( type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder() )
      {
        return callStaticMethod( GosuRuntimeMethods.class, "logicalNot", new Class[] {Object.class}, Collections.singletonList( root ) );
      }
      else if( type != JavaTypes.pBOOLEAN() )
      {
        throw new IllegalStateException( "Logical not operator '!' requires boolean operand. Found: " + type );
      }
    }
    else if( _expr().isBitNot() )
    {
      if( type != JavaTypes.pINT() && type != JavaTypes.pLONG())
      {
        throw new IllegalStateException( "Bitwise not operator '~' requires int or long operand. Found: " + type );
      }
    }

    return new IRNotExpression( root );
  }
}
