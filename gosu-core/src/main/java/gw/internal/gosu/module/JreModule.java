/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;

/**
 */
public class JreModule extends Module implements IJreModule
{
  private Object _nativeSDK;

  public JreModule( ExecutionEnvironment execEnv )
  {
    super( execEnv, JRE_MODULE_NAME);
  }

  @Override
  public Object getNativeSDK() {
    return _nativeSDK;
  }

  public void setNativeSDK(Object nativeSDK) {
    _nativeSDK = nativeSDK;
  }
}
