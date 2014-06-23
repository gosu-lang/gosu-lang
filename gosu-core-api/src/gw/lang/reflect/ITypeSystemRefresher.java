/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface ITypeSystemRefresher
{
  void maybeRefresh();

  void maybeRefresh(boolean force);
}