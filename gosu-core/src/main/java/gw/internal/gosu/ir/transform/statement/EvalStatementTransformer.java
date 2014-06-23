/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.EvalStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IREvalStatement;

/**
 */
public class EvalStatementTransformer extends AbstractStatementTransformer<EvalStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, EvalStatement stmt )
  {
    EvalStatementTransformer compiler = new EvalStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private EvalStatementTransformer( TopLevelTransformationContext cc, EvalStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRExpression evalExpr = ExpressionTransformer.compile( _stmt().getEvalExpression(), _cc() );
    return new IREvalStatement( evalExpr );
  }
}