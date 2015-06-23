/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbServiceImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.vfs.VirtualFile;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IGlobalModule;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuMessages;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.util.fingerprint.FP64;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModuleClasspathListener implements ModuleRootListener {
  private static final Logger LOG = Logger.getInstance(ModuleClasspathListener.class);

  public static boolean ENABLED = true;
  public static final String ARROW = " <- ";
  public static final String EXPORT = "^, ";
  public static final String NOT_EXPORT = ", ";

  @Override
  public void beforeRootsChange(ModuleRootEvent event) {
  }

  @Override
  public void rootsChanged(@NotNull ModuleRootEvent event) {
    Project project = (Project) event.getSource();
    if (!shouldProcessRootChanges(project)) {
      return;
    }
    boolean processDependencies = true;

    PluginLoaderUtil loader = PluginLoaderUtil.instance( project );
    String circularDependency = GosuModuleUtil.getCircularModuleDependency(project);
    if (circularDependency != null) {
      ExceptionUtil.showWarning(GosuBundle.message("error.dependency.line1"), GosuBundle.message("error.dependency.line2", circularDependency));
      processDependencies = false;
    } else if (!loader.isStarted() && loader.getFailureReason() == PluginFailureReason.CIRCULAR_DEPENDENCY) {
      startPlugin(project, GosuBundle.message("error.dependency.resolved"));
      return;
    }

    if (loader.isStarted()) {
      if (processDependencies) {
        processModuleDependenciesChange(project);
      }

      final Module[] modules = ModuleManager.getInstance(project).getModules();
      for (Module ijModule : modules) {
        IModule gosuModule = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ).getModule(ijModule.getName());
        processClasspathChange(gosuModule, ijModule);
        changeSourceRoots(gosuModule, ijModule);
      }
    }

    processSDK(project);
  }

  private boolean shouldProcessRootChanges(Project project) {
    if (!ENABLED) {
      return false;
    }
    PluginLoaderUtil loader = PluginLoaderUtil.instance( project );
    if (loader.isStarted()) {
      return true;
    } else {
      return loader.getFailureReason() != PluginFailureReason.NONE;
    }
  }

  private void processSDK(@NotNull final Project project) {
    ProjectRootManager rootMgr = ProjectRootManagerEx.getInstance(project);
    Sdk ijSDK = rootMgr.getProjectSdk();
    if (ijSDK == null) {
      stopPlugin(project, GosuBundle.message("error.no.project.sdk"));
    } else if (!PluginLoaderUtil.instance( project ).isStarted()) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          ExceptionUtil.showWarning(GosuBundle.message("info.plugin.not.started"), GosuBundle.message("info.restart.ide"));
        }
      });
    } else {
      Sdk gosuSDK = (Sdk)GosuModuleUtil.getJreModule(project).getNativeSDK();
      if (isSDKChanged(project, gosuSDK, ijSDK) && !DumbServiceImpl.getInstance(project).isDumb()) {
        refreshJreModule( project );
      }
    }
  }

  private boolean isSDKChanged(Project project, Sdk gosuSDK, @NotNull Sdk ijSDK) {
    return gosuSDK != ijSDK || !getJrePaths(project).equals(getSDKPaths(ijSDK));
  }

  private Set<IDirectory> getJrePaths(Project project) {
    final IModule jreModule = GosuModuleUtil.getJreModule(project);
    final Set<IDirectory> paths = Sets.newHashSet(jreModule.getJavaClassPath());
    for (IDirectory dir : jreModule.getSourcePath()) {
      paths.add(dir);
    }
    return paths;
  }

  private Set<String> getSDKPaths(@NotNull Sdk sdk) {
    final Set<String> paths = Sets.newHashSet();
    for (VirtualFile file : sdk.getRootProvider().getFiles(OrderRootType.CLASSES)) {
      paths.add(FileUtil.removeJarSeparator(file.getPath()));
    }
    return paths;
  }

  private void processClasspathChange(@NotNull IModule gosuModule, Module ijModule) {
    List<IDirectory> ijClasspath = TypeSystemStarter.getClassPaths( ijModule );
    List<IDirectory> ijSources = TypeSystemStarter.getSourceFolders( ijModule );
    List<IDirectory> gosuClasspath = gosuModule.getJavaClassPath();
    if (areDifferentIgnoringOrder(ijClasspath, gosuClasspath)) {
      changeClasspath(gosuModule, ijClasspath, ijSources);
    }
  }


  private boolean areDifferentIgnoringOrder(@NotNull List<IDirectory> list1, @NotNull List<IDirectory> list2) {
    if (list1.size() != list2.size()) {
      return true;
    }
    List list1copy = new ArrayList<>(list1);
    list1copy.removeAll(list2);
    return list1copy.size() != 0;
  }

  private void processModuleDependenciesChange(@NotNull Project project) {
    FP64 ijDependencyFingerprint = computeIJDependencyFingerprint(project);
    FP64 gosuDependencyFingerprint = computeGosuDependencyFingerprint(project);
    if (!ijDependencyFingerprint.equals(gosuDependencyFingerprint)) {
      changeDependencies(project);
    }
  }

  @NotNull
  private FP64 computeGosuDependencyFingerprint( Project project ) {
    List<String> strings = new ArrayList<>();
    for (IModule module : GosuModuleUtil.getModules(project)) {
      if (!(module instanceof IJreModule) && !(module instanceof IGlobalModule)) {
        String s = module.getName() + ARROW;
        for (Dependency dependency : module.getDependencies()) {
          final IModule child = dependency.getModule();
          if (!(child instanceof IJreModule)) {
            s += child.getName() + (dependency.isExported() ? EXPORT : NOT_EXPORT);
          }
        }
        strings.add(s);
      }
    }
    return computeOrderIndependentFingerprint(strings);
  }

  @NotNull
  private FP64 computeIJDependencyFingerprint(@NotNull Project project) {
    List<String> strings = new ArrayList<>();
    Module[] ijModules = ModuleManager.getInstance(project).getModules();
    for (Module ijModule : ijModules) {
      String s = ijModule.getName() + ARROW;
      for (Module child : ModuleRootManager.getInstance(ijModule).getDependencies()) {
        s += child.getName() + (TypeSystemStarter.isExported( ijModule, child ) ? EXPORT : NOT_EXPORT);
      }
      strings.add(s);
    }
    return computeOrderIndependentFingerprint(strings);
  }

  @NotNull
  private FP64 computeOrderIndependentFingerprint(@NotNull List<String> strings) {
    Collections.sort(strings);

    final FP64 fp = new FP64();
    for (String s : strings) {
      fp.extend(s);
    }
    return fp;
  }

  // ================================ private part

  private void startPlugin(final Project project, String message) {
    ExceptionUtil.showInfo(GosuBundle.message("info.plugin.enabled"), message);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          PluginLoaderUtil.instance( project ).startPLugin();
        } catch (Throwable t) {
          LOG.error(GosuMessages.create(GosuBundle.message("error.cannot.start.plugin"), t));
        }
      }
    });
  }

  private void stopPlugin(final Project project, final String message) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          ExceptionUtil.showWarning(GosuBundle.message("info.plugin.disabled"), message);
          PluginLoaderUtil.instance( project ).closeProject( PluginFailureReason.NO_SDK);
        } catch (Throwable t) {
          LOG.error(GosuMessages.create(GosuBundle.message("error.cannot.stop.plugin"), t));
        }
      }
    });
  }

  private void refreshJreModule( final Project project ) {
    final PluginLoaderUtil util = PluginLoaderUtil.instance( project );
    if (util.isStarted()) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          try {
            IJreModule jreModule = (IJreModule)TypeSystem.getExecutionEnvironment().getJreModule();
            TypeSystemStarter.updateJreModuleWithProjectSdk( project, jreModule );
            TypeSystem.refresh( jreModule );
          } catch (Throwable t) {
            LOG.error(GosuMessages.create(GosuBundle.message("error.cannot.restart.plugin"), t));
          }
        }
      });
    }
  }

  private void changeDependencies(@NotNull Project project) {
    final Module[] ijModules = ModuleManager.getInstance(project).getModules();
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) );
    for (Module ijModule : ijModules) {
      IModule gosuModule = execEnv.getModule(ijModule.getName());
      final List<Dependency> newDeps = Lists.newArrayList();
      for (Module child : ModuleRootManager.getInstance(ijModule).getDependencies()) {
        IModule gosuChild = execEnv.getModule(child.getName());
        newDeps.add(new Dependency(gosuChild, TypeSystemStarter.isExported( ijModule, child )));
      }
      newDeps.add(new Dependency(execEnv.getJreModule(), true));
      gosuModule.setDependencies(newDeps);
    }
    TypeSystem.refresh( true );
  }

  private void changeSourceRoots(@NotNull IModule gosuModule, Module ijModule) {
    final List<IDirectory> gosuSourcePaths = gosuModule.getSourcePath();
    final List<IDirectory> ijSourcePaths = TypeSystemStarter.getSourceFolders( ijModule );
    final List<IDirectory> ijExcludedPaths = TypeSystemStarter.getExcludedFolders( ijModule );
    if (areDifferentIgnoringOrder(gosuSourcePaths, ijSourcePaths)) {
      gosuModule.setSourcePath(ijSourcePaths);
      gosuModule.setExcludedPaths(ijExcludedPaths);
      TypeSystem.refresh( gosuModule );
    }
  }

  private void changeClasspath(@NotNull IModule gosuModule, List<IDirectory> classpath, List<IDirectory> sources) {
    gosuModule.configurePaths(classpath, sources);
    TypeSystem.refresh( gosuModule );
  }
}
