/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IMethodInfo;

/**
 */
public class ReducedDelegateFunctionSymbol extends ReducedDynamicFunctionSymbol implements IReducedDelegateFunctionSymbol {
  private IMethodInfo _targetMethodInfo;

  ReducedDelegateFunctionSymbol(DelegateFunctionSymbol dfs) {
    super( dfs );
    _targetMethodInfo = dfs.getMi();
  }

  @Override
  public IMethodInfo getTargetMethodInfo() {
    return _targetMethodInfo;
  }
}
