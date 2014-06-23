/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.dependencies;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.compiler.FileDependencyCache;
import gw.plugin.ij.compiler.FileDependencyInfo;
import gw.plugin.ij.compiler.GosuCompilerMonitor;

import java.util.Comparator;

import static com.google.common.collect.Iterables.limit;

public class DumpCompilationTime extends TypeSystemAwareAction {
  private static final int COUNT = 20;
  private static Comparator<FileDependencyInfo> TIME_COMPARATOR = new Comparator<FileDependencyInfo>() {
    @Override
    public int compare(FileDependencyInfo o1, FileDependencyInfo o2) {
      return Longs.compare(o1.getCompileTime(), o2.getCompileTime());
    }
  };

  private static String formatTime(long millis) {
    return millis / 1000 + "s";
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    long totalTime = 0;
    final FileDependencyCache cache = GosuCompilerMonitor.getInstance(e.getProject()).getDependencyCache();
    System.out.println(String.format("Cache: %d entries", cache.size()));
    long moduleTime = 0;
    for (FileDependencyInfo info : cache.values()) {
      moduleTime += info.getCompileTime();
    }

    for (FileDependencyInfo info : limit(Ordering.from(TIME_COMPARATOR).reverse().sortedCopy(cache.values()), COUNT)) {
      System.out.println(String.format("    %s - %dms", info.getFile().getPath(), info.getCompileTime()));
    }

    totalTime += moduleTime;
    System.out.println(String.format("Total compilation time: %s", formatTime(totalTime)));
    System.out.println();
  }
}
