/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;

/**
 */
public class BeanMethodCallStatementTransformer extends AbstractStatementTransformer<BeanMethodCallStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, BeanMethodCallStatement stmt )
  {
    BeanMethodCallStatementTransformer compiler = new BeanMethodCallStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private BeanMethodCallStatementTransformer( TopLevelTransformationContext cc, BeanMethodCallStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRExpression methodCall = ExpressionTransformer.compile( _stmt().getBeanMethodCall(), _cc() );
    return buildMethodCall( methodCall );
  }
}