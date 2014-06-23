/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

public class DefaultXmlSchemaCompatibilityConfig extends BaseService implements IXmlSchemaCompatibilityConfig {

  @Override
  public boolean useCompatibilityMode(String namespace) {
    return false;
  }

}
