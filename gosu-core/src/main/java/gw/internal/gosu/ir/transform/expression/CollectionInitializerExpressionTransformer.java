/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.expressions.CollectionInitializerExpression;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collection;
import java.util.AbstractCollection;
import java.util.List;
import java.util.ArrayList;

/**
 */
public class CollectionInitializerExpressionTransformer extends AbstractElementTransformer<CollectionInitializerExpression>
{
  public static List<IRStatement> compile( TopLevelTransformationContext cc, CollectionInitializerExpression expr, IRExpression root )
  {
    CollectionInitializerExpressionTransformer gen = new CollectionInitializerExpressionTransformer( cc, expr );
    return gen.compile( root );
  }

  private CollectionInitializerExpressionTransformer( TopLevelTransformationContext cc, CollectionInitializerExpression expr )
  {
    super( cc, expr );
  }

  private List<IRStatement> compile( IRExpression root )
  {
    List<IRStatement> statements = new ArrayList<IRStatement>();

    Class collectionType = getCollectionType();
    for( IExpression e : getParsedElement().getValues() )
    {
      IRExpression value = ExpressionTransformer.compile( e, _cc() );
      statements.add( buildMethodCall( callMethod( collectionType, "add", new Class[]{Object.class}, root, exprList( value ) ) ));
    }
    return statements;
  }

  /**
   * Try to get a more specific class instead of using the Collection interface
   * i.e., invokevirtual is significantly faster then invokeinterface.
   */
  private Class getCollectionType()
  {
    Class collectionType = Collection.class;
    IParsedElement parent = getParsedElement().getParent();
    if( parent instanceof NewExpression )
    {
      IType newType = ((NewExpression)parent).getType();
      IJavaClassInfo classInfo = IRTypeResolver.getJavaBackedClass(newType);
      Class javaBackedClass = classInfo != null ? classInfo.getBackingClass() : null;
      if( classInfo != null && javaBackedClass != null )
      {
        collectionType = javaBackedClass;
      }
      else if( JavaTypes.getJreType(AbstractCollection.class).isAssignableFrom( newType ) )
      {
        collectionType = AbstractCollection.class;
      }
    }
    return collectionType;
  }
}
