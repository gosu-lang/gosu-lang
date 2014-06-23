/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

public class ReferenceWithName<T> extends Reference<T> {

  private final String _name;

  public ReferenceWithName( T value, String name ) {
    super( value );
    _name = name;
  }

  @Override
  public String toString() {
    return _name;
  }

}
