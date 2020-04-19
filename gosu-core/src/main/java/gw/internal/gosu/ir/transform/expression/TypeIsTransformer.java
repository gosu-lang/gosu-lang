/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.TypeIsExpression;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;

/**
 */
public class TypeIsTransformer extends AbstractExpressionTransformer<TypeIsExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, TypeIsExpression expr )
  {
    TypeIsTransformer gen = new TypeIsTransformer( cc, expr );
    return gen.compile();
  }

  private TypeIsTransformer( TopLevelTransformationContext cc, TypeIsExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().evaluate();
    if( lhsType.isPrimitive() || rhsType.isPrimitive() )
    {
      throw new IllegalStateException( "Primitive types not supported in typeis expression" );
    }
    if( !isStructure( rhsType ) &&
        (!rhsType.isParameterizedType() ||
         rhsType == TypeLord.getDefaultParameterizedType( rhsType )) &&
        (rhsType instanceof IGosuClassInternal ||
         rhsType instanceof IJavaType) &&
        (lhsType instanceof IGosuClassInternal ||
         lhsType instanceof IJavaType) )
    {
      // Perf: use the INSTANCEOF bytecode when we are dealing with Java-based types that are not parameterized
      // Note Gosu array types don't retain type info at runtime, so those can use instanceof regardless
      IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
      return new IRInstanceOfExpression( lhs, getDescriptor( TypeLord.getPureGenericType( rhsType ) ) );
    }

    // We want to short-circuit to false if the lhs evaluates to null, so we generate code that looks like the following:
    // temp = lhs
    // return (temp == null ? false : rhs.isAssignableFrom(TypeSystem.getFromObject(temp)).booleanValue())
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRSymbol temp = _cc().makeAndIndexTempSymbol( lhs.getType() );

    IRExpression getTypeCall = callStaticMethod( TypeSystem.class, "getFromObject", new Class[]{Object.class},
            exprList( identifier( temp ) ) );

    IRExpression isStructurallyAssignableCall = callStaticMethod(GosuRuntimeMethods.class, "isStructurallyAssignable", new Class[]{IType.class, IType.class},
            exprList(ExpressionTransformer.compile(_expr().getRHS(), _cc()), getTypeCall));
    return buildComposite(
            buildAssignment( temp, lhs ),
            isStructurallyAssignableCall );
  }

  private boolean isStructure(IType type)
  {
    return type instanceof IGosuClass && ((IGosuClass)type).isStructure() ||
           type instanceof IJavaType && ((IJavaType)type).isStructure();
  }
}
