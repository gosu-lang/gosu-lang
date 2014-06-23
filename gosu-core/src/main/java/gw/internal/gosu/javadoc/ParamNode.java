/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IParamNode;

/**
 */
class ParamNode implements IParamNode
{
  private String _description;
  private String _name;
  private String _type;

  public String getDescription() {
    return _description;
  }

  public void setDescription(String value) {
    _description = value;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  public String getType() {
    return _type;
  }

  public void setType(String type) {
    _type = type;
  }
}
