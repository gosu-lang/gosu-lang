/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IExceptionNode;

/**
 */
class ExceptionNode implements IExceptionNode, IDocNodeWithDescription {

  private String _description;
  private String _type;

  public String getDescription() {
    return _description;
  }

  public void setDescription( String description ) {
    _description = description;
  }

  public String getType() {
    return _type;
  }

  public void setType(String type) {
    _type = type;
  }
}