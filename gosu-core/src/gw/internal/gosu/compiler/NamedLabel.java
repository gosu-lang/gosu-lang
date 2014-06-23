/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.ext.org.objectweb.asm.Label;

public class NamedLabel extends Label
{
  private final String _name;

  public NamedLabel( String name )
  {
    _name = name;
  }

  @Override
    public String toString()
  {
    return _name;
  }
}
