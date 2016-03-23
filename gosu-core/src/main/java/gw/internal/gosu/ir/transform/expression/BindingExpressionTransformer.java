/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRMethodFromMethodInfo;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.BindingExpression;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuMethodInfo;

import java.util.Collections;

/**
 */
public class BindingExpressionTransformer extends AbstractExpressionTransformer<BindingExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BindingExpression expr )
  {
    BindingExpressionTransformer compiler = new BindingExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private BindingExpressionTransformer( TopLevelTransformationContext cc, BindingExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    String bindMethodName = _expr().isPrefix() ? "prefixBind" : "postfixBind";
    Expression rhsExpr = _expr().isPrefix() ? _expr().getLhsExpr() : _expr().getRhsExpr();
    Expression lhsExpr = _expr().isPrefix() ? _expr().getRhsExpr() : _expr().getLhsExpr();

    IType lhsExprType = lhsExpr.getType();
    if( lhsExprType.isPrimitive() )
    {
      lhsExprType = TypeLord.getBoxedTypeFromPrimitiveType( lhsExprType );
    }
    IType rhsExprType = rhsExpr.getType();
    if( rhsExprType.isPrimitive() )
    {
      rhsExprType = TypeLord.getBoxedTypeFromPrimitiveType( rhsExprType );
    }
    IGosuMethodInfo bindMethod = (IGosuMethodInfo)rhsExprType.getTypeInfo().getCallableMethod( bindMethodName, lhsExprType );
    IType owner = TypeLord.getPureGenericType( bindMethod.getContainer().getOwnersType() );
    bindMethod = (IGosuMethodInfo)owner.getTypeInfo().getCallableMethod( bindMethodName, lhsExprType );
    FunctionType funcType = new FunctionType( bindMethod );
    funcType = funcType.getRuntimeType();
    IRExpression irLhs = ExpressionTransformer.compile( lhsExpr, _cc() );
    IRExpression irRhs = ExpressionTransformer.compile( rhsExpr, _cc() );
    IRMethodFromMethodInfo irMethod = IRMethodFactory.createIRMethod( bindMethod, funcType );
    IRExpression bindCall = callMethod( irMethod, boxValue( irRhs.getType(), irRhs ), Collections.singletonList( boxValue( irLhs.getType(), irLhs ) ) );
    return buildCast( getDescriptor( _expr().getType() ), bindCall );
  }
}
