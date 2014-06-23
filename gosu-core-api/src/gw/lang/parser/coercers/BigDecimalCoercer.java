/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class BigDecimalCoercer extends StandardCoercer
{
  private static final BigDecimalCoercer INSTANCE = new BigDecimalCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBigDecimalFrom( value );
  }

  public static BigDecimalCoercer instance()
  {
    return INSTANCE;
  }
}