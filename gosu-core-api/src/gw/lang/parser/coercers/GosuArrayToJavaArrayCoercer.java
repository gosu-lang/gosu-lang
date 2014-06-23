/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuArrayClassInstance;

public class GosuArrayToJavaArrayCoercer extends BaseCoercer
{
  private static final GosuArrayToJavaArrayCoercer INSTANCE = new GosuArrayToJavaArrayCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    IGosuArrayClassInstance arr = (IGosuArrayClassInstance)value;
    return arr.getObjectArray();
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
    return 0;
  }

  public static GosuArrayToJavaArrayCoercer instance()
  {
    return INSTANCE;
  }
}
