/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.util.Rational;

public class RationalCoercer extends StandardCoercer
{
  private static final RationalCoercer INSTANCE = new RationalCoercer();

  public Rational coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeRationalFrom( value );
  }

  public static RationalCoercer instance()
  {
    return INSTANCE;
  }
}