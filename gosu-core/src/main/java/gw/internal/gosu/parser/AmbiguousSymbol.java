/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.reflect.java.JavaTypes;

public class AmbiguousSymbol extends Symbol
{
  public AmbiguousSymbol( String name )
  {
    super( name, JavaTypes.OBJECT(), null, null );
  }
}
