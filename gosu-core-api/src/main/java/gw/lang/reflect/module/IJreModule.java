/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IJreModule extends IModule {
  String JRE_MODULE_NAME = "_jre_module_";

  Object getNativeSDK();

  void setNativeSDK(Object nativeSDK);

}
