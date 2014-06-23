/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical;

import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@UnstableAPI
public class FullCachingPhysicalFileSystem extends AbstractCachingPhysicalFileSystem {

  private Map<String, DirCacheInfo> _dirCache = new HashMap<String, DirCacheInfo>();

  private Lock _lock = new ReentrantLock();

  public FullCachingPhysicalFileSystem(IPhysicalFileSystem delegate) {
    super(delegate);
  }


  @Override
  public List<? extends IFileMetadata> listFiles(ResourcePath directoryPath) {
    _lock.lock();
    try {
      String pathString = directoryPath.getPathString();
      DirCacheInfo cacheInfo = _dirCache.get(pathString);
      if (cacheInfo == null) {
        cacheInfo = new DirCacheInfo(directoryPath);
        _dirCache.put(pathString, cacheInfo);
      }
      return cacheInfo._files;
    } finally {
      _lock.unlock();
    }
  }

  @Override
  public IFileMetadata getFileMetadata(ResourcePath filePath) {
    return _delegate.getFileMetadata(filePath);
  }

  @Override
  public void clearDirectoryCaches(ResourcePath dirPath) {
    _lock.lock();
    try {
      _dirCache.remove(dirPath.getPathString());
    } finally {
      _lock.unlock();
    }

    _delegate.clearDirectoryCaches(dirPath);
  }

  public void clearAllCaches() {
    _lock.lock();
    try {
      _dirCache.clear();
    } finally {
      _lock.unlock();
    }

    _delegate.clearAllCaches();
  }

  // TODO - Timestamping?
  private class DirCacheInfo {
    private List<? extends IFileMetadata> _files;

    private DirCacheInfo(ResourcePath absolutePath) {
      _files = _delegate.listFiles(absolutePath);
    }
  }
}
