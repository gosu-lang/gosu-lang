/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.DependencyScope;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.VirtualFile;
import gw.config.CommonServices;
import gw.config.IExtensionFolderLocator;
import gw.config.IPlatformHelper;
import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.IGosuParser;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.filesystem.IDEAFileSystem;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.Iterables.filter;

public class TypeSystemStarter {
  private static final Logger LOG = Logger.getInstance(TypeSystemStarter.class);
  public static final String JAR_INDICATOR = ".jar!";

  private static final Map<Project, TypeSystemStarter> INSTANCES = new WeakHashMap<>();

  public enum StartupStatus {NOT_STARTED, STARTING, STARTED, STOPPING}

  private final AtomicReference<StartupStatus> _status = new AtomicReference<>( StartupStatus.NOT_STARTED);
  private final AtomicInteger _concurrentRefresh = new AtomicInteger(0);

  private final Project _project;
  @Nullable
  Module[] _allIJModules;

  public static TypeSystemStarter instance(Project project) {
    TypeSystemStarter typeSystemStarter = INSTANCES.get(project);
    if (typeSystemStarter == null) {
      INSTANCES.put(project, typeSystemStarter = new TypeSystemStarter(project));
    }
    return typeSystemStarter;
  }

  public static TypeSystemStarter instance(@NotNull IType type) {
    return instance(projectFrom(type));
  }

  public static TypeSystemStarter instance(@NotNull IModule module) {
    return instance(projectFrom(module));
  }

  public static TypeSystemStarter instance(@NotNull IExecutionEnvironment execEnv) {
    return instance(projectFrom(execEnv));
  }

  @NotNull
  public static Project projectFrom(@NotNull IType type) {
    return (Project) type.getTypeLoader().getModule().getExecutionEnvironment().getProject().getNativeProject();
  }

  @NotNull
  public static Project projectFrom(@NotNull IModule module) {
    return (Project) module.getExecutionEnvironment().getProject().getNativeProject();
  }

  @NotNull
  public static Project projectFrom(@NotNull IExecutionEnvironment execEnv) {
    return (Project) execEnv.getProject().getNativeProject();
  }

  public TypeSystemStarter(Project project) {
    _project = project;
  }

  public void start(@NotNull Project project) {
    if (_status.compareAndSet( StartupStatus.NOT_STARTED, StartupStatus.STARTING)) {
      try {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        _allIJModules = moduleManager.getModules();
        initializeGosu(project);
        _status.set( StartupStatus.STARTED);
      } catch (RuntimeException e) {
        _status.set( StartupStatus.NOT_STARTED);
        throw e;
      } catch (Error e) {
        _status.set( StartupStatus.NOT_STARTED);
        throw e;
      }
    } else {
      // XXX type system already started -- concurrent start?! ack!
      throw new IllegalStateException("ACK! Attempted concurrent start of type system.");
    }
  }

  /**
   * Caller must verify type system is in started status after this call returns to safely continue.
   */
  public void startRefresh() {
    _concurrentRefresh.incrementAndGet();
  }

  public void stopRefresh() {
    if (_concurrentRefresh.decrementAndGet() == 0) {
      synchronized (_concurrentRefresh) {
        _concurrentRefresh.notifyAll();
      }
    }
  }

  void initializeGosu(@NotNull Project project) {
    String circularDependency = GosuModuleUtil.getCircularModuleDependency(project);
    if (circularDependency != null) {
      throw new GosuPluginException("Gosu does not support circular dependencies.\n" + circularDependency, PluginFailureReason.CIRCULAR_DEPENDENCY);
    }

    // make sure IJavaType is initialized, because if another thread tries
    // to initialize it without the typesystem lock, we'll deadlock
    //noinspection UnusedDeclaration
    Class c = IJavaType.class;
    //noinspection UnusedAssignment
    c = IGosuParser.class;

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) );
    GosuInitialization.instance( execEnv ).uninitializeRuntime();
    try {
      CommonServices.getKernel().redefineService_Privileged(IFileSystem.class, new IDEAFileSystem());
    } catch (Exception e) {
      LOG.error(e);
    }
    CommonServices.getKernel().redefineService_Privileged(IExtensionFolderLocator.class, new IDEAExtensionFolderLocator());
    CommonServices.getKernel().redefineService_Privileged(IPlatformHelper.class, new IDEAPlatformHelper(project));
    List<IModule> modules = defineModules(project);

    GosuInitialization.instance( execEnv ).initializeMultipleModules(modules);

    IModule module = execEnv.getModule( IExecutionEnvironment.GLOBAL_MODULE_NAME);
    TypeSystem.pushModule( module );
    try {
      Object o1 = IGosuParser.NaN;
      Object o2 = JavaTypes.DOUBLE();
    } finally {
      TypeSystem.popModule( module );
    }
  }

  @NotNull
  private List<IModule> defineModules(@NotNull Project project) {
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) );
    execEnv.createJreModule( );

    final List<IDirectory> allSourcePaths = Lists.newArrayList();
    final Map<Module, IModule> modules = Maps.newHashMap();
    final List<IModule> allModules = Lists.newArrayList();
    for (Module ijModule : _allIJModules) {
      final IModule module = defineModule(ijModule);
      if (module != null) {
        allSourcePaths.addAll(module.getSourcePath());
        modules.put(ijModule, module);
        allModules.add(module);
      }
    }

    for (Module ijModule : _allIJModules) {
      final IModule module = modules.get(ijModule);
      for (Module child : ModuleRootManager.getInstance(ijModule).getDependencies()) {
        IModule moduleDep = modules.get(child);
        if (moduleDep != null) {
          module.addDependency(new Dependency(moduleDep, isExported(ijModule, child)));
        }
      }
    }

    addImplicitJreModuleDependency(project, allModules);
    allSourcePaths.addAll(execEnv.getJreModule().getSourcePath());

    IModule _globalModule = GosuShop.createGlobalModule( execEnv );
    _globalModule.configurePaths(Collections.<IDirectory>emptyList(), allSourcePaths);
    List<IModule> rootModules = findRootModules(allModules);
    for (IModule rootModule : rootModules) {
      _globalModule.addDependency(new Dependency(rootModule, true));
    }
//    _globalModule.addDependency(new Dependency(execEnv.getJreModule(), true));

    allModules.add(_globalModule);
    return allModules;
  }

  public List<IModule> findRootModules(List<IModule> modules) {
    List<IModule> moduleRoots = new ArrayList<>(modules);
    for (IModule module : modules) {
      for (Dependency d : module.getDependencies()) {
        moduleRoots.remove(d.getModule());
      }
    }
    return moduleRoots;
  }

  public static boolean isExported(@NotNull Module ijModule, Module child) {
    for (OrderEntry entry : ModuleRootManager.getInstance(ijModule).getOrderEntries()) {
      if (entry instanceof ModuleOrderEntry) {
        final ModuleOrderEntry moduleEntry = (ModuleOrderEntry) entry;
        final DependencyScope scope = moduleEntry.getScope();
        if (!scope.isForProductionCompile() && !scope.isForProductionRuntime()) {
          continue;
        }
        final Module module = moduleEntry.getModule();
        if (module != null && module == child) {
          return moduleEntry.isExported();
        }
      }
    }
    return false;
  }

  private void addImplicitJreModuleDependency(Project project, @NotNull List<IModule> modules) {
    IJreModule jreModule = GosuModuleUtil.getJreModule(project);
    updateJreModuleWithProjectSdk(project, jreModule);
    for (IModule module : modules) {
      module.addDependency(new Dependency(jreModule, true));
    }
    modules.add(jreModule);
  }

  public static void updateJreModuleWithProjectSdk(Project project, @NotNull IJreModule jreModule) {
    //note: A module can declare its own SDK, separate from the project's SDK.  If we ever handle
    //       this case we'll create a separate ExecutionEnvironment rooted at the module.  The idea
    //       is that a JRE represents a runtime environment and the SDK essentially defines the JRE.
    final ProjectRootManager rootManager = ProjectRootManagerEx.getInstance(project);
    Sdk projectSdk = rootManager.getProjectSdk();

    if (projectSdk != null) {
      final VirtualFile[] classFiles = projectSdk.getRootProvider().getFiles(OrderRootType.CLASSES);
      if (classFiles.length > 0) {
        jreModule.configurePaths(toDirectories(classFiles), Collections.<IDirectory>emptyList());
      } else {
        throw new GosuPluginException("Project SDK does not have any files at this location:\n"
            + projectSdk.getHomePath() +
            "\nPlease, fix your current SDK or switch to another one.", PluginFailureReason.INVALID_SDK);
      }
      jreModule.setNativeSDK(projectSdk);
    } else {
      throw new GosuPluginException("Project SDK not defined.", PluginFailureReason.NO_SDK);
    }
  }

  private static List<IDirectory> toDirectories(@NotNull VirtualFile[] files) {

    final List<IDirectory> result = Lists.newArrayList();
    for (VirtualFile f : files) {
      File file = new File(stripExtraCharacters(f.getPath()));
      IDirectory dir = CommonServices.getFileSystem().getIDirectory(file);
      result.add(dir);
    }
    return result;
  }

  @NotNull
  public List<Module> getAllRequiredModules(@NotNull Module p) {
    Set<Module> visitedProjects = new HashSet<>();
    List<Module> projects = new ArrayList<>();
    getAllRequiredProjects(p, projects, visitedProjects);
    return projects;
  }

  private void getAllRequiredProjects(@NotNull Module ijModule, @NotNull List<Module> ijModuleList, @NotNull Set<Module> visitedModules) {
    visitedModules.add(ijModule);

    for (Module otherModule : ModuleRootManager.getInstance(ijModule).getDependencies()) {
      if (!visitedModules.contains(otherModule)) {
        ijModuleList.add(otherModule);
        getAllRequiredProjects(otherModule, ijModuleList, visitedModules);
      }
    }
  }

  public IModule defineModule(@NotNull Module ijModule) {
    IModule gosuModule = GosuShop.createModule( TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( ijModule.getProject() ) ),
                                                ijModule.getName() );

//    File moduleLocation = new File(ijModule.getProject().getLocation());
//    IDirectory eclipseModuleRoot = CommonServices.getFileSystem().getIDirectory(moduleLocation);
//    gosuModule.addRoot(eclipseModuleRoot);
    List<VirtualFile> sourceFolders = getSourceRoots(ijModule);
    gosuModule.configurePaths(getClassPaths(ijModule), Lists.transform(sourceFolders, ToDirectory.INSTANCE));
    gosuModule.setExcludedPaths(getExcludedFolders(ijModule));

    //Fix this
//    ModuleRootManager rootManager = ModuleRootManager.getInstance(ijModule);
//    File outputPath = rootManager.getOutputLocation().removeFirstSegments(1).toFile();
//    outputPath = new File(moduleLocation, outputPath.getPath());
//    gosuModule.setOutputPath(CommonServices.getFileSystem().getIDirectory(outputPath));

    gosuModule.setNativeModule(new IJNativeModule(ijModule));
    return gosuModule;
  }

  public static List<IDirectory> getSourceFolders(@NotNull Module ijModule) {
    return Lists.transform(getSourceRoots(ijModule), ToDirectory.INSTANCE);
  }

  public static List<IDirectory> getExcludedFolders(@NotNull Module ijModule) {
    return Lists.transform(getExcludedRoots(ijModule), ToDirectory.INSTANCE);
  }

  public static List<VirtualFile> getSourceRoots(@NotNull Module ijModule) {
    final ModuleRootManager moduleManager = ModuleRootManager.getInstance(ijModule);
    final List<VirtualFile> sourcePaths = Lists.newArrayList();
    List<VirtualFile> excludeRoots = Arrays.asList(moduleManager.getExcludeRoots());
    for (VirtualFile sourceRoot : moduleManager.getSourceRoots()) {
      if (!excludeRoots.contains(sourceRoot)) {
        sourcePaths.add(sourceRoot);
      }
    }

    return sourcePaths;
  }

  public static List<VirtualFile> getExcludedRoots(@NotNull Module ijModule) {
    final ModuleRootManager moduleManager = ModuleRootManager.getInstance(ijModule);
    return Arrays.asList(moduleManager.getExcludeRoots());
  }

  private enum ToDirectory implements Function<VirtualFile, IDirectory> {
    INSTANCE;

    @Override
    public IDirectory apply(VirtualFile file) {
      String sourcePath = file.getPath();
      if (sourcePath.contains(JAR_INDICATOR)) {
        sourcePath = sourcePath.substring(0, sourcePath.length() - 2);
      }
      return CommonServices.getFileSystem().getIDirectory(new File(sourcePath));
    }
  }

  public void stop(@NotNull Project project) {
    if (_status.compareAndSet( StartupStatus.STARTED, StartupStatus.STOPPING)) {
      try {
        // wait for concurrent refreshes to complete...
        synchronized (_concurrentRefresh) {
          try {
            if (_concurrentRefresh.get() > 0) {
              _concurrentRefresh.wait(10000);
            }
          } catch (InterruptedException ex) {
            // Timeout for safety.  Not sure if this could happen... seems doubtful.
          }
        }

        GosuInitialization.instance( TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ) ).uninitializeMultipleModules();
        _allIJModules = null;
      } finally {
        // XXX could fail to shutdown, so maybe put into failed state in such cases?
        _status.set( StartupStatus.NOT_STARTED);
      }
    } else {
      // XXX type system not started yet, cannot stop.  Implement interrupt capability?
      throw new IllegalStateException("ACK! Attempted to stop type system while it was still starting... Do we have to support this?");
    }
  }

  public boolean isStarted() {
    return _status.get() == StartupStatus.STARTED;
  }

  public static List<IDirectory> getClassPaths(@NotNull Module ijModule) {
    List<String> paths = getDirectClassPaths(ijModule);
    for (Iterator<String> it = paths.iterator(); it.hasNext();) {
      String url = it.next();
      if (dependencyChainContains(ijModule, url, new ArrayList<Module>())) {
        it.remove();
      }
    }
    List<IDirectory> dirs = new ArrayList<>();
    for (String path : paths) {
      dirs.add( CommonServices.getFileSystem().getIDirectory(new File(path)));
    }
    return dirs;
  }

  private static enum GosuCoreJar implements Predicate<File> {
    INSTANCE;

    @Override
    public boolean apply(@Nullable File file) {
      // FIXME: Stupid way to detect if JAR is Gosu JAR.
      return file.getName().startsWith("gosu-core-");
    }
  }

  private static List<String> getDirectClassPaths(Module ijModule) {
    final ModuleRootManager rootManager = ModuleRootManager.getInstance(ijModule);
    Sdk sdk = rootManager.getSdk();
    boolean gosuSdk = false; //sdk != null && sdk.getSdkType() == GosuSdkType.getInstance();
    final List<OrderEntry> orderEntries = Arrays.asList(rootManager.getOrderEntries());
    Predicate<File> ignoredLibs = gosuSdk ? GosuCoreJar.INSTANCE : Predicates.<File>alwaysFalse();

    List<String> paths = new ArrayList<>();
    for (LibraryOrderEntry entry : filter(orderEntries, LibraryOrderEntry.class)) {
      final Library lib = entry.getLibrary();
      if (lib != null) {
        for (VirtualFile virtualFile : lib.getFiles(OrderRootType.CLASSES)) {
          final File file = new File(stripExtraCharacters(virtualFile.getPath()));
          if (file.exists() && !ignoredLibs.apply(file)) {
            paths.add(file.getAbsolutePath());
          }
        }
      }
    }
    return paths;
  }

  private static boolean dependencyChainContains(Module ijModule, String path, List<Module> visited) {
    if (!visited.contains(ijModule)) {
      visited.add(ijModule);

      ModuleRootManager rootManager = ModuleRootManager.getInstance(ijModule);
      for (Module dep : rootManager.getDependencies()) {
        if (getDirectClassPaths(dep).contains(path) || dependencyChainContains(dep, path, visited)) {
          return true;
        }
      }
    }
    return false;
  }

  @NotNull
  private static String stripExtraCharacters(@NotNull String fileName) {
    //TODO-dp this is not robust enough (eliminated !/ at the end of the jar)
    if (fileName.endsWith("!/")) {
      fileName = fileName.substring(0, fileName.length() - 2);
    }
    return fileName;
  }
}
