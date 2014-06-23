/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util.transform.java;

class Wrap {
  String JAVA_WRAP_START;
  String JAVA_WRAP_END;
  String GOSU_WRAP_START;
  String GOSU_WRAP_END;

  Wrap(String js, String je, String gs, String ge) {
    JAVA_WRAP_START = js;
    JAVA_WRAP_END = je;
    GOSU_WRAP_START = gs;
    GOSU_WRAP_END = ge;
  }
}

