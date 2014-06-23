/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a very simple implementation of an LRUMap using the
 * LinkedHashMap special constructor.
 */
public class LRUMap extends LinkedHashMap
{
  private int _maxEntries;

  public LRUMap(int maxEntries)
  {
    super(maxEntries, .75F, true);
    _maxEntries = maxEntries;
  }

  @Override
  protected boolean removeEldestEntry( Map.Entry eldest )
  {
    return size() > _maxEntries;
  }
  
}
