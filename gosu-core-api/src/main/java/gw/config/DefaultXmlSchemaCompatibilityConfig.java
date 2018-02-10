/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import manifold.api.service.BaseService;

public class DefaultXmlSchemaCompatibilityConfig extends BaseService implements IXmlSchemaCompatibilityConfig {

  @Override
  public boolean useCompatibilityMode(String namespace) {
    return false;
  }

}
