/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ArrayExpansionPropertyInfo;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.MemberExpansionAccess;
import gw.lang.ir.IRExpression;
import gw.lang.parser.MemberAccessKind;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

/**
 */
public class MemberExpansionAccessTransformer extends AbstractMemberExpansionTransformer<MemberAccess>
{
  public static IRExpression compile( TopLevelTransformationContext cc, MemberExpansionAccess expr )
  {
    MemberExpansionAccessTransformer compiler = new MemberExpansionAccessTransformer( cc, expr );
    return compiler.compile();
  }

  private MemberExpansionAccessTransformer( TopLevelTransformationContext cc, MemberExpansionAccess expr )
  {
    super( cc, expr );
  }

  @Override
  protected IRExpression createIterationExpr( IType rootComponentType, String irIdentifierSym, IType identifierType, IType compType )
  {
    // Make MemberAccessExpr for *temp_mae_X.<property>

    IType iterExprType = getPropertyOrMethodType( rootComponentType, compType );
    MemberAccess ma;
    ma = isNestedExpansion( rootComponentType ) ? new MemberExpansionAccess() : new MemberAccess();
    ma.setType( iterExprType );
    ma.setMemberAccessKind( MemberAccessKind.NULL_SAFE ); // expansion implies null-safety on elements
    Identifier id = new Identifier();
    id.setType( rootComponentType );
    StandardSymbolTable symTable = new StandardSymbolTable();
    Symbol identifierSym = new Symbol( irIdentifierSym, identifierType, symTable );
    id.setSymbol( identifierSym, symTable );
    ma.setRootExpression( id );
    ma.setMemberName( _expr().getMemberName() );

    return ExpressionTransformer.compile( ma, _cc() );
  }

  private boolean isNestedExpansion( IType rootComponentType )
  {
    IPropertyInfo pi = _expr().getPropertyInfo();
    if( pi instanceof ArrayExpansionPropertyInfo )
    {
      pi = ((ArrayExpansionPropertyInfo)pi).getDelegate();
    }
    IType type = pi.getFeatureType();
    return rootComponentType != TypeLord.getExpandableComponentType( rootComponentType );
  }

  @Override
  protected IType getPropertyOrMethodType( IType rootComponentType, IType compType )
  {
    IPropertyInfo pi = _expr().getPropertyInfo();
    if( pi instanceof ArrayExpansionPropertyInfo )
    {
      pi = ((ArrayExpansionPropertyInfo)pi).getDelegate();
    }
    IType type = pi.getFeatureType();
    if( rootComponentType != TypeLord.getExpandableComponentType( rootComponentType ) )
    {
      if( type.isArray() )
      {
        return type;
      }
      return type.getArrayType();
    }
    return type;
  }
}