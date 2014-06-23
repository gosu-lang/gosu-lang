/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.internal.gosu.parser.IGosuAnnotation;

import java.util.List;

public interface IModifierInfo
{
  int getModifiers();

  /**
   * This method exists for historical reasons only, and will be removed in future
   * releases.
   */
  @Deprecated
  void syncAnnotations( IModifierInfo modifierInfo );
  
  List<IGosuAnnotation> getAnnotations();
}
