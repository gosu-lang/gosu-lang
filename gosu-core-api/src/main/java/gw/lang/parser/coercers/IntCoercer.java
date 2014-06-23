/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class IntCoercer extends BaseBoxedCoercer
{
  private static final IntCoercer INSTANCE = new IntCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeIntegerFrom( value );
  }

  public static IntCoercer instance()
  {
    return INSTANCE;
  }
}