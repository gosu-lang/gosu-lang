/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

@UnstableAPI
public class IRStatementList extends IRStatement {
  private List<IRStatement> _statements;
  private boolean _hasScope = true;

  public IRStatementList(boolean hasScope, IRStatement... statements) {
    _hasScope = hasScope;
    _statements = new ArrayList<IRStatement>();
    _statements.addAll(Arrays.asList(statements));

    for (IRStatement statement : statements) {
      statement.setParent( this );
    }
  }

  public IRStatementList(boolean hasScope, List<IRStatement> statements) {
    _hasScope = hasScope;
    _statements = statements;
    for (IRStatement statement : statements) {
      if (statement != null) {
        statement.setParent( this );
      }
    }
  }

  public void addStatement(IRStatement statement) {
    _statements.add(statement);
    statement.setParent( this );
  }

  public List<IRStatement> getStatements() {
    return _statements;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    for( int i = 0; i < _statements.size(); i++ )
    {
      if (_statements.get(i) != null) {
        IRTerminalStatement terminalStmt = _statements.get(i).getLeastSignificantTerminalStatement();
        if( terminalStmt != null )
        {
          return terminalStmt;
        }
      }
    }
    return null;
  }
  
  public void mergeStatements( IRStatement irStatement )
  {
    if( irStatement instanceof IRStatementList )
    {
      for( IRStatement statement : ((IRStatementList)irStatement).getStatements() )
      {
        addStatement( statement );
      }
    }
    else
    {
      addStatement( irStatement );
    }
  }

  public boolean hasScope()
  {
    return _hasScope;
  }
}
