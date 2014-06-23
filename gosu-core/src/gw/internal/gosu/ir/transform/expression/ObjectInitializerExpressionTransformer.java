/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.expressions.ObjectInitializerExpression;
import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 */
public class ObjectInitializerExpressionTransformer extends AbstractElementTransformer<ObjectInitializerExpression>
{
  public static List<IRStatement> compile( TopLevelTransformationContext cc, ObjectInitializerExpression expr, IRExpression root )
  {
    ObjectInitializerExpressionTransformer compiler = new ObjectInitializerExpressionTransformer( cc, expr );
    return compiler.compile( root );
  }

  private ObjectInitializerExpressionTransformer( TopLevelTransformationContext cc, ObjectInitializerExpression expr )
  {
    super( cc, expr );
  }

  private List<IRStatement> compile( IRExpression root )
  {
    List<InitializerAssignment> initializers = getParsedElement().getInitializers();
    if( initializers == null )
    {
      return Collections.emptyList();
    }

    List<IRStatement> statements = new ArrayList<IRStatement>();
    for( InitializerAssignment initialier : initializers )
    {
      statements.add( _cc().compileInitializerAssignment( initialier, root ) );
    }
    return statements;
  }
}
