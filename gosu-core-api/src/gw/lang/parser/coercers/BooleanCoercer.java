/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class BooleanCoercer extends BaseBoxedCoercer
{
  private static final BooleanCoercer INSTANCE = new BooleanCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBooleanFrom( value );
  }

  public static BooleanCoercer instance()
  {
    return INSTANCE;
  }
}
