/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.FunctionType;

/**
 */
public class ReducedParameterizedDynamicFunctionSymbol extends ReducedDynamicFunctionSymbol {

  private ReducedDynamicFunctionSymbol _delegate;

  ReducedParameterizedDynamicFunctionSymbol( ReducedDynamicFunctionSymbol rdfsDelegate, ParameterizedDynamicFunctionSymbol dfs ) {
    super(dfs);
    _delegate = rdfsDelegate;
    setType(new FunctionType((FunctionType) _delegate.getType(), dfs.getGosuClass()));
  }

  @Override
  public ReducedDynamicFunctionSymbol getBackingDfs() {
    return _delegate;
  }
}
