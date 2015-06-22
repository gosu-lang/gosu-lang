/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import gw.fs.IResource;

public interface IFileListener {
  void modified( IResource file, String oldText, String newText );
  void modified( IResource file );
  void deleted( IResource file );
  void created( IResource file );
}
