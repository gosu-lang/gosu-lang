/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

public class IdentityCoercer extends BaseCoercer
{
  private static final IdentityCoercer INSTANCE = new IdentityCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return value;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return MAX_PRIORITY;
  }

  public static IdentityCoercer instance()
  {
    return INSTANCE;
  }
}
