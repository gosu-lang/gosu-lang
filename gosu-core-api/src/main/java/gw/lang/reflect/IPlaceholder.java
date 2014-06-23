/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IPlaceholder
{
  Object UNHANDLED = new Object() { public String toString() {return "unhandled";} };

  boolean isPlaceholder();
}
