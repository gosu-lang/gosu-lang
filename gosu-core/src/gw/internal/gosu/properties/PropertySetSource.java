/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IFile;

import java.util.Set;

/**
 * Represents the different sources of {@link PropertySet} objects that can be used to build
 * {@link PropertiesType} objects.
 */
public interface PropertySetSource {

  /**
   * The names of all the property sets available from this source
   * @return a non null set of property set names
   */
  public Set<String> getPropertySetNames();

  /**
   * Return the named property set
   * @param name the name of the property set
   * @return a property set, or null if the name is not in the set returned by {@link #getPropertySetNames()}
   */
  public PropertySet getPropertySet(String name);

  /**
   * Return the property set for the given file
   *
   * @param file the name of the file
   * @return a property set, or null if the name is not in the set returned by {@link #getPropertySetNames()}
   */
  PropertySet getPropertySetForFile(IFile file);

  /**
   * Returns the file for the given name.
   */
  IFile getFile(String name);
}
