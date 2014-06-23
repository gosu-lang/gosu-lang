/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

/**
 * This is a interface for profiling in modules before pl.  Intially the only use is in ph, and they
 * only want to profile completed actions, so just publish the one method.
 *
 * @author dandrews
 */
public interface IGosuProfilingService extends IService {
  /** This will log a profiling event, note that the start time and end times should have been captured
   * from the same clock, for example IEntityAccess.getCurrentTime().
   *
   * @param startTime the start of the profiled code
   * @param endTime the end of the profiled code (if 0 will use IEntityAccess.getCurrentTime())
   * @param path the path that was taken to reach this place (A called B called C could be A->B->C)
   * @param location this would be the location (maybe file#linenumb)
   * @param count the number of times this time represented
   * @param waitTime any wait times that were consumed during this execution
   */
  void completed(long startTime, long endTime, String path, String location, int count, long waitTime);
}