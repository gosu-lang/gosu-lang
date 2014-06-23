/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.BreakStatement;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRBreakStatement;

/**
 */
public class BreakStatementTransformer extends AbstractStatementTransformer<BreakStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, BreakStatement stmt )
  {
    BreakStatementTransformer gen = new BreakStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private BreakStatementTransformer( TopLevelTransformationContext cc, BreakStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    return new IRBreakStatement();
  }
}