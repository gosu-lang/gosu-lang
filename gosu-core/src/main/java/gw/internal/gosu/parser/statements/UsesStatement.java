/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.module.IModule;

/**
 */
public class UsesStatement extends Statement implements IUsesStatement
{
  private String _strTypeName;

  public UsesStatement()
  {
  }

  public UsesStatement(String typeName)
  {
    _strTypeName = typeName;
  }

  public String getTypeName()
  {
    return _strTypeName;
  }

  public void setTypeName( String strTypeName )
  {
    _strTypeName = strTypeName.intern();
  }

  public Object execute()
  {
    // no-op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return Keyword.KW_uses + " " + getTypeName();
  }

  public IModule getModule() {
    return getGosuClass().getClassStatement().getModule();
  }
}
