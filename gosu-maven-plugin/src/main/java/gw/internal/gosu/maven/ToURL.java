/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import com.google.common.base.Function;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
* Convert absolute path to {@link java.net.URL}
*/
enum ToURL implements Function<File, URL> {
  INSTANCE;

  @Override
  public URL apply(File input) {
    try {
      return input.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }
}
