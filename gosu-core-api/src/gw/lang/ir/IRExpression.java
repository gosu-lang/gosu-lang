/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public abstract class IRExpression extends IRElement {

  public abstract IRType getType();
  
}
