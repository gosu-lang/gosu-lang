/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.jar;

import gw.fs.IDirectory;
import gw.lang.UnstableAPI;

@UnstableAPI
public interface IJarFileDirectory extends IDirectory {
  JarEntryDirectoryImpl getOrCreateDirectory(String relativeName);
  JarEntryFileImpl getOrCreateFile(String relativeName);
}
