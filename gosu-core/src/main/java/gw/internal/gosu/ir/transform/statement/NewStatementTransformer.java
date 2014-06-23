/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.NewStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.expression.IRNewExpression;

/**
 */
public class NewStatementTransformer extends AbstractStatementTransformer<NewStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, NewStatement stmt )
  {
    NewStatementTransformer compiler = new NewStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private NewStatementTransformer( TopLevelTransformationContext cc, NewStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRNewExpression newExpr = (IRNewExpression)ExpressionTransformer.compile( _stmt().getNewExpression(), _cc() );
    return buildNewExpression( newExpr );
  }
}