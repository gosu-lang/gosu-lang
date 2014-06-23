/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.dependencies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.compiler.FileDependencyCache;
import gw.plugin.ij.compiler.FileDependencyInfo;
import gw.plugin.ij.compiler.GosuCompilerMonitor;
import gw.plugin.ij.util.GosuModuleUtil;

import java.util.Collection;

public class DumpDependencyInfo extends TypeSystemAwareAction {
  private void print(String title, Collection<VirtualFile> files) {
    System.out.println(String.format("  %s (%d):", title, files.size()));
    if (files.isEmpty()) {
      System.out.println("    None");
    } else {
      for (VirtualFile f : files) {
        System.out.println("    " + f);
      }
    }
  }

  private void printDK(String title, Collection<String> strings) {
    System.out.println(String.format("  %s (%d):", title, strings.size()));
    if (strings.isEmpty()) {
      System.out.println("    None");
    } else {
      for (String f : strings) {
        System.out.println("    " + f);
      }
    }
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    final Project project = e.getProject();
    final FileDependencyCache cache = GosuCompilerMonitor.getInstance(project).getDependencyCache();
    for (VirtualFile file : e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY)) {
      final IModule module = GosuModuleUtil.findModuleForFile(file, project);
      final FileDependencyInfo info = cache.get(file);
      if (info != null) {
        System.out.println(String.format("Cache for '%s' [%d] - %dms", file, info.getFingerprint(), info.getCompileTime()));
        print("Dependencies", cache.getDependencies(file));
        print("Dependents", cache.getDependentsOn(file));
        printDK("DisplayKeys", cache.get(file).getDisplayKeys());
        System.out.println();
      } else {
        System.out.println(String.format("No cache entry for '%s'", file));
      }
    }
  }
}
