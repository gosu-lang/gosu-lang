/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class DoubleCoercer extends BaseBoxedCoercer
{
  private static final DoubleCoercer INSTANCE = new DoubleCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeDoubleFrom( value );
  }

  public static DoubleCoercer instance()
  {
    return INSTANCE;
  }
}