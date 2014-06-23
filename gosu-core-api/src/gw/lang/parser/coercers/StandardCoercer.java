/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

public abstract class StandardCoercer extends BaseCoercer
{
  public boolean isExplicitCoercion()
  {
    return true;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 1; // lowest priority
  }
}