/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.psi.impl.GosuClassParseData;

public interface IGosuFile extends IGosuFileBase {
  void addImport(String qualifiedName);

  GosuClassParseData getParseData();

  IGosuClass reparseGosuFromPsi();
}
