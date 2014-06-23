/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.TerminalType;
import gw.util.GosuExceptionUtil;

/**
 * Represents a return statement as specified in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ReturnStatement extends TerminalStatement implements IReturnStatement
{
  private Expression _value;


  public boolean isCompileTimeConstant()
  {
    return getValue() == null || getValue().isCompileTimeConstant();
  }

  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    // Return statement can be executed directly if the return value can be
    // evaluated directly e.g., if it's a literal
    try
    {
      return _value.evaluate();
    }
    catch( Throwable t )
    {
      throw GosuExceptionUtil.forceThrow( t );
    }
  }

  public Expression getValue()
  {
    return _value;
  }

  public void setValue( Expression value )
  {
    _value = value;
  }

  @Override
  public TerminalType getTerminalType() {
    return TerminalType.ReturnOrThrow;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = true;
    return this;
  }

  @Override
  public String toString()
  {
    Expression value = getValue();
    return "return" + (value == null ? "" : (" " + value.toString()));
  }

}
