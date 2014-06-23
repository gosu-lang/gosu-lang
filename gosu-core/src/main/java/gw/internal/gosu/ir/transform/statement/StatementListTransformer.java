/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.parser.IStatement;

import java.util.List;
import java.util.ArrayList;

/**
 */
public class StatementListTransformer extends AbstractStatementTransformer<StatementList>
{
  public static IRStatement compile( TopLevelTransformationContext cc, StatementList stmt )
  {
    StatementListTransformer gen = new StatementListTransformer( cc, stmt );
    return gen.compile();
  }

  private StatementListTransformer( TopLevelTransformationContext cc, StatementList stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected void visitStatementLineNumber(IRStatement irStatement) {
    // Do nothing
  }

  @Override
  protected IRStatement compile_impl()
  {
    List<IRStatement> irStatements = new ArrayList<IRStatement>();
    _cc().pushScope( false );
    try
    {
      IStatement[] statements = _stmt().getStatements();
      if( statements != null )
      {
        for( IStatement s : statements )
        {
          IRStatement irStatement = _cc().compile( s );
          if (irStatement != null) {
            irStatements.add(irStatement);
          }
        }
      }
    }
    finally
    {
      _cc().popScope();
    }

    return new IRStatementList( true, irStatements );
  }
}