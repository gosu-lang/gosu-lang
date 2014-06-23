/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.intellij.compiler.CompilerManagerImpl;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileTask;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;

public class CompilerLogger {
  private Project project;
  private long t1;

  public CompilerLogger(Project project) {
    this.project = project;
  }

  private CompileTask beforeTask = new CompileTask() {
    public boolean execute(CompileContext context) {
      t1 = System.currentTimeMillis();
      return true;
    }
  };

  private CompileTask afterTask = new CompileTask() {
    public boolean execute(CompileContext context) {
      long dt = (System.currentTimeMillis() - t1)/1000;
      String type = context.isRebuild() ? "Full" : "Incremental";
      System.out.println(type + " compilation done in " + dt + " sec.");
      return true;
    }
  };

  public void start() {
    CompilerManager compilerManager = CompilerManagerImpl.getInstance(project);
    compilerManager.addBeforeTask(beforeTask);
    compilerManager.addAfterTask(afterTask);
  }

}
