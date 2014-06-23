/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.mode;

import gw.lang.launch.IArgInfo;

public interface IGosuMode extends Comparable<IGosuMode> {

  void setArgInfo(IArgInfo argInfo);

  int getPriority();

  boolean accept();

  int run() throws Exception;

}
