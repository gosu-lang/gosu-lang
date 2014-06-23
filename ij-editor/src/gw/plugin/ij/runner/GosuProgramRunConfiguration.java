/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.google.common.collect.Lists;
import com.intellij.execution.CantRunException;
import com.intellij.execution.CommonJavaRunConfigurationParameters;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.PathUtil;
import com.intellij.util.containers.hash.LinkedHashMap;
import gw.plugin.ij.filetypes.GosuProgramFileProvider;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.sdk.GosuSdkAdditionalData;
import gw.plugin.ij.sdk.GosuSdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class GosuProgramRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements CommonJavaRunConfigurationParameters {
  public String _vmParams;
  @Nullable
  public String _strWorkDir;
  public String _strProgramParams;
  public String _strProgramPath;
  public String _fqn;
  private Map<String, String> _envs = new LinkedHashMap<>();
  private boolean _passParentEnv;

  public GosuProgramRunConfiguration(String name, @NotNull Project project, ConfigurationFactory factory) {
    super(name, new RunConfigurationModule(project), factory);
    _strWorkDir = PathUtil.getLocalPath(project.getBaseDir());
    _passParentEnv = true;
  }

  @Override
  public boolean excludeCompileBeforeLaunchOption() {
    return false; //isTester();
  }

  private boolean isTester() {
    return getFqn() != null && getFqn().endsWith('.' + GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME);
  }

  @NotNull
  @Override
  protected ModuleBasedConfiguration createInstance() {
    return new GosuProgramRunConfiguration(getName(), getProject(), getFactory());
  }

  public void setWorkDir(String dir) {
    _strWorkDir = dir;
  }

  @Nullable
  public String getWorkDir() {
    return _strWorkDir;
  }

  public String getFqn() {
    return _fqn;
  }

  public void setFqn(String fqn) {
    _fqn = fqn;
  }

  @Nullable
  public Module getModule() {
    return getConfigurationModule().getModule();
  }

  @Nullable
  private String getAbsoluteWorkDir() {
    if (!new File(_strWorkDir).isAbsolute()) {
      return new File(getProject().getLocation(), _strWorkDir).getAbsolutePath();
    }
    return _strWorkDir;
  }

  public Collection<Module> getValidModules() {
    final Module[] modules = ModuleManager.getInstance(getProject()).getModules();
    final GosuProgramRunner runner = findConfiguration();
    if (runner != null) {
      final ArrayList<Module> result = Lists.newArrayList();
      for (Module module : modules) {
        if (runner.isValidModule(module)) {
          result.add(module);
        }
      }
      return result;
    } else {
      return Arrays.asList(modules);
    }
  }

  @Nullable
  private GosuProgramRunner findConfiguration() {
    final VirtualFile scriptFile = getProgramFile();
    if (scriptFile == null) {
      return null;
    }

    final PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(scriptFile);
    if (psiFile != null && !GosuProgramFileProvider.isProgram(psiFile.getVirtualFile())) {
      return null;
    }

    return new DefaultGosuRunner();
  }

  public void readExternal(Element element) throws InvalidDataException {
    PathMacroManager.getInstance(getProject()).expandPaths(element);
    super.readExternal( element );
    readModule( element );
    _strProgramPath = JDOMExternalizer.readString(element, "path");
    _vmParams = JDOMExternalizer.readString(element, "vmparams");
    _strProgramParams = JDOMExternalizer.readString(element, "params");
    String wrk = JDOMExternalizer.readString(element, "workDir");
    if (!".".equals(wrk)) {
      _strWorkDir = wrk;
    }
    _envs.clear();
    JDOMExternalizer.readMap(element, _envs, null, "env");
  }

  public void writeExternal(Element element) throws WriteExternalException {
    super.writeExternal(element);
    writeModule(element);
    JDOMExternalizer.write(element, "path", _strProgramPath);
    JDOMExternalizer.write(element, "vmparams", _vmParams);
    JDOMExternalizer.write(element, "params", _strProgramParams);
    JDOMExternalizer.write(element, "workDir", _strWorkDir);
    JDOMExternalizer.writeMap(element, _envs, null, "env");
    PathMacroManager.getInstance( getProject() ).collapsePathsRecursively(element);
  }

  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
    final VirtualFile programFile = getProgramFile();
    if (programFile == null) {
      throw new CantRunException("Cannot find script " + _strProgramPath);
    }

    final GosuProgramRunner programRunner = findConfiguration();
    if (programRunner == null) {
      throw new CantRunException("Unknown program type " + _strProgramPath);
    }

    final Module module = getModule();
    if (!programRunner.ensureRunnerConfigured(module, getName(), getProject())) {
      return null;
    }

    final boolean tests = ProjectRootManager.getInstance(getProject()).getFileIndex().isInTestSourceContent(programFile);

    JavaCommandLineState state =
        new JavaCommandLineState(environment) {
          @NotNull
          protected JavaParameters createJavaParameters() throws ExecutionException {
            JavaParameters params = new JavaParameters();
            params.setCharset(null);
            Sdk gosuSdk;
            if (module != null) {
              gosuSdk = ModuleRootManager.getInstance(module).getSdk();
            } else {
              ProjectRootManager rootMgr = ProjectRootManagerEx.getInstance(getProject());
              gosuSdk = rootMgr.getProjectSdk();
            }
            Sdk sdk;
            assert gosuSdk != null;
            SdkTypeId sdkType = gosuSdk.getSdkType();
            if (sdkType.equals(GosuSdkType.getInstance())) {
              GosuSdkAdditionalData data = (GosuSdkAdditionalData) gosuSdk.getSdkAdditionalData();
              assert data != null;
              sdk = data.getJavaSdk();
            } else {
              sdk = gosuSdk;
            }
            params.setJdk(sdk);
            params.setWorkingDirectory(getAbsoluteWorkDir());
            programRunner.configureCommandLine(gosuSdk, params, module, tests, programFile, GosuProgramRunConfiguration.this);
            return params;
          }
        };

    state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
    return state;

  }

  @Nullable
  private VirtualFile getProgramFile() {
    if (_strProgramPath == null) {
      return null;
    }
    return LocalFileSystem.getInstance().findFileByPath(FileUtil.toSystemIndependentName(_strProgramPath));
  }

  @NotNull
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new GosuRunConfigurationEditor();
  }

  @Override
  public void setVMParameters( String value ) {
    _vmParams = value;
  }

  @Override
  public String getVMParameters() {
    return _vmParams;
  }

  @Override
  public boolean isAlternativeJrePathEnabled() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAlternativeJrePathEnabled(boolean enabled) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getAlternativeJrePath() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAlternativeJrePath(String path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getRunClass() {
    return null;
  }

  @Override
  public String getPackage() {
    return null;
  }

  @Override
  public void setProgramParameters( @Nullable String value ) {
    _strProgramParams = value;
  }

  @Override
  public String getProgramParameters() {
    return _strProgramParams;
  }

  @Override
  public void setWorkingDirectory( @Nullable String value ) {
    _strWorkDir = value;
  }

  @Override
  public String getWorkingDirectory() {
    return _strWorkDir;
  }

  @Override
  public void setEnvs( @NotNull Map<String, String> envs ) {
    _envs.clear();
    _envs.putAll(envs);
  }

  @NotNull
  @Override
  public Map<String, String> getEnvs() {
    return _envs;
  }

  @Override
  public void setPassParentEnvs( boolean passParentEnvs ) {
    _passParentEnv = passParentEnvs;
  }

  @Override
  public boolean isPassParentEnvs() {
    return _passParentEnv;
  }
}
