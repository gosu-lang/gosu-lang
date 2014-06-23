/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.UnstableAPI;

@UnstableAPI
public class TypeSystemRefresher implements ITypeSystemRefresher {
  private static final long KB = 1024;
  private static final long MB = KB * KB;
  private static TypeSystemRefresher _instance;

  public static synchronized TypeSystemRefresher getInstance() {
    if (_instance == null) {
      _instance = new TypeSystemRefresher();
    }
    return _instance;
  }

  protected TypeSystemRefresher() {}

  @Override
  public void maybeRefresh()
  {
    maybeRefresh(false);
  }

  @Override
  public void maybeRefresh(boolean force)
  {
    if( force || isNearMemoryBoundary() )
    {
      doRefresh();
    }
  }

  private void doRefresh() {
    TypeSystem.refresh( true );
    doPostRefreshCleanup();
    Runtime.getRuntime().gc();
    Thread.yield();
  }

  protected void doPostRefreshCleanup() {
    // nothing in base
  }

//  private String getMemoryUsage()
//  {
//    long lFreeInHeap = Runtime.getRuntime().freeMemory()/MB;
//    long lCurrentHeapSize = Runtime.getRuntime().totalMemory()/MB;
//    long lMaxHeap = Runtime.getRuntime().maxMemory()/MB;
//    long lUsed = lCurrentHeapSize - lFreeInHeap;
//
//    return lUsed + "M of " + lCurrentHeapSize + "M (max " + lMaxHeap + "M)";
//  }
//
  private boolean isNearMemoryBoundary()
  {
    long lFreeInHeap = Runtime.getRuntime().freeMemory();
    long lCurrentHeapSize = Runtime.getRuntime().totalMemory();
    long lMaxHeap = Runtime.getRuntime().maxMemory();

//    // If the heap size is near the max heap size and the free size is smallish...
    return lCurrentHeapSize > (lMaxHeap - 50*MB) &&
           lFreeInHeap < 50*MB;

    // If the free size is smallish...
//    return lFreeInHeap < 5*MB;

  }
}
