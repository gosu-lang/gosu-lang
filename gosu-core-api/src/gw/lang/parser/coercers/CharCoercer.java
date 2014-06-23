/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class CharCoercer extends BaseBoxedCoercer
{
  private static final CharCoercer INSTANCE = new CharCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value instanceof CharSequence && ((CharSequence)value).length() == 1 )
    {
      return ((CharSequence)value).charAt( 0 );
    }
    else
    {
      Double aDouble = CommonServices.getCoercionManager().makeDoubleFrom( value );
      return (char)aDouble.intValue();
    }
  }

  public static CharCoercer instance()
  {
    return INSTANCE;
  }
}