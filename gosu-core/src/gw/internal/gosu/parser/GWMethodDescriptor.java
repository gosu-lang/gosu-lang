/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.BeanInfoUtil;
import gw.lang.reflect.IScriptabilityModifier;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;

public class GWMethodDescriptor extends FeatureDescriptor {
  private Method _method;

  public GWMethodDescriptor(Method method) {
    _method = method;
  }

  public synchronized Method getMethod() {
    return _method;
  }

  public String getName() {
    return _method.getName();
  }

  public boolean isHidden() {
    return false;
  }

  public boolean isVisible(IScriptabilityModifier constraint) {
    return BeanInfoUtil.isVisible(this, constraint);
  }
}
