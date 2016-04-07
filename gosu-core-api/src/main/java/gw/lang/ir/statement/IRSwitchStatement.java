/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class IRSwitchStatement extends IRStatement {

  private final boolean _areLabelsConstant;
  private IRStatement _init;
  private List<IRCaseClause> _cases;
  private List<IRStatement> _defaultStatements;

  public IRSwitchStatement(IRStatement init, List<IRCaseClause> cases, List<IRStatement> defaultStatements, boolean areLabelsConstant) {
    _init = init;
    _cases = cases;
    _defaultStatements = defaultStatements;
    _areLabelsConstant = areLabelsConstant;

    setParentToThis( init );
    for (IRCaseClause caseClause : cases) {
      setParentToThis( caseClause.getCondition() );
      for ( IRStatement caseStatement : caseClause.getStatements() ) {
        setParentToThis( caseStatement );
      }
    }
    for (IRStatement defaultStatement : defaultStatements) {
      setParentToThis( defaultStatement );
    }
  }

  public IRStatement getInit() {
    return _init;
  }

  public boolean areLabelsConstant() {
    return _areLabelsConstant;
  }

  public List<IRCaseClause> getCases() {
    return _cases;
  }

  public List<IRStatement> getDefaultStatements() {
    return _defaultStatements;
  }

  /**
   * The switch statement has a non-null terminal stmt iff:
   * 1) There are no case stmts or all the the case stmts have non-break terminator and
   * 2) The default stmt exists and has a non-break terminator
   */
  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    if( _defaultStatements.isEmpty())
    {
      return null;
    }
    IRContinueStatement caseStmtContinue = null;
    if( _cases != null )
    {
      outer:
      for( int i = 0; i < _cases.size(); i++ )
      {
        List caseStatements = _cases.get(i).getStatements();
        if( caseStatements != null && caseStatements.size() > 0 )
        {
          for( int iStmt = 0; iStmt < caseStatements.size(); iStmt++ )
          {
            IRStatement statement = (IRStatement) caseStatements.get(iStmt);
            // Note that the statement can be null if it's just a semicolon
            IRTerminalStatement terminalStmt = (statement == null ? null : statement.getLeastSignificantTerminalStatement());
            if( terminalStmt != null && !(terminalStmt instanceof IRBreakStatement) )
            {
              if( terminalStmt instanceof IRContinueStatement )
              {
                caseStmtContinue = (IRContinueStatement)terminalStmt;
              }
              continue outer;
            }
          }
          return null;
        }
      }
    }
    for( int i = 0; i < _defaultStatements.size(); i++ )
    {
      IRTerminalStatement terminalStmt = _defaultStatements.get( i ).getLeastSignificantTerminalStatement();
      if( terminalStmt != null && !(terminalStmt instanceof IRBreakStatement) )
      {
        return caseStmtContinue != null ? caseStmtContinue : terminalStmt;
      }
    }
    return null;
  }
}
