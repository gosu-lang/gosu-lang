/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.lang.reflect.ITypeLoader;
import gw.lang.UnstableAPI;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;

import java.util.List;

@UnstableAPI
public interface IModule
{
  public static final String CONFIG_RESOURCE_PREFIX = "config";

  IExecutionEnvironment getExecutionEnvironment();

  IDirectory getOutputPath();

  /**
   * @return A unique name relative to all other modules in a given execution 
   *   environment.
   */
  String getName();

  void setName(String name);

  /**
   * @return A list of dependencies for this module. The list may contain both 
   *   libraries and other modules. The dependency graph must not have cycles. 
   */
  List<Dependency> getDependencies();

  void setDependencies(List<Dependency> newDeps);

  void addDependency( Dependency dependency );

  void removeDependency( Dependency d );

  ITypeLoaderStack getModuleTypeLoader();

  /**
   * @return The path[s] having source files that should be exposed to this 
   *   module.
   */
  List<IDirectory> getSourcePath();

  void setSourcePath( List<IDirectory> path );

  List<IDirectory> getJavaClassPath();
  void setJavaClassPath(List<IDirectory> paths);

  List<IDirectory> getBackingSourcePath();
  void setBackingSourcePath(List<IDirectory> paths);

  List<IDirectory> getExcludedPaths();

  void setExcludedPaths(List<IDirectory> paths);

  /**
   * Configure both source and Java classpaths of the module in a semi-automated way. First parameter
   * is Java classpath. Second parameter is extended with all paths from Java classpath that are marked
   * to have Gosu "sources" (through MANIFEST.MF with Contains-Sources header) and used as Gosu source path.
   *
   * @param classpath path to types not directly in the module's sources e.g., jar files containing .class files and other types/resources
   * @param sourcePaths path to the sources directly defined in this module; sources in this path are statically compiled as part of this module's build target e.g., ./src directories
   * @param backingSourcePaths path to sources corresponding with the classpath parameter (intended for IDE usa)
   */
  void configurePaths( List<IDirectory> classpath, List<IDirectory> sourcePaths, List<IDirectory> backingSourcePaths );

  /**
   * @return The module/project from the execution environment that corresponds
   *   with this logical module. For example, in Eclipse the native module is of 
   *   type IJavaProject.
   */
  Object getNativeModule();

  void setNativeModule( INativeModule nativeModule );

  /**
   * Returns typeloaders of the given class that are local to this module as well as such
   * typeloaders from dependent modules.
   *
   * @param typeLoaderClass
   * @param <T>
   * @return
   */
  <T extends ITypeLoader> List<? extends T> getTypeLoaders(Class<T> typeLoaderClass);

  IModule[] getModuleTraversalList();

  IFileSystemGosuClassRepository getFileRepository();

  /**
   * Get class loader, associated with this module.
   * @return
   */
  ClassLoader getModuleClassLoader();

  void disposeLoader();
}

