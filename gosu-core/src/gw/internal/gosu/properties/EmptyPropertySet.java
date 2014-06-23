/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import java.util.Collections;
import java.util.Set;

/**
 * Property set with no properties
 */
public class EmptyPropertySet implements PropertySet {

  private final String _name;
  
  public EmptyPropertySet(String name) {
    _name = name;
  }

  @Override
  public Set<String> getKeys() {
    return Collections.emptySet();
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getValue(String key) {
    return null;
  }

}
