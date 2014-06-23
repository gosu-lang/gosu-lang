/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.resources;

import gw.config.CommonServices;

public class ResourceKey
{
  private String _strKey;

  public ResourceKey(String strKey)
  {
    _strKey = strKey;
  }

  public String getKey()
  {
    return _strKey;
  }

  @Override
  public String toString() {
    return CommonServices.getGosuLocalizationService().localize(this);
  }
}
