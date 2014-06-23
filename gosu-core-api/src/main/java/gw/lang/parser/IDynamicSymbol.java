/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IGosuClass;

public interface IDynamicSymbol extends IFunctionSymbol
{
  IScriptPartId getScriptPart();
  IGosuClass getGosuClass();
}
