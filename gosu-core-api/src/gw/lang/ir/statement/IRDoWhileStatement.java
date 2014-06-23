/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRAbstractLoopStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRDoWhileStatement extends IRAbstractLoopStatement
{

  // test
  private IRExpression _test;

  // body
  private IRStatement _body;

  public IRExpression getLoopTest()
  {
    return _test;
  }

  public void setLoopTest( IRExpression test )
  {
    _test = test;
    setParentToThis( test );
  }

  public IRStatement getBody()
  {
    return _body;
  }

  public void setBody( IRStatement irStatement )
  {
    _body = irStatement;
    setParentToThis( irStatement );
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    // do/while loops always execute at least once, so we can look for a terminal statement
    // within the body of the loop
    if (_body != null)
    {
      IRTerminalStatement terminalStmt = _body.getLeastSignificantTerminalStatement();
      if (terminalStmt instanceof IRReturnStatement ||
              terminalStmt instanceof IRThrowStatement)
      {
        return terminalStmt;
      }
    }
    return null;
  }
}