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
    return 2;
  }
}
