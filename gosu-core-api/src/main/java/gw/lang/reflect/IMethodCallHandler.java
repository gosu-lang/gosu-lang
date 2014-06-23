/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IMethodCallHandler
{
  public Object handleCall( Object ctx, Object... args );
}
