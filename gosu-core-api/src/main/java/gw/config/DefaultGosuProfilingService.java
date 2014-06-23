/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.util.ILogger;

/**
 */
public class DefaultGosuProfilingService extends BaseService implements IGosuProfilingService {
  private long _time0 = 0;

  /**
   * This will log a profiling event, note that the start time and end times should have been captured
   * from the same clock, for example IEntityAccess.getCurrentTime().
   *
   * @param startTime the start of the profiled code
   * @param endTime   the end of the profiled code (if 0 will use IEntityAccess.getCurrentTime())
   * @param path      the path that was taken to reach this place (A called B called C could be A->B->C)
   * @param location  this would be the location (maybe file#linenumb)
   * @param count     the number of times this time represented
   * @param waitTime  any wait times that were consumed during this execution
   */
  public void completed(long startTime, long endTime, String path, String location, int count, long waitTime) {
   ILogger logger = CommonServices.getEntityAccess().getLogger();
    if (logger.isDebugEnabled()) {
      if (endTime <= 0) {
        endTime = CommonServices.getEntityAccess().getCurrentTime().getTime();
      }
      logger.debug("At " + (endTime - _time0)  + " msecs: executed '"
              + path + "' (" + location + ") "
              + (count > 1 ? count + " times " : "")
              + "in " + (endTime - startTime) + " msecs"
              + (waitTime > 0 ? " wait=" + waitTime + " msecs " : "")
      );
    }
  }

  @Override
  protected void doInit() {
    _time0 = CommonServices.getEntityAccess().getCurrentTime().getTime(); 
  }
}
