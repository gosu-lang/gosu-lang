/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GosuProgramRunner {

  public abstract boolean isValidModule(@NotNull Module module);

  public abstract boolean ensureRunnerConfigured(@Nullable Module module, final String confName, final Project project);

  public abstract void configureCommandLine(Sdk gosuSdk, JavaParameters params, @Nullable Module module, boolean tests, VirtualFile script,
                                            GosuProgramRunConfiguration configuration) throws CantRunException;

  protected static void setToolsJar(@NotNull JavaParameters params) {
    Sdk jdk = params.getJdk();
    if (jdk != null && jdk.getSdkType() instanceof JavaSdkType) {
      String toolsPath = ((JavaSdkType) jdk.getSdkType()).getToolsPath(jdk);
      if (toolsPath != null) {
        params.getVMParametersList().add("-Dtools.jar=" + toolsPath);
      }
    }
  }

  protected static void addClasspathFromRootModel(@Nullable Module module, boolean isTests, @NotNull JavaParameters params) throws CantRunException {
    if (module == null) {
      return;
    }

    final JavaParameters tmp = new JavaParameters();
    tmp.configureByModule(module, isTests ? JavaParameters.CLASSES_AND_TESTS : JavaParameters.CLASSES_ONLY);
    if (tmp.getClassPath().getVirtualFiles().isEmpty()) {
      return;
    }
    params.getClassPath().addAll(tmp.getClassPath().getPathList());
  }
}
