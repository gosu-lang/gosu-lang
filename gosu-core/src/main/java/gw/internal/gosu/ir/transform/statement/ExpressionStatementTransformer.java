/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.ExpressionStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRMethodCallStatement;

/**
 */
public class ExpressionStatementTransformer extends AbstractStatementTransformer<ExpressionStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ExpressionStatement stmt )
  {
    ExpressionStatementTransformer compiler = new ExpressionStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private ExpressionStatementTransformer( TopLevelTransformationContext cc, ExpressionStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRExpression expression = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    return new IRMethodCallStatement( expression );
  }
}
