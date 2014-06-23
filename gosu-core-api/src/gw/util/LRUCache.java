/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private final int _maxSize;

  public LRUCache(int maxSize) {
    _maxSize = maxSize;
  }

  protected boolean removeEldestEntry(Map.Entry eldest) {
    return size() > _maxSize;
  }

  public int getMaxSize() {
    return _maxSize;
  }
}
