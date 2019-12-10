/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.util.StringPool;

/**
 */
public class NamespaceStatement extends Statement implements INamespaceStatement
{
  private String _strNamespace;

  public NamespaceStatement()
  {
  }

  public String getNamespace()
  {
    return _strNamespace;
  }

  public void setNamespace( String strNamespace )
  {
    _strNamespace = strNamespace == null ? null : StringPool.get( strNamespace );
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
    return "package " + getNamespace();
  }
}
