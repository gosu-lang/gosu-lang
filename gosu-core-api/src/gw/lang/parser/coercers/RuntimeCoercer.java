/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class RuntimeCoercer extends BaseCoercer
{
  private static final RuntimeCoercer INSTANCE = new RuntimeCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().convertValue( value, typeToCoerceTo );
  }

  public boolean handlesNull()
  {
    // Must handle null -> primitive runtime coercion
    return true;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 1;
  }

  public static RuntimeCoercer instance()
  {
    return INSTANCE;
  }
}
