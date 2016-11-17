/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

public abstract class StandardCoercer extends BaseCoercer
{
  public boolean isExplicitCoercion()
  {
    return true;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 1; // lowest priority
  }

  protected boolean isCoercingDimensionWithSameType( IType to, IType from )
  {
    if( JavaTypes.IDIMENSION().isAssignableFrom( from ) )
    {
      IType numberType = TypeSystem.findParameterizedType( from, JavaTypes.IDIMENSION() ).getTypeParameters()[1];
      if( to == numberType )
      {
        // must be better than Boxed coercion (which is 2 right now)
        return true;
      }
    }
    return false;
  }
}