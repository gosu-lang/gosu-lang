/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IBaseFeatureNode {
  String getDescription();

  void setDescription( String value );

  String getDeprecated();

  boolean isDeprecated();

  void setDeprecated( String value );

}
