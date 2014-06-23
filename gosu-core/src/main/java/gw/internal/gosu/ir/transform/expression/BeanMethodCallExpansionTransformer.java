/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.ArrayExpansionMethodInfo;
import gw.lang.parser.MemberAccessKind;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.parser.StandardSymbolTable;

/**
 */
public class BeanMethodCallExpansionTransformer extends AbstractMemberExpansionTransformer<BeanMethodCallExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BeanMethodCallExpression expr )
  {
    BeanMethodCallExpansionTransformer compiler = new BeanMethodCallExpansionTransformer( cc, expr );
    return compiler.compile();
  }

  private BeanMethodCallExpansionTransformer( TopLevelTransformationContext cc, BeanMethodCallExpression expr )
  {
    super( cc, expr );
  }

  @Override
  protected IRExpression createIterationExpr(IType rootComponentType, String identifierName, IType identifierType, IType compType)
  {
    // Make BeanMethodCallExpressionExpr for identifierSym.<call()>
    BeanMethodCallExpression mc = new BeanMethodCallExpression();
    Identifier id = new Identifier();
    id.setType( rootComponentType );
    StandardSymbolTable symTable = new StandardSymbolTable();
    id.setSymbol( new Symbol(identifierName, identifierType, symTable ), symTable);
    mc.setRootExpression( id );
    mc.setMethodDescriptor( ((ArrayExpansionMethodInfo)_expr().getMethodDescriptor()).getDelegate() );
    mc.setAccessPath( _expr().getAccessPath() );
    mc.setArgs( _expr().getArgs() );
    mc.setArgTypes( _expr().getArgTypes() );
    mc.setFunctionType( _expr().getFunctionType() );
    mc.setType( getMethodReturnType() );
    mc.setMemberAccessKind( MemberAccessKind.NULL_SAFE );
    return BeanMethodCallExpressionTransformer.compile( _cc(), mc );
  }

  @Override
  protected IType getPropertyOrMethodType(IType rootComponentType, IType compType) {
    return getMethodReturnType();
  }

  private IType getMethodReturnType() {
    IMethodInfo methodInfo = _expr().getMethodDescriptor();
    if (methodInfo instanceof ArrayExpansionMethodInfo) {
      methodInfo = ((ArrayExpansionMethodInfo) methodInfo).getDelegate();
    }

    return methodInfo.getReturnType();
  }
}