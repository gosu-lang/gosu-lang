/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class StringBuilderCoercer extends StandardCoercer
{
  private static final StringBuilderCoercer INSTANCE = new StringBuilderCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return new StringBuilder( CommonServices.getCoercionManager().makeStringFrom( value ) );
  }

  public static StringBuilderCoercer instance()
  {
    return INSTANCE;
  }
}