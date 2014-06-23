/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 */
public interface ICompilable
{
  boolean isCompilable();
  byte[] compile();
}
