/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.MapInitializerExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.AbstractMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 */
public class MapInitializerExpressionTransformer extends AbstractElementTransformer<MapInitializerExpression>
{
  public static List<IRStatement> compile( TopLevelTransformationContext cc, MapInitializerExpression expr, IRExpression root )
  {
    MapInitializerExpressionTransformer gen = new MapInitializerExpressionTransformer( cc, expr );
    return gen.compile( root );
  }

  private MapInitializerExpressionTransformer( TopLevelTransformationContext cc, MapInitializerExpression expr )
  {
    super( cc, expr );
  }

  private List<IRStatement> compile( IRExpression root )
  {
    Class mapType = getMapType();
    List<IExpression> keys = getParsedElement().getKeys();
    List<IExpression> values = getParsedElement().getValues();
    List<IRStatement> statements = new ArrayList<IRStatement>();
    for( int i = 0; i < keys.size(); i++ )
    {
      IExpression keyExpr = keys.get( i );
      IRExpression irKeyExpr = ExpressionTransformer.compile( keyExpr, _cc() );
      IExpression valueExpr = values.get( i );
      IRExpression irValueExpr = ExpressionTransformer.compile( valueExpr, _cc() );
      IRExpression putCall = callMethod( mapType, "put", new Class[]{Object.class, Object.class}, root, exprList( irKeyExpr, irValueExpr ) );
      statements.add( buildMethodCall( putCall ) );
    }
    return statements;
  }

  /**
   * Try to get a more specific class instead of using the Map interface
   * i.e., invokevirtual is significantly faster then invokeinterface.
   */
  private Class getMapType()
  {
    Class mapType = Map.class;
    IParsedElement parent = getParsedElement().getParent();
    if( parent instanceof NewExpression )
    {
      IType newType = ((NewExpression)parent).getType();
      IJavaClassInfo classInfo = IRTypeResolver.getJavaBackedClass(newType);
      Class javaBackedClass = classInfo != null ? classInfo.getBackingClass() : null;
      if( classInfo != null && javaBackedClass != null )
      {
        mapType = javaBackedClass;
      }
      else if( JavaTypes.getJreType(AbstractMap.class).isAssignableFrom( newType ) )
      {
        mapType = AbstractMap.class;
      }
    }
    return mapType;
  }
}
