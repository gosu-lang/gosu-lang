/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;
import gw.config.CommonServices;

public class TypeVariableCoercer implements ICoercer
{
  private static final TypeVariableCoercer INSTANCE = new TypeVariableCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().convertValue( value, typeToCoerceTo );
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return 2;
  }

  public static TypeVariableCoercer instance()
  {
    return INSTANCE;
  }
}
