/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.ir.transform.expression.AdditiveExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ArrayAccessTransformer;
import gw.internal.gosu.ir.transform.expression.BeanMethodCallExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BitshiftExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BitwiseAndExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BitwiseOrExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BitwiseXorExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BlockExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.BlockInvocationTransformer;
import gw.internal.gosu.ir.transform.expression.BooleanLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.CharLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.CollectionInitializerExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ConditionalAndExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ConditionalOrExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ConditionalTernaryExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.DefaultArgLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.EqualityExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.EvalExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ExistsExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.FeatureLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.IdentifierTransformer;
import gw.internal.gosu.ir.transform.expression.IdentityExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.IntervalExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.MapAccessTransformer;
import gw.internal.gosu.ir.transform.expression.MapInitializerExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.MemberAccessTransformer;
import gw.internal.gosu.ir.transform.expression.MemberExpansionAccessTransformer;
import gw.internal.gosu.ir.transform.expression.MethodCallExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.MultiplicativeExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.NewExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.NullExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.NumericLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.ObjectInitializerExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.ObjectLiteralExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.QueryExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.RelationalExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.StaticTypeOfTransformer;
import gw.internal.gosu.ir.transform.expression.StringLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.TemplateStringLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.TypeAsTransformer;
import gw.internal.gosu.ir.transform.expression.TypeIsTransformer;
import gw.internal.gosu.ir.transform.expression.TypeLiteralTransformer;
import gw.internal.gosu.ir.transform.expression.TypeOfTransformer;
import gw.internal.gosu.ir.transform.expression.UnaryExpressionTransformer;
import gw.internal.gosu.ir.transform.expression.UnaryNotPlusMinusExpressionTransformer;
import gw.internal.gosu.parser.ParenthesizedExpression;
import gw.internal.gosu.parser.expressions.AdditiveExpression;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.BitshiftExpression;
import gw.internal.gosu.parser.expressions.BitwiseAndExpression;
import gw.internal.gosu.parser.expressions.BitwiseOrExpression;
import gw.internal.gosu.parser.expressions.BitwiseXorExpression;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.BlockInvocation;
import gw.internal.gosu.parser.expressions.BooleanLiteral;
import gw.internal.gosu.parser.expressions.CharLiteral;
import gw.internal.gosu.parser.expressions.CollectionInitializerExpression;
import gw.internal.gosu.parser.expressions.ConditionalAndExpression;
import gw.internal.gosu.parser.expressions.ConditionalOrExpression;
import gw.internal.gosu.parser.expressions.ConditionalTernaryExpression;
import gw.internal.gosu.parser.expressions.DefaultArgLiteral;
import gw.internal.gosu.parser.expressions.EqualityExpression;
import gw.internal.gosu.parser.expressions.EvalExpression;
import gw.internal.gosu.parser.expressions.ExistsExpression;
import gw.internal.gosu.parser.expressions.FeatureLiteral;
import gw.internal.gosu.parser.expressions.IdentityExpression;
import gw.internal.gosu.parser.expressions.IntervalExpression;
import gw.internal.gosu.parser.expressions.MapAccess;
import gw.internal.gosu.parser.expressions.MapInitializerExpression;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.MemberExpansionAccess;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.expressions.MultiplicativeExpression;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.parser.expressions.ObjectInitializerExpression;
import gw.internal.gosu.parser.expressions.ObjectLiteralExpression;
import gw.internal.gosu.parser.expressions.QueryExpression;
import gw.internal.gosu.parser.expressions.RelationalExpression;
import gw.internal.gosu.parser.expressions.StaticTypeOfExpression;
import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.internal.gosu.parser.expressions.TemplateStringLiteral;
import gw.internal.gosu.parser.expressions.TypeIsExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.expressions.UnaryExpression;
import gw.internal.gosu.parser.expressions.UnaryNotPlusMinusExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.parser.expressions.ITypeOfExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 */
public class ExpressionTransformer
{
  private IExpression _expr;
  private TopLevelTransformationContext _cc;
  private static Map<IExpression, IRSymbol> _tempSymbolsForCompoundAssignment = new HashMap<IExpression, IRSymbol>();

  public static IRExpression compile( IExpression expr, TopLevelTransformationContext cc )
  {
    ExpressionTransformer gen = new ExpressionTransformer( expr, cc );
    return gen.compile();
  }

  public static List<IRStatement> compileInitializer( IExpression expr, TopLevelTransformationContext cc, IRExpression root )
  {
    ExpressionTransformer gen = new ExpressionTransformer( expr, cc );
    return gen.compileInitializer( root );
  }

  public static void addTempSymbolForCompoundAssignment( IExpression e, IRSymbol s )
  {
    _tempSymbolsForCompoundAssignment.put( e, s );
  }

  public static void clearTempSymbolForCompoundAssignment()
  {
    _tempSymbolsForCompoundAssignment.clear();
  }

  private ExpressionTransformer( IExpression expr, TopLevelTransformationContext cc )
  {
    _expr = expr;
    _cc = cc;
  }

  private IRExpression compile()
  {
    IRSymbol symbol = _tempSymbolsForCompoundAssignment.get( _expr );
    if(  symbol != null ) {
      return new IRIdentifier( symbol );
    }
    else if( _expr instanceof IIdentifierExpression )
    {
      return IdentifierTransformer.compile( _cc, (IIdentifierExpression)_expr );
    }
    else if( _expr instanceof ITypeAsExpression )
    {
      return TypeAsTransformer.compile( _cc, (ITypeAsExpression)_expr );
    }
    else if( _expr instanceof TypeIsExpression)
    {
      return TypeIsTransformer.compile( _cc, (TypeIsExpression)_expr );
    }
    else if( _expr instanceof ITypeOfExpression)
    {
      return TypeOfTransformer.compile( _cc, (ITypeOfExpression)_expr );
    }
    else if( _expr instanceof StaticTypeOfExpression)
    {
      return StaticTypeOfTransformer.compile( _cc, (StaticTypeOfExpression)_expr );
    }
    else if( _expr instanceof StringLiteral )
    {
      return StringLiteralTransformer.compile( _cc, (StringLiteral)_expr );
    }
    else if( _expr instanceof CharLiteral )
    {
      return CharLiteralTransformer.compile( _cc, (CharLiteral)_expr );
    }
    else if( _expr instanceof NumericLiteral )
    {
      return NumericLiteralTransformer.compile( _cc, (NumericLiteral)_expr );
    }
    else if( _expr instanceof TypeLiteral)
    {
      return TypeLiteralTransformer.compile( _cc, (TypeLiteral)_expr );
    }
    else if( _expr instanceof BooleanLiteral )
    {
      return BooleanLiteralTransformer.compile( _cc, (BooleanLiteral)_expr );
    }
    else if( _expr instanceof DefaultArgLiteral )
    {
      return DefaultArgLiteralTransformer.compile( _cc, (DefaultArgLiteral)_expr );
    }
    else if( _expr instanceof UnaryExpression)
    {
      return UnaryExpressionTransformer.compile( _cc, (UnaryExpression)_expr );
    }
    else if( _expr instanceof UnaryNotPlusMinusExpression)
    {
      return UnaryNotPlusMinusExpressionTransformer.compile( _cc, (UnaryNotPlusMinusExpression)_expr );
    }
    else if( _expr instanceof EqualityExpression)
    {
      return EqualityExpressionTransformer.compile( _cc, (EqualityExpression)_expr );
    }
    else if( _expr instanceof IdentityExpression)
    {
      return IdentityExpressionTransformer.compile( _cc, (IdentityExpression)_expr );
    }
    else if( _expr instanceof RelationalExpression )
    {
      return RelationalExpressionTransformer.compile( _cc, (RelationalExpression)_expr );
    }
    else if( _expr instanceof ConditionalOrExpression)
    {
      return ConditionalOrExpressionTransformer.compile( _cc, (ConditionalOrExpression)_expr );
    }
    else if( _expr instanceof ConditionalAndExpression)
    {
      return ConditionalAndExpressionTransformer.compile( _cc, (ConditionalAndExpression)_expr );
    }
    else if( _expr instanceof AdditiveExpression)
    {
      return AdditiveExpressionTransformer.compile( _cc, (AdditiveExpression)_expr );
    }
    else if( _expr instanceof MultiplicativeExpression)
    {
      return MultiplicativeExpressionTransformer.compile( _cc, (MultiplicativeExpression)_expr );
    }
    else if( _expr instanceof BitshiftExpression)
    {
      return BitshiftExpressionTransformer.compile( _cc, (BitshiftExpression)_expr );
    }
    else if( _expr instanceof BitwiseOrExpression)
    {
      return BitwiseOrExpressionTransformer.compile( _cc, (BitwiseOrExpression)_expr );
    }
    else if( _expr instanceof BitwiseXorExpression)
    {
      return BitwiseXorExpressionTransformer.compile( _cc, (BitwiseXorExpression)_expr );
    }
    else if( _expr instanceof BitwiseAndExpression)
    {
      return BitwiseAndExpressionTransformer.compile( _cc, (BitwiseAndExpression)_expr );
    }
    else if( _expr instanceof BeanMethodCallExpression )
    {
      return BeanMethodCallExpressionTransformer.compile( _cc, (BeanMethodCallExpression)_expr );
    }
    else if( _expr instanceof MethodCallExpression )
    {
      return MethodCallExpressionTransformer.compile( _cc, (MethodCallExpression)_expr );
    }
    else if( _expr instanceof MemberExpansionAccess)
    {
      return MemberExpansionAccessTransformer.compile( _cc, (MemberExpansionAccess)_expr );
    }
    else if( _expr instanceof MemberAccess)
    {
      return MemberAccessTransformer.compile( _cc, (MemberAccess)_expr );
    }
    else if( _expr instanceof NewExpression )
    {
      return NewExpressionTransformer.compile( _cc, (NewExpression)_expr );
    }
    else if( _expr instanceof EvalExpression)
    {
      return EvalExpressionTransformer.compile( _cc, (EvalExpression)_expr );
    }
    else if( _expr instanceof QueryExpression )
    {
      return QueryExpressionTransformer.compile( _cc, (QueryExpression)_expr );
    }
    else if( _expr instanceof ConditionalTernaryExpression)
    {
      return ConditionalTernaryExpressionTransformer.compile( _cc, (ConditionalTernaryExpression)_expr );
    }
    else if( _expr instanceof ArrayAccess)
    {
      return ArrayAccessTransformer.compile( _cc, (ArrayAccess)_expr );
    }
    else if( _expr instanceof MapAccess)
    {
      return MapAccessTransformer.compile( _cc, (MapAccess)_expr );
    }
    else if( _expr instanceof IntervalExpression )
    {
      return IntervalExpressionTransformer.compile( _cc, (IntervalExpression)_expr );
    }
    else if( _expr instanceof ParenthesizedExpression)
    {
      return compile( ((ParenthesizedExpression)_expr).getExpression(), _cc );
    }
//    else if( _expr instanceof VirtualExpression )
//    {
//      VirtualExpressionCompiler.compile( _cc, (VirtualExpression)_expr );
//    }
    else if( _expr instanceof NullExpression )
    {
      return NullExpressionTransformer.compile( _cc, (NullExpression)_expr );
    }
    else if( _expr instanceof BlockExpression)
    {
      return BlockExpressionTransformer.compile( _cc, (BlockExpression)_expr );
    }
    else if( _expr instanceof ObjectLiteralExpression)
    {
      return ObjectLiteralExpressionTransformer.compile( _cc, (ObjectLiteralExpression)_expr );
    }
    else if( _expr instanceof TemplateStringLiteral )
    {
      return TemplateStringLiteralTransformer.compile( _cc, (TemplateStringLiteral)_expr );
    }
    else if( _expr instanceof ExistsExpression )
    {
      return ExistsExpressionTransformer.compile( _cc, (ExistsExpression)_expr );
    }
    else if( _expr instanceof BlockInvocation )
    {
      return BlockInvocationTransformer.compile( _cc, (BlockInvocation)_expr );
    }
    else if( _expr instanceof FeatureLiteral )
    {
      return FeatureLiteralTransformer.compile( _cc, (FeatureLiteral)_expr );
    }
    else
    {
      if( _expr == null )
      {
        throw new IllegalStateException( "Found null parsed element, which is illegal" );        
      }
      throw new UnsupportedOperationException( "Expression Transformer not yet implemented for: " + _expr.getClass().getName() );
    }
  }

  private List<IRStatement> compileInitializer( IRExpression root )
  {
    if( _expr instanceof CollectionInitializerExpression)
    {
      return CollectionInitializerExpressionTransformer.compile( _cc, (CollectionInitializerExpression)_expr, root );
    }
    else if( _expr instanceof MapInitializerExpression)
    {
      return MapInitializerExpressionTransformer.compile( _cc, (MapInitializerExpression)_expr, root );
    }
    else if( _expr instanceof ObjectInitializerExpression)
    {
      return ObjectInitializerExpressionTransformer.compile( _cc, (ObjectInitializerExpression)_expr, root );
    }
    else
    {
      throw new UnsupportedOperationException( "Expression Transformer not yet implemented for: " + _expr.getClass().getName() );
    }
  }
}
