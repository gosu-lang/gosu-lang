/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.SyntheticMemberAccessStatement;
import gw.internal.gosu.ir.transform.statement.AbstractStatementTransformer;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRSyntheticStatement;

/**
 */
public class SyntheticMemberAccessStatementTransformer extends AbstractStatementTransformer<SyntheticMemberAccessStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, SyntheticMemberAccessStatement stmt )
  {
    SyntheticMemberAccessStatementTransformer gen = new SyntheticMemberAccessStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private SyntheticMemberAccessStatementTransformer( TopLevelTransformationContext cc, SyntheticMemberAccessStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    return new IRSyntheticStatement( ExpressionTransformer.compile( _stmt().getMemberAccessExpression(), _cc() ) );
  }
}