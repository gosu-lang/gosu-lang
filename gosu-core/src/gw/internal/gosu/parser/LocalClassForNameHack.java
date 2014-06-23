/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class LocalClassForNameHack
{
  public static IType forName( String name )
  {
    return TypeSystem.getByFullName( name );
  }
}
