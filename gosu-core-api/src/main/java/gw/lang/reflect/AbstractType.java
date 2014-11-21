/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.GosuShop;

public abstract class AbstractType implements IType {
  private IMetaType _metaType;
  private IMetaType _literalMetaType;

  @Override
  public IMetaType getMetaType() {
    if (_metaType == null) {
      _metaType = GosuShop.createMetaType(getTheRef(), false);
    }
    return _metaType;
  }

  @Override
  public IMetaType getLiteralMetaType() {
    if (_literalMetaType == null) {
      _literalMetaType = GosuShop.createMetaType(getTheRef(), true);
    }
    return _literalMetaType;
  }

  protected IType getTheRef() {
    if (this instanceof INonLoadableType) {
      return this;
    } else {
      return TypeSystem.getOrCreateTypeReference(this);
    }
  }
}
