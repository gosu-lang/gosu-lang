/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.reflect.module.IModule;

import java.io.File;

public interface IPlatformHelper extends IService {

  ExecutionMode getExecutionMode();

  boolean shouldCacheTypeNames();

  void refresh(IModule module);

  boolean isPathIgnored(String relativePath);

  File getIndexFile(String id);

  File getIDEACachesDirFile();

  public String getIDEACachesDir();

  public File getIDEACorruptionMarkerFile();
}
