/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.perf;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.plugin.ij.actions.TypeSystemAwareAction;

import javax.swing.*;

public class PerfAction extends TypeSystemAwareAction {
  static int THREAD_INDEX = 0;
  private PerfTask task = new ParseTask();

  @Override
  public void actionPerformed(AnActionEvent e) {
    THREAD_INDEX++;
    new Thread("X Perf Thread " + THREAD_INDEX) {
      public void run() {
        int N = task.getCount();
        long dt = 0;
        System.out.println("Running perf action...");
        for (int i = 0; i < N; i++) {
          setUp();
          long t1 = System.nanoTime();
          try {
            task.run();
          } finally {
            dt += System.nanoTime() - t1;
          }
        }
        System.out.println("Perf action ran " + N + " times on thread '" + getName() + "': " + Math.round(dt * 1e-6 / N) + "ms/iteration. ");
      }
    }.start();
  }

  private void setUp() {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          task.setup();
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
