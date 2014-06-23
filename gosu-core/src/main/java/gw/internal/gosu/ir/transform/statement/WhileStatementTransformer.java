/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRWhileStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.WhileStatement;
import gw.lang.parser.statements.ITerminalStatement;

public class WhileStatementTransformer extends AbstractStatementTransformer<WhileStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, WhileStatement stmt )
  {
    WhileStatementTransformer compiler = new WhileStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private WhileStatementTransformer( TopLevelTransformationContext cc, WhileStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Push a scope in case while body not a statement list
    _cc().pushScope( false );
    IRWhileStatement whileLoop = new IRWhileStatement();
    try
    {

      // loop test
      whileLoop.setLoopTest( ExpressionTransformer.compile( _stmt().getExpression(), _cc() ) );

      // execute body

      boolean[] bAbsolute = {false};
      ITerminalStatement terminalStmt = _stmt().getLeastSignificantTerminalStatement(bAbsolute);
      whileLoop.setBody( _cc().compile(_stmt().getStatement() ) );
      if( terminalStmt == _stmt() && bAbsolute[0] )
      {
        _cc().getCurrentFunction().setLoopImplicitReturn( true );
      }

      return whileLoop;
    }
    finally
    {
      _cc().popScope();
    }
  }
}