/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.gs.IGosuPropertyInfo;

/**
 */
public class ParameterizedGosuPropertyInfo extends GosuPropertyInfo {

  private IGosuPropertyInfo _delegate;

  public ParameterizedGosuPropertyInfo(IFeatureInfo container, DynamicPropertySymbol dps, IGosuPropertyInfo delegate) {
    super(container, dps);
    this._delegate = delegate;
  }

  public IGosuPropertyInfo getBackingPropertyInfo() {
    return _delegate;
  }
}
