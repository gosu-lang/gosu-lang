/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.ITypeLoader;
import manifold.api.host.RefreshRequest;
import manifold.api.host.RefreshKind;
import gw.lang.reflect.module.IModule;

import java.net.URL;
import java.util.Set;
import manifold.api.fs.IDirectory;
import manifold.api.sourceprod.TypeName;

public interface IGosuClassRepository
{
  /**
   * The module having this repository
   */
  IModule getModule();

  /**
   * Finds the ISourceFileHandle for a given fully-qualified class name, or null if no such source file exists
   *
   *
   * @param strQualifiedClassName the fully-qualified name of the class
   * @param extensions
   * @return The source file handle for the given class, or null if no such
   *   source file exists.
   */
  public ISourceFileHandle findClass(String strQualifiedClassName, String[] extensions);

  /**
   * Finds the given resource in this repository.
   *
   * <p> The name of a resource is a '<tt>/</tt>'-separated path name that
   * identifies the resource.
   *
   * @param name the name of the resource
   * @return the URL of the resource or null if the resource cannot be found.
   */
  URL findResource(String name);

  /**
   * @return A set containing all type names in this repository (includes enhancement names)
   */
  public Set<String> getAllTypeNames();

  /**
   * Returns the names of all types in this repository that end with one of the specified file extensions
   *
   * @return A set containing all the type names in this repository.
   * @param extensions the set of file name extensions to consider
   */
  public Set<String> getAllTypeNames(String... extensions);

  /**
   * Returns all type names in the given namespace and with the given extensions.
   */
  Set<TypeName> getTypeNames( String namespace, Set<String> extensions, ITypeLoader loader);

  /**
   * Returns the number of namespaces this repository has matching the given name.
   * Note a namespace can span multiple source roots, hence the integer return value -- one per source root.
   */
  int hasNamespace(String namespace);

  /**
   * Called when a type is refreshed
   */
  void typesRefreshed(RefreshRequest request);

  /**
   * Called when a namespace is refreshed
   */
  void namespaceRefreshed( String namespace, IDirectory dir, RefreshKind kind);
}
