/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class StringCoercer extends StandardCoercer
{
  private static final StringCoercer INSTANCE = new StringCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeStringFrom( value );
  }

  public static StringCoercer instance()
  {
    return INSTANCE;
  }
}
