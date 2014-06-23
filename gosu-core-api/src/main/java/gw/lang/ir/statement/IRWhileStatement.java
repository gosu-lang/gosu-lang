/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRAbstractLoopStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRWhileStatement extends IRAbstractLoopStatement
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

  public boolean isLoopCondLiteralTrue()
  {
    return _test instanceof IRBooleanLiteral && ((IRBooleanLiteral) _test).getValue();
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    // Unless _test is the literal value "true" there's a chance the loop won't
    // execute even once
    if( !isLoopCondLiteralTrue() )
    {
      return null;
    }

    if( _body != null )
    {
      IRTerminalStatement terminalStmt = _body.getLeastSignificantTerminalStatement();
      if( terminalStmt instanceof IRReturnStatement ||
          terminalStmt instanceof IRThrowStatement )
      {
        return terminalStmt;
      }
    }
    return null;
  }
}