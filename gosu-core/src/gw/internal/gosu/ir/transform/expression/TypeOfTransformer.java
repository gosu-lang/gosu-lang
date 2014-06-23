/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.parser.expressions.ITypeOfExpression;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class TypeOfTransformer extends AbstractExpressionTransformer<ITypeOfExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ITypeOfExpression expr )
  {
    TypeOfTransformer gen = new TypeOfTransformer( cc, expr );
    return gen.compile();
  }

  private TypeOfTransformer( TopLevelTransformationContext cc, ITypeOfExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType rhsType = _expr().getExpression().getType();
    IRExpression rhs = ExpressionTransformer.compile( _expr().getExpression(), _cc() );

    if( rhsType.isPrimitive() )
    {
      // We evaluate the RHS, but just pop it off and push on the type since we know it statically
      return buildComposite(
              new IRSyntheticStatement( rhs ),
              pushType( rhsType ) );
    }
    else
    {
      // If it's not a primitive, we want to short-circuit to "void" if the expression is null, and otherwise to call
      // TypeSystem.getFromObject, so we end up with code that looks like:
      // temp = rhs
      // (temp == null ? TypeSystem.getByFullName( "void" ) : TypeSystem.getFromObject( temp) )
      IRSymbol temp = _cc().makeAndIndexTempSymbol( rhs.getType() );
      return buildComposite(
              buildAssignment( temp, rhs ),
              buildNullCheckTernary(
                      identifier( temp ),
                      pushType( JavaTypes.pVOID() ),
                      callStaticMethod( GosuRuntimeMethods.class, "typeof", new Class[]{ Object.class }, exprList( identifier( temp ) ) ) ) );
    }
  }
}
