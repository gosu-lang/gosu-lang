/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import com.google.common.base.Function;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
* Convert {@link java.io.File} to absolute path.
*/
enum ToPath implements Function<File, String> {
  INSTANCE;

  @Override
  public String apply(File input) {
    return input.getAbsolutePath();
  }
}
