/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRAbstractLoopStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public class IRForEachStatement extends IRAbstractLoopStatement
{

  // init
  private List<IRStatement> _initializers = new ArrayList<IRStatement>();

  // test
  private IRExpression _test;

  // increment
  private List<IRStatement> _incrementors = new ArrayList<IRStatement>();

  // body
  private IRStatement _body;

  // identifier, if any, to null check
  private IRIdentifier _identifierToNullCheck;

  public List<IRStatement> getInitializers()
  {
    return _initializers;
  }

  public void addInitializer( IRStatement initializer )
  {
    _initializers.add( initializer );
    setParentToThis( initializer );
  }

  public List<IRStatement> getIncrementors()
  {
    return _incrementors;
  }

  public void addIncrementor( IRStatement incrementor )
  {
    _incrementors.add( incrementor );
    setParentToThis( incrementor );
  }

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

  public boolean hasIdentifierToNullCheck()
  {
    return _identifierToNullCheck != null;
  }

  public IRIdentifier getIdentifierToNullCheck()
  {
    return _identifierToNullCheck;
  }

  public void setIdentifierToNullCheck( IRIdentifier exprToNullCheck )
  {
    _identifierToNullCheck = exprToNullCheck;
    setParentToThis( exprToNullCheck );
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }
}