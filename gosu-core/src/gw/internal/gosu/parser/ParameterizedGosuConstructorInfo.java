/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.gs.IGosuConstructorInfo;

/**
 */
public class ParameterizedGosuConstructorInfo extends GosuConstructorInfo {

  private IGosuConstructorInfo _delegate;

  public ParameterizedGosuConstructorInfo(IFeatureInfo container, DynamicFunctionSymbol dfs, IGosuConstructorInfo delegate) {
    super(container, dfs);
    this._delegate = delegate;
  }

  public IGosuConstructorInfo getBackingConstructorInfo() {
    return _delegate;
  }
}
