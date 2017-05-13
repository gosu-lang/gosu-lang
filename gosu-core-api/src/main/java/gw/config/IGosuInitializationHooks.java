/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import manifold.api.service.IService;

public interface IGosuInitializationHooks extends IService
{
  void beforeTypeLoaderCreation(Class typeLoaderClass);
  void afterTypeLoaderCreation();
}