/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;

public interface ICoercer
{
  int MAX_PRIORITY = 16;

  public Object coerceValue( IType typeToCoerceTo, Object value );

  boolean isExplicitCoercion();

  /**
   * @return true if this coercer knows how to handle the null value.
   */
  boolean handlesNull();

  /**
   * @return a value between 0 and MAX_PRIORITY, inclusive that indicates
   * the priority of this coercer when resolving overloaded methods.  Typically
   * a coercer should return 0, but coercers that have a high affinity between the
   * target and coerced type, such as primitives, can have higher priorities.
   * @param to
   * @param from
   */
  int getPriority( IType to, IType from );
}
