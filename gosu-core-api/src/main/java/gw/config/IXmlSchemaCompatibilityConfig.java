/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import manifold.api.service.IService;

public interface IXmlSchemaCompatibilityConfig extends IService
{

  /**
   * Should compatibility mode (old typeloader) be used for the specified schema?
   * @param namespace the schema namespace
   * @return true if compatibility mode (old typeloader) should be used for the specified schema
   */
  boolean useCompatibilityMode( String namespace );

}
