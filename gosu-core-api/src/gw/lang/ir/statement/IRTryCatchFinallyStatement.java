/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class IRTryCatchFinallyStatement extends IRStatement {
  private IRStatement _tryBody;
  private List<IRCatchClause> _catchStatements;
  private IRStatement _finallyBody;

  public IRTryCatchFinallyStatement(IRStatement tryBody, List<IRCatchClause> catchStatements, IRStatement finallyBody) {
    _tryBody = tryBody;
    _catchStatements = catchStatements;
    _finallyBody = finallyBody;

    tryBody.setParent( this );
    for (IRCatchClause catchClause : catchStatements) {
      catchClause.getBody().setParent( this );
    }
    if (finallyBody != null) {
      finallyBody.setParent( this );
    }
  }

  public IRStatement getTryBody() {
    return _tryBody;
  }

  public List<IRCatchClause> getCatchStatements() {
    return _catchStatements;
  }

  public IRStatement getFinallyBody() {
    return _finallyBody;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {

    IRTerminalStatement tryStmtTerminal = _tryBody.getLeastSignificantTerminalStatement();
    if( tryStmtTerminal != null )
    {
      if( _catchStatements.isEmpty())
      {
        return tryStmtTerminal;
      }
      IRTerminalStatement catchStmtTerminal = _catchStatements.get( _catchStatements.size() - 1 ).getBody().getLeastSignificantTerminalStatement();
      if( catchStmtTerminal != null )
      {
        return catchStmtTerminal;
      }
    }
    return null;
  }
}
