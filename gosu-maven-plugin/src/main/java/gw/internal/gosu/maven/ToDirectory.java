/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import com.google.common.base.Function;
import gw.config.CommonServices;
import gw.fs.IDirectory;

import java.io.File;

/**
*/
enum ToDirectory implements Function<File, IDirectory> {
  INSTANCE;

  @Override
  public IDirectory apply(File file) {
    return CommonServices.getFileSystem().getIDirectory(file);
  }
}
