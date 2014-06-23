/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileTask;

public class DependencyCacheCleaner implements CompileTask {
  @Override
  public boolean execute(CompileContext context) {
    if (context.isRebuild()) { // if full compile
      GosuCompilerMonitor.getInstance(context.getProject()).getDependencyCache().clear();
    }
    return true;
  }
}
