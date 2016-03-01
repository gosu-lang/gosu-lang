/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.IType;

public class BasicClassExtension extends BasicClass implements IGosuObject {
  private IType _type;

  public BasicClassExtension() {
  }

  @Override
  public IType getIntrinsicType() {
    return _type;
  }

  public void setType(IType type) {
    _type = type;
  }
}
