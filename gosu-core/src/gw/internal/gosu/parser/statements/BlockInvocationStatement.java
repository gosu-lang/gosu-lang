/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.statements.IBlockInvocationStatement;
import gw.lang.parser.expressions.IBlockInvocation;
import gw.lang.parser.statements.ITerminalStatement;

public class BlockInvocationStatement extends Statement implements IBlockInvocationStatement
{
  IBlockInvocation _invocation;

  public BlockInvocationStatement( IBlockInvocation invocation )
  {
    _invocation = invocation;
  }

  @Override
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  public IBlockInvocation getBlockInvocation()
  {
    return _invocation;
  }

  @Override
  public String toString()
  {
    return _invocation.toString();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

}
