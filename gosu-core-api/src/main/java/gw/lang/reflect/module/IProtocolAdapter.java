/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.UnstableAPI;

import java.net.URL;

@UnstableAPI
public interface IProtocolAdapter {
  String[] getSupportedProtocols();

  IDirectory getIDirectory(URL url);

  IFile getIFile(URL url);

}
