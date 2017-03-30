/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

public class MethodReferenceCoercer extends BaseCoercer
{
  private static final MethodReferenceCoercer INSTANCE = new MethodReferenceCoercer();

  private MethodReferenceCoercer()
  {
  }

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    throw new IllegalStateException( "MethodReference coercion is done at compile-time" );
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 1;
  }

  public static MethodReferenceCoercer instance()
  {
    return INSTANCE;
  }
}
