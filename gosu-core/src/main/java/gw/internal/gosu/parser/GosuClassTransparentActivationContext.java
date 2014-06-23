/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.gs.IGosuClass;

class GosuClassTransparentActivationContext extends TransparentActivationContext {
  private boolean _hasLabel;

  public GosuClassTransparentActivationContext(IGosuClass gosuClass, boolean hasLabel) {
    super(gosuClass);
    _hasLabel = hasLabel;
  }

  @Override
  public String getLabel() {
    return _hasLabel ? ((IGosuClass)getContext()).getName() : null;
  }
}
