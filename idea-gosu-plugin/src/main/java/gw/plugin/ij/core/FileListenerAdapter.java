/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import gw.fs.IResource;

public class FileListenerAdapter implements IFileListener {

  @Override
  public void modified(IResource file, String oldText, String newText) {
  }

  @Override
  public void modified(IResource file) {
  }

  @Override
  public void deleted(IResource file) {
  }

  @Override
  public void created(IResource file) {
  }
}
