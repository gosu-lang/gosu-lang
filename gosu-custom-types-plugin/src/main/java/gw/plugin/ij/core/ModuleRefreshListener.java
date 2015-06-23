/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.Function;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModuleRefreshListener implements ModuleListener {
  private void createModule(@NotNull Module ijModule) {
    final IModule gsModule = TypeSystemStarter.instance( ijModule.getProject() ).defineModule(ijModule);
    final IExecutionEnvironment env = gsModule.getExecutionEnvironment();

    // Add implicit JRE module dependency
    final IModule jreModule = env.getJreModule();
    gsModule.addDependency(new Dependency(jreModule, true));

    // Add to the execution environment
    env.addModule(gsModule);
  }

  private void deleteModule(@NotNull IModule gsModule) {
    final IExecutionEnvironment env = gsModule.getExecutionEnvironment();
    if (gsModule != env.getJreModule()) {
      TypeSystem.refresh( gsModule );
      env.removeModule(gsModule);
      for (IModule module : env.getModules()) {
        removeDependency(module, gsModule);
      }
    }
  }

  private void removeDependency(IModule module, IModule deleted) {
    for (Dependency dep : module.getDependencies()) {
      if (dep.getModule() == deleted) {
        module.removeDependency(dep);
        break;
      }
    }
  }

  // ModuleListener
  public void moduleAdded(Project project, Module ijModule) {
    createModule(ijModule);
  }

  public void beforeModuleRemoved(Project project, Module ijModule) {
    final IModule gsModule = GosuModuleUtil.getModule(ijModule);
    deleteModule(gsModule);
  }

  public void moduleRemoved(Project project, Module module) {
    // Nothing to do
  }

  @Override
  public void modulesRenamed( Project project, List<Module> modules, Function<Module, String> moduleStringFunction )
  {
    // hmmm
  }

  public void modulesRenamed(Project project, List<Module> ijModules) {
    for (Module ijModule : ijModules) {
      for (IModule gsModule : GosuModuleUtil.getModules(project)) {
        if (gsModule.getNativeModule() == ijModule) {
          TypeSystem.getExecutionEnvironment().renameModule(gsModule, ijModule.getName());
        }
      }
    }
  }
}
