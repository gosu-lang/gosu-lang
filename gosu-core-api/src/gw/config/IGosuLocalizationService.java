/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.parser.resources.ResourceKey;

public interface IGosuLocalizationService extends IService
{

  public String localize( ResourceKey key, Object... args );

  public boolean exists( ResourceKey key );

}