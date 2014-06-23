/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;

@PublishInGosu
public abstract class XmlSchemaAccess<T extends XmlElement> {

  /**
   * Returns the schema represented by this XmlSchemaAccess type.
   *
   * @return the schema
   */
  public abstract T getSchema();

  /**
   * Returns the package name of this schema.
   *
   * @return the package name of this schema
   */
  @Override
  public abstract String toString();

}