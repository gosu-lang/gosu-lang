/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import java.util.Set;

/**
 * Basic interface that describes a set of properties - could be a Properties object or a
 * resource bundle or any other set of key/value string pairs. The PropertiesTypeLoader builds types
 * on this basic interface
 */
public interface PropertySet {

  /**
   * The name of the property set, which will be used as the name of the type
   * @return a non null name
   */
  public String getName();

  /**
   * The set of keys which can be used to look up values in this property set
   * @return a non null set containing the keys
   */
  public Set<String> getKeys();

  /**
   * Get the value corresponding to the given key
   * @param key the key, never null
   * @return the value corresponding to the given key or null if there is no corresponding value
   */
  public String getValue(String key);

}
