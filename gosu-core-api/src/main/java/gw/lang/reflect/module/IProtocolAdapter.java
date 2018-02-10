/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import gw.lang.UnstableAPI;

import java.net.URL;

@UnstableAPI
public interface IProtocolAdapter {
  String[] getSupportedProtocols();

  IDirectory getIDirectory(URL url);

  IFile getIFile(URL url);

}
