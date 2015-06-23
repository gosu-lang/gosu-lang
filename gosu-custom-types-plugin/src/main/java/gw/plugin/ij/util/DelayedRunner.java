/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.collect.Maps;
import com.intellij.openapi.diagnostic.Logger;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Executes a task after a certain time delay. The task can be rescheduled which causes the old task to be cancelled
 * if it did not execute yet. Useful for reducing the frequency of refresh operations.
 * Tasks are executed on a timer thread.
 */
public class DelayedRunner
{
  private static final Logger LOG = Logger.getInstance(DelayedRunner.class);

  private final Timer timer = new Timer(true);
  private final Map<String, TimerTask> tasksByKey = Maps.newHashMap();

  public synchronized void scheduleTask(final String key, long millis, Runnable userTask) {
    TimerTask refreshTask = tasksByKey.get(key);
    if (refreshTask != null) {
      refreshTask.cancel();
    }
    refreshTask = new MyDelayedTask(key, userTask);
    tasksByKey.put(key, refreshTask);
    timer.schedule(refreshTask, millis);
  }

  private class MyDelayedTask extends TimerTask {
    private final String key;
    private final Runnable userTask;

    public MyDelayedTask(String key, Runnable userTask) {
      this.key = key;
      this.userTask = userTask;
    }

    public void run() {
      tasksByKey.remove(key);
      try {
        userTask.run();
      } catch (Throwable e) {
        LOG.error("DelayedRunner task threw an exception.", e);
      }
    }
  }
}
