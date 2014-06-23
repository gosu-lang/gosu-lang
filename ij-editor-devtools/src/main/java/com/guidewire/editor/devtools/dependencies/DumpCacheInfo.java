/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.dependencies;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.compiler.FileDependencyCache;
import gw.plugin.ij.compiler.GosuCompilerMonitor;

import java.util.Set;

public class DumpCacheInfo extends TypeSystemAwareAction {
  private void printIndex(Set<VirtualFile> files) {
    final Multiset<String> stats = HashMultiset.create();
    for (VirtualFile file : files) {
      stats.add(FileUtil.getExtension(file.getPath()));
    }

    for (String ext : stats.elementSet()) {
      System.out.println(String.format("    %s: %d", ext, stats.count(ext)));
    }
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    final FileDependencyCache cache = GosuCompilerMonitor.getInstance(e.getProject()).getDependencyCache();
    System.out.println(String.format("Cache: %d entries", cache.size()));
    printIndex(cache.keySet());
    System.out.println();
  }
}
