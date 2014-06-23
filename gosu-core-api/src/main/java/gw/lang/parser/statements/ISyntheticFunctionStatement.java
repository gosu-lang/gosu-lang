/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IDynamicFunctionSymbol;

public interface ISyntheticFunctionStatement extends INoOpStatement
{
  IDynamicFunctionSymbol getDfsOwner();
}
