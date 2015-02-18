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

    MemberAccess ma;
    ma = new MemberAccess();
    // We need to set the type of the MemberAccess to exactly the type of the property in question
    ma.setType( getPropertyOrMethodType( rootComponentType, compType ) );
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