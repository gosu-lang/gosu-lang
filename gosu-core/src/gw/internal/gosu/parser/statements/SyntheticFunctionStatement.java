/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.lang.parser.statements.ISyntheticFunctionStatement;

/**
 */
public class SyntheticFunctionStatement extends NoOpStatement implements ISyntheticFunctionStatement
{
  private DynamicFunctionSymbol _dfsOwner;

  public SyntheticFunctionStatement()
  {
  }

  public DynamicFunctionSymbol getDfsOwner()
  {
    return _dfsOwner;
  }

  public void setDfsOwner( DynamicFunctionSymbol dfsOwner )
  {
    _dfsOwner = dfsOwner;
  }

}
