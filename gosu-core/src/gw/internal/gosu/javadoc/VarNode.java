/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IVarNode;

/**
 */
class VarNode extends BaseFeatureNode implements IVarNode
{
  private String _name;

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

}
