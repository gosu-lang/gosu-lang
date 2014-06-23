/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRIfStatement extends IRStatement {
  private IRExpression _expression;
  private IRStatement _ifStatement;
  private IRStatement _elseStatement;

  public IRIfStatement(IRExpression expression, IRStatement ifStatement, IRStatement elseStatement) {
    // It's possible for the compiler to pass us a null if statement if the code looks like if(foo) { ; }
    // So we guard against that and treat it like a no-op statement to avoid having to null-check everywhere
    // else
    if (ifStatement == null) {
      ifStatement = new IRNoOpStatement();
    }
    _expression = expression;
    _ifStatement = ifStatement;
    _elseStatement = elseStatement;

    expression.setParent( this );
    ifStatement.setParent( this );
    if (elseStatement != null) {
      elseStatement.setParent( this );
    }
  }

  public IRExpression getExpression() {
    return _expression;
  }

  public IRStatement getIfStatement() {
    return _ifStatement;
  }

  public IRStatement getElseStatement() {
    return _elseStatement;
  }

  public void setElseStatement(IRStatement elseStatement) {
    _elseStatement = elseStatement;
    setParentToThis( elseStatement );
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    IRTerminalStatement ifStmtTerminal = _ifStatement.getLeastSignificantTerminalStatement();
    if( ifStmtTerminal != null && _elseStatement != null )
    {
      IRTerminalStatement elseStmtTerminal = _elseStatement.getLeastSignificantTerminalStatement();
      if( elseStmtTerminal != null )
      {
        if( ifStmtTerminal instanceof IRBreakStatement ||
            ifStmtTerminal instanceof IRContinueStatement )
        {
          return ifStmtTerminal;
        }
        if( elseStmtTerminal instanceof IRBreakStatement ||
            elseStmtTerminal instanceof IRContinueStatement )
        {
          return elseStmtTerminal;
        }
        // Return either one, doesn't matter because they are both return or throw.
        return ifStmtTerminal;
      }
    }
    return null;
  }
}
