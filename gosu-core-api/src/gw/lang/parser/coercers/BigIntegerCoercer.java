/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class BigIntegerCoercer extends StandardCoercer
{
  private static final BigIntegerCoercer INSTANCE = new BigIntegerCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBigIntegerFrom( value );
  }

  public static BigIntegerCoercer instance()
  {
    return INSTANCE;
  }
}