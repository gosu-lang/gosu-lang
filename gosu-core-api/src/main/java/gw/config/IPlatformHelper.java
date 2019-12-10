/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import java.io.File;

public interface IPlatformHelper extends IService {

  ExecutionMode getExecutionMode();

  boolean shouldCacheTypeNames();

  void refresh();

  boolean isPathIgnored(String relativePath);

  File getIndexFile(String id);

  File getIDEACachesDirFile();

  public String getIDEACachesDir();

  public File getIDEACorruptionMarkerFile();
}
