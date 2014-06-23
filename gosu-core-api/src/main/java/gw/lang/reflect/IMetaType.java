/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IMetaType extends INonLoadableType
{
  IType getType();

  boolean isLiteral();
}
