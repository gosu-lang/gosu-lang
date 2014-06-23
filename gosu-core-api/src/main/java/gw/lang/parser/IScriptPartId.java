/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

public abstract class IScriptPartId
{
  /**
   * @return The type that contains this part.
   */
  public abstract IType getContainingType();

  /**
   * @return The name of the type that contains this part.
   */
  public abstract String getContainingTypeName();

  /**
   * @return An id that distinguishes this part from other parts
   *         in the containing type.
   */
  public abstract String getId();

  public abstract String toString();

  public abstract void setRuntimeType( IGosuClass runtimeType );
  public abstract IGosuClass getRuntimeType();
}
