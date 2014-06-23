/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.BlockInvocationStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;

/**
 */
public class BlockInvocationStatementTransformer extends AbstractStatementTransformer<BlockInvocationStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, BlockInvocationStatement stmt )
  {
    BlockInvocationStatementTransformer compiler = new BlockInvocationStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private BlockInvocationStatementTransformer( TopLevelTransformationContext cc, BlockInvocationStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    return buildMethodCall( ExpressionTransformer.compile( _stmt().getBlockInvocation(), _cc() ) );
  }
}