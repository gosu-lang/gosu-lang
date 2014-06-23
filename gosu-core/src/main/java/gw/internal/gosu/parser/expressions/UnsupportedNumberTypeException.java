/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.reflect.IType;

public class UnsupportedNumberTypeException extends RuntimeException
{

  public UnsupportedNumberTypeException( IType type )
  {
    super( "The type " + type.getName() + " is not supported in mathematical expressions" );
  }

  public UnsupportedNumberTypeException( Class<?> aClass ) {
    super( "The type " + aClass.getName() + " is not supported in mathematical expressions" );
  }
}
