/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.compiler.impl.CompileDriver;
import com.intellij.compiler.impl.javaCompiler.JavaCompiler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.Compiler;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.project.Project;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.compiler.GosuCompiler;
import gw.plugin.ij.core.PluginLoaderUtil;


public class RebuildJavaAction extends TypeSystemAwareAction {
  public void actionPerformed(final AnActionEvent e) {
    PluginLoaderUtil.instance(e.getProject()).removeCompiler();

    final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    final CompileDriver compileDriver = new CompileDriver(project);
    compileDriver.setCompilerFilter(new CompilerFilter() {
      public boolean acceptCompiler(Compiler compiler) {
        return compiler instanceof JavaCompiler;
      }
    });

    final long t1 = System.nanoTime();
    compileDriver.rebuild(new CompileStatusNotification() {
      public void finished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
        PluginLoaderUtil.instance(e.getProject()).addCompiler();
        printTiming("Java compilation", 0, t1);
      }
    });
  }
}
