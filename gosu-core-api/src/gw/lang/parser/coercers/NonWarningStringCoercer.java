/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

public class NonWarningStringCoercer extends StandardCoercer
{
  private static final NonWarningStringCoercer INSTANCE = new NonWarningStringCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeStringFrom( value );
  }

  public static NonWarningStringCoercer instance()
  {
    return INSTANCE;
  }

  @Override
  public boolean isExplicitCoercion()
  {
    return false;
  }
}