/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

/**
 */
public interface IProject
{
  String getName();

  Object getNativeProject();

  boolean isDisposed();

  boolean isHeadless();

  boolean isShadowMode();
}
