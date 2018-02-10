/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.reflect.ITypeLoader;
import manifold.api.service.BaseService;

public class DefaultGosuInitializationHooks extends BaseService implements IGosuInitializationHooks
{

  @Override
  public void beforeTypeLoaderCreation(Class typeLoaderClass) {
    // Do nothing
  }

  @Override
  public void afterTypeLoaderCreation() {
    // Do nothing
  }

}