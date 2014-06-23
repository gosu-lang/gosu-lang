/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical;

import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@UnstableAPI
public class FuzzyTimestampCachingPhysicalFileSystem extends AbstractCachingPhysicalFileSystem {

  private Map<String, DirCacheInfo> _dirCache = new HashMap<String, DirCacheInfo>();

  private Lock _lock = new ReentrantLock();

  public FuzzyTimestampCachingPhysicalFileSystem(IPhysicalFileSystem delegate) {
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
      return cacheInfo.listFiles();
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
    private ResourcePath _absolutePath;
    private long _lastFileTimestamp;
    private long _lastRefreshTimestamp;

    private DirCacheInfo(ResourcePath absolutePath) {
      _absolutePath = absolutePath;
      _lastFileTimestamp = -1;
    }

    public List<? extends IFileMetadata> listFiles() {
      maybeRefreshList();
      return _files;
    }

    private void maybeRefreshList() {
      if (_lastFileTimestamp == -1) {
        doRefreshImpl();
      } else {
        long currentTimestamp = _delegate.getFileMetadata(_absolutePath).lastModifiedTime();
        if (currentTimestamp == 0) {
          // If the timestamp is 0, assume it's been deleted
          _files = Collections.emptyList();
        } else if (_lastFileTimestamp != currentTimestamp) {
          doRefreshImpl();
        } else {
          long refreshDelta = _lastRefreshTimestamp - currentTimestamp;
          if(refreshDelta > -16 && refreshDelta < 16) {
            doRefreshImpl();
          }
        }
      }
    }

    private void doRefreshImpl() {
      _lastRefreshTimestamp = System.currentTimeMillis();
      _files = _delegate.listFiles(_absolutePath);
    }
  }
}
