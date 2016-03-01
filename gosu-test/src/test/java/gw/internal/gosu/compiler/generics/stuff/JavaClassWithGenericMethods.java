/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.generics.stuff;

public class JavaClassWithGenericMethods
{
  public <T> T identity( T t )
  {
    return t;
  }

  public static <T> T staticIdentity( T t )
  {
    return t;
  }

  public <T> T identityToOverride( T t )
  {
    return t;
  }

  public static <T> T staticIdentityToOverride( T t )
  {
    return t;
  }
}
