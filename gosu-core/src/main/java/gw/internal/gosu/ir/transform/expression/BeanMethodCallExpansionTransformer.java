/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ArrayExpansionMethodInfo;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.lang.ir.IRExpression;
import gw.lang.parser.MemberAccessKind;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

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
  protected IRExpression createIterationExpr( IType rootComponentType, String identifierName, IType identifierType, IType compType )
  {
    // Make BeanMethodCallExpressionExpr for identifierSym.<call()>
    BeanMethodCallExpression mc = new BeanMethodCallExpression();
    Identifier id = new Identifier();
    id.setType( rootComponentType );
    StandardSymbolTable symTable = new StandardSymbolTable();
    id.setSymbol( new Symbol( identifierName, identifierType, symTable ), symTable );
    mc.setRootExpression( id );
    mc.setMethodDescriptor( _expr().getMethodDescriptor() instanceof ArrayExpansionMethodInfo
                            ? ((ArrayExpansionMethodInfo)_expr().getMethodDescriptor()).getDelegate()
                            : _expr().getMethodDescriptor());
    mc.setAccessPath( _expr().getAccessPath() );
    mc.setArgs( _expr().getArgs() );
    mc.setArgTypes( _expr().getArgTypes() );
    mc.setFunctionType( _expr().getFunctionType() );
    mc.setType( getPropertyOrMethodType( rootComponentType, compType ) );
    mc.setMemberAccessKind( rootComponentType != TypeLord.getExpandableComponentType( rootComponentType )
                            ? MemberAccessKind.EXPANSION
                            : MemberAccessKind.NULL_SAFE );
    return ExpressionTransformer.compile( mc, _cc() );
  }

  @Override
  protected IType getPropertyOrMethodType( IType rootComponentType, IType compType )
  {
    IMethodInfo mi = _expr().getMethodDescriptor();
    if( mi instanceof ArrayExpansionMethodInfo )
    {
      mi = ((ArrayExpansionMethodInfo)mi).getDelegate();
    }
    IType type = mi.getReturnType();
    if( rootComponentType != TypeLord.getExpandableComponentType( rootComponentType ) )
    {
      if( type.isArray() || type == JavaTypes.pVOID() )
      {
        return type;
      }
      return type.getArrayType();
    }
    return type;
  }
}