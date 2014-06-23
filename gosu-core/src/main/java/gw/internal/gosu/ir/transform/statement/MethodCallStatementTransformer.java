/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;

/**
 */
public class MethodCallStatementTransformer extends AbstractStatementTransformer<MethodCallStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, MethodCallStatement stmt )
  {
    MethodCallStatementTransformer compiler = new MethodCallStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private MethodCallStatementTransformer( TopLevelTransformationContext cc, MethodCallStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRExpression methodCall = ExpressionTransformer.compile( _stmt().getMethodCall(), _cc() );
    return buildMethodCall( methodCall );
  }
}