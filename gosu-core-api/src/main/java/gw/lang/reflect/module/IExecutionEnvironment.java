/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.internal.gosu.parser.TypeSystemState;
import gw.lang.UnstableAPI;

@UnstableAPI
public interface IExecutionEnvironment
{
  String DEFAULT_SINGLE_MODULE_NAME = "_default_";

  IProject getProject();
  IModule getModule();

  TypeSystemState getState();

  boolean isShadowingMode();
}
