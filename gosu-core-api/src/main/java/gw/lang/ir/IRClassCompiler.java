/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IRClassCompiler {
  byte[] compile( IRClass irClass, boolean debug );

  String compileToJava( IRClass irClass );
}
