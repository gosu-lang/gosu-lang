/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.compiler.impl.CompileDriver;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.Compiler;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.project.Project;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.compiler.GosuCompiler;


public class RebuildGosuAction extends TypeSystemAwareAction {
  public void actionPerformed(AnActionEvent e) {
    final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    final CompileDriver compileDriver = new CompileDriver(project);
    compileDriver.setCompilerFilter(new CompilerFilter() {
      public boolean acceptCompiler(Compiler compiler) {
        return compiler instanceof GosuCompiler;
      }
    });

    final long t1 = System.nanoTime();
    compileDriver.rebuild(new CompileStatusNotification() {
      public void finished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
        printTiming("Gosu compilation", 0, t1);
      }
    });
  }
}
