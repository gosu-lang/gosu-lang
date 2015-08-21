/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface ISource {

  String getSource();
  void stopCachingSource();
}
