/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

public abstract class BaseBoxedCoercer extends StandardCoercer
{
  @Override
  public int getPriority( IType to, IType from )
  {
    if( isCoercingDimensionWithSameType( to, from ) )
    {
      // must be higher priority than blind boxed coercion
      return 3;
    }
    return 2;
  }
}
