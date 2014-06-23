/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

/**
 */
public interface IClassDefinitionListener
{
  void classDefined( ICompilableType gsClass, byte[] bytes );
  void auxilaryClassDefined( Class cls, byte[] bytes );
}
