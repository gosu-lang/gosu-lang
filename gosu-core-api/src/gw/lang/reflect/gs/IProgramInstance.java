/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

public interface IProgramInstance
{
  public Object evaluate(IExternalSymbolMap symbols);

  public Object evaluateRootExpr(IExternalSymbolMap symbols);
}
