/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class DateCoercer extends StandardCoercer
{
  private static final DateCoercer INSTANCE = new DateCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeDateFrom( value );
  }

  public static DateCoercer instance()
  {
    return INSTANCE;
  }
}