/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;

import java.io.Closeable;

public interface IWrappingTestType extends IType {

  public Closeable withPropertyListener(PropertyListener listener);

  public Closeable withMethodListener(MethodListener listener);

  public Closeable withConstructorListener(ConstructorListener listener);

  interface PropertyListener {
    void onPropertyAccess(IPropertyInfo pi, Object ctx, boolean set, Object value);
  }

  interface MethodListener {
    void onMethodCall(IMethodInfo info, Object o, Object[] args);
  }

  interface ConstructorListener {
    void onConstructor(Object[] args);
  }
}
