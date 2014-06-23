/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.TerminalType;


/**
 * Represents a continue statement as specified in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ContinueStatement extends TerminalStatement implements IContinueStatement
{
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new IllegalStateException( "Can't execute this parsed element directly" );
  }

  @Override
  public TerminalType getTerminalType() {
    return TerminalType.Continue;
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
    return "continue";
  }

}
