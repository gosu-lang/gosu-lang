/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.fs.physical;

import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

@UnstableAPI
public abstract class AbstractCachingPhysicalFileSystem implements IPhysicalFileSystem {

  protected final IPhysicalFileSystem _delegate;

  public AbstractCachingPhysicalFileSystem(IPhysicalFileSystem delegate) {
    _delegate = delegate;
  }

  @Override
  public boolean exists(ResourcePath resourcePath) {
    return getFileMetadata(resourcePath) != null;
  }

  @Override
  public boolean delete(ResourcePath filePath) {
    return _delegate.delete(filePath);
  }

  @Override
  public boolean mkdir(ResourcePath dirPath) {
    return _delegate.mkdir(dirPath);
  }
}
