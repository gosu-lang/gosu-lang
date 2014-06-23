/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.ContinueStatement;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRContinueStatement;

/**
 */
public class ContinueStatementTransformer extends AbstractStatementTransformer<ContinueStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ContinueStatement stmt )
  {
    ContinueStatementTransformer gen = new ContinueStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private ContinueStatementTransformer( TopLevelTransformationContext cc, ContinueStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    return new IRContinueStatement();
  }
}