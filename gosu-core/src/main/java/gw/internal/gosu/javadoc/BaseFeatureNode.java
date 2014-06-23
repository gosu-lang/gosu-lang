/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IBaseFeatureNode;

/**
 */
abstract class BaseFeatureNode implements IBaseFeatureNode, IDocNodeWithDescription {
  private String _description;
  private String _deprecated;

  public String getDescription() {
    return _description;
  }

  public void setDescription( String description ) {
    _description = description;
  }

  public String getDeprecated() {
    return _deprecated;
  }

  public boolean isDeprecated() {
    return getDeprecated() != null;
  }

  public void setDeprecated( String deprecated ) {
    _deprecated = deprecated;
  }

}
