/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRTypeFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.FeatureLiteral;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.features.BoundComplexPropertyChainReference;
import gw.lang.reflect.features.BoundMethodReference;
import gw.lang.reflect.features.BoundPropertyReference;
import gw.lang.reflect.features.BoundSimplePropertyChainReference;
import gw.lang.reflect.features.ComplexPropertyChainReference;
import gw.lang.reflect.features.ConstructorReference;
import gw.lang.reflect.features.FeatureReference;
import gw.lang.reflect.features.MethodChainReference;
import gw.lang.reflect.features.MethodReference;
import gw.lang.reflect.features.PropertyReference;
import gw.lang.reflect.features.SimplePropertyChainReference;

import java.util.ArrayList;
import java.util.List;

public class FeatureLiteralTransformer extends AbstractExpressionTransformer<FeatureLiteral>
{
  public static IRExpression compile( TopLevelTransformationContext cc, FeatureLiteral expr )
  {
    FeatureLiteralTransformer compiler = new FeatureLiteralTransformer( cc, expr );
    return compiler.compile();
  }

  private FeatureLiteralTransformer( TopLevelTransformationContext cc, FeatureLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    if( _expr().isPropertyLiteral() )
    {
      IExpression root = _expr().getRoot();
      if( root instanceof TypeLiteral )
      {
        return buildNewExpression( PropertyReference.class, new Class[]{IType.class, String.class},
                                    exprList( pushType( _expr().getRootType() ),
                                              stringLiteral( _expr().getPropertyName() ) ) );
      }
      else if( root instanceof FeatureLiteral )
      {
        IRNewExpression expression = (IRNewExpression)ExpressionTransformer.compile( root, _cc() );
        if( JavaClassIRType.get( SimplePropertyChainReference.class ).isAssignableFrom( IRTypeFactory.get( _expr().getType() ) ) )
        {
          return buildNewExpression( SimplePropertyChainReference.class, new Class[]{IType.class, FeatureReference.class, String.class},
                                     exprList( pushType( _expr().getRootType() ),
                                               expression,
                                               stringLiteral( _expr().getPropertyName() ) ) );
        }
        else if( JavaClassIRType.get( BoundSimplePropertyChainReference.class ).isAssignableFrom( IRTypeFactory.get( _expr().getType() ) ) )
        {
          return buildNewExpression( BoundSimplePropertyChainReference.class, new Class[]{IType.class, FeatureReference.class, String.class},
                                     exprList( pushType( _expr().getRootType() ),
                                               expression,
                                               stringLiteral( _expr().getPropertyName() ) ) );
        }
        else if( JavaClassIRType.get( BoundComplexPropertyChainReference.class ).isAssignableFrom( IRTypeFactory.get( _expr().getType() ) ) )
        {
          return buildNewExpression( BoundComplexPropertyChainReference.class, new Class[]{IType.class, FeatureReference.class, String.class},
                                     exprList( pushType( _expr().getRootType() ),
                                               expression,
                                               stringLiteral( _expr().getPropertyName() ) ) );
        }
        else
        {
          return buildNewExpression( ComplexPropertyChainReference.class, new Class[]{IType.class, FeatureReference.class, String.class},
                                     exprList( pushType( _expr().getRootType() ),
                                               expression,
                                               stringLiteral( _expr().getPropertyName() ) ) );
        }
      }
      else
      {
        return buildNewExpression( BoundPropertyReference.class, new Class[]{IType.class, Object.class, String.class},
                                   exprList( pushType( _expr().getRootType() ),
                                             ExpressionTransformer.compile( root, _cc() ),
                                             stringLiteral( _expr().getPropertyName() ) ) );
      }
    }
    else if( _expr().isMethodLiteral() )
    {
      IExpression root = _expr().getRoot();
      if( root instanceof TypeLiteral )
      {
        return buildNewExpression( MethodReference.class, new Class[]{IType.class, String.class, IType[].class, Object[].class},
                                   exprList( pushType( _expr().getRootType() ),
                                             stringLiteral( _expr().getMethodName() ),
                                             pushArrayOfTypes( _expr().getParameterTypes() ),
                                             getBoundValues()) );
      }
      else if( root instanceof FeatureLiteral )
      {
        IRNewExpression expression = (IRNewExpression) ExpressionTransformer.compile(root, _cc());
        return buildNewExpression( MethodChainReference.class, new Class[]{IType.class, FeatureReference.class, String.class, IType[].class, Object[].class},
                                   exprList( pushType( _expr().getRootType() ),
                                             expression,
                                             stringLiteral( _expr().getMethodName() ),
                                             pushArrayOfTypes( _expr().getParameterTypes() ),
                                             getBoundValues() ) );
      }
      else
      {
        return buildNewExpression( BoundMethodReference.class, new Class[]{IType.class, Object.class, String.class, IType[].class, Object[].class},
                                   exprList( pushType( _expr().getRootType() ),
                                             ExpressionTransformer.compile( root, _cc() ),
                                             stringLiteral( _expr().getMethodName() ),
                                             pushArrayOfTypes( _expr().getParameterTypes() ),
                                             getBoundValues() ) );
      }
    }
    else if( _expr().isConstructorLiteral() )
    {
      return buildNewExpression( ConstructorReference.class, new Class[]{IType.class, IType[].class, Object[].class},
                                 exprList( pushType( _expr().getRootType() ),
                                           pushArrayOfTypes( _expr().getParameterTypes() ),
                                           getBoundValues() ) );
    }
    else
    {
      throw new IllegalStateException( "Unable to convert reference of type " + _expr().toString() );
    }
  }

  private IRExpression getBoundValues()
  {
    List<IExpression> args = _expr().getBoundArgs();
    IRExpression boundArgs;
    if( args == null )
    {
      boundArgs = pushNull();
    }
    else
    {
      ArrayList<IRExpression> values = new ArrayList<IRExpression>();
      for( IExpression arg : args )
      {
        values.add( ExpressionTransformer.compile( arg, _cc() ) );
      }
      boundArgs = collectArgsIntoObjArray( values );
    }
    return boundArgs;
  }
}
