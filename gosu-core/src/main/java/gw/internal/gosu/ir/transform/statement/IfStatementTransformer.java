/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.IfStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;

/**
 */
public class IfStatementTransformer extends AbstractStatementTransformer<IfStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, IfStatement stmt )
  {
    IfStatementTransformer compiler = new IfStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private IfStatementTransformer( TopLevelTransformationContext cc, IfStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRExpression test = ExpressionTransformer.compile(_stmt().getExpression(), _cc());

    IRStatement ifBlockStatement = null;
    _cc().pushScope( false );
    try {
      ifBlockStatement = _cc().compile(_stmt().getStatement());
    } finally {
      _cc().popScope();
    }

    IRStatement elseBlockStatement = null;
    _cc().pushScope( false );
    try {
      elseBlockStatement = _stmt().hasElseStatement() ? _cc().compile(_stmt().getElseStatement()) : null;
    } finally {
      _cc().popScope();
    }
    return buildIfElse(
            test,
            ifBlockStatement,
            elseBlockStatement
    );
  }
}