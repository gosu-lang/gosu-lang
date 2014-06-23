/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.perf;

public interface PerfTask extends Runnable{

  void setup();

  int getCount();
}
