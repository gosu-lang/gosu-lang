/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.testFramework.LightVirtualFile;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.filetypes.GosuProgramFileProvider;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuProgramRunConfigurationType implements LocatableConfigurationType {
  @NotNull
  private final GosuFactory _configurationFactory;


  public GosuProgramRunConfigurationType() {
    _configurationFactory = new GosuFactory(this);
  }

  @NotNull
  public String getDisplayName() {
    return "Gosu Program";
  }

  @NotNull
  public String getConfigurationTypeDescription() {
    return "Gosu Program";
  }

  @Nullable
  public Icon getIcon() {
    return GosuIcons.FILE_PROGRAM;
  }

  @NonNls
  @NotNull
  public String getId() {
    return "GosuProgramRunConfiguration";
  }

  @NotNull
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[]{_configurationFactory};
  }

  @NotNull
  public ConfigurationFactory getFactory() {
    return _configurationFactory;
  }

  public RunnerAndConfigurationSettings createConfigurationByLocation(@NotNull Location location) {
    final PsiElement element = location.getPsiElement();
    final PsiFile file = getProgramFile(element);
    return file != null ? createConfiguration(file) : null;
  }

  public boolean isConfigurationByLocation(RunConfiguration configuration, Location location) {
    if( !(configuration instanceof GosuProgramRunConfiguration) ) {
      return false;
    }
    if( location.getPsiElement() == null || location.getPsiElement() == null ||
        !(location.getPsiElement().getContainingFile() instanceof AbstractGosuClassFileImpl) ) {
      return false;
    }
    AbstractGosuClassFileImpl psiFile = (AbstractGosuClassFileImpl)location.getPsiElement().getContainingFile();
    return psiFile.getQualifiedClassNameFromFile().equals( ((GosuProgramRunConfiguration)configuration).getFqn() );
  }

  @Nullable
  private RunnerAndConfigurationSettings createConfiguration(@NotNull final PsiFile aClass) {
    PsiFile file = aClass.getContainingFile();
    SmartPsiElementPointer owningFileRef = file.getUserData(FileContextUtil.INJECTED_IN_ELEMENT);
    if (owningFileRef != null) {
      // don't build run configurations for programs that are really injected file fragments.
//      file = owningFileRef.getContainingFile();
      return null;
    }
    PsiDirectory dir = file.getContainingDirectory();
    VirtualFile vFile = file.getVirtualFile();


    if (dir == null && vFile instanceof LightVirtualFile) {
      // don't build run configuration for gosu tester editor (or do we want to?)
      // and rules in the Rule editor
      return null;
    }
    assert dir != null;
    assert vFile != null;

    Project project = aClass.getProject();
    RunnerAndConfigurationSettings settings = RunManager.getInstance(project).createRunConfiguration("", _configurationFactory);
    GosuProgramRunConfiguration configuration = (GosuProgramRunConfiguration) settings.getConfiguration();
    if (!(aClass instanceof GosuScratchpadFileImpl)) { // scratchpad file is not really a type
      PsiClass psiClass = ((GosuProgramFileImpl) aClass).getPsiClass();
      if (psiClass != null) {
        configuration.setFqn(psiClass.getQualifiedName());
      }
    }
    configuration.setWorkDir(dir.getVirtualFile().getPath());
    configuration._strProgramPath = vFile.getPath();
    RunConfigurationModule module = configuration.getConfigurationModule();

    String name = getConfigurationName(aClass, module);
    configuration.setName(name);
    IModule gosuModule = GosuModuleUtil.findModuleForFile(aClass.getVirtualFile(), project);
    configuration.setModule(GosuModuleUtil.getModule(gosuModule));
    return settings;
  }

  @NotNull
  private static String getConfigurationName(@NotNull PsiFile progFile, RunConfigurationModule module) {
    return progFile.getName();
  }

  @Nullable
  private static PsiFile getProgramFile(@NotNull PsiElement element) {
    final PsiFile file = element.getContainingFile();
    if (file != null && GosuProgramFileProvider.isProgram(file.getVirtualFile())) {
      return file;
    }
    return null;
  }

  public static GosuProgramRunConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(GosuProgramRunConfigurationType.class);
  }

  public static class GosuFactory extends ConfigurationFactory {
    public GosuFactory(@NotNull LocatableConfigurationType type) {
      super(type);
    }

    @NotNull
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      return new GosuProgramRunConfiguration("Gosu Program", project, this);
    }
  }
}
