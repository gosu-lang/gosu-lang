/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.ITypeInfo;

import java.lang.annotation.ElementType;

public enum UsageTarget
{
  AllTarget,
  TypeTarget,
  ConstructorTarget,
  PropertyTarget,
  MethodTarget,
  ParameterTarget;

  public static UsageTarget convert( ElementType elementType )
  {
    switch( elementType )
    {
      case CONSTRUCTOR:
        return ConstructorTarget;
      case FIELD:
        return PropertyTarget;
      case TYPE:
        return TypeTarget;
      case METHOD:
        return UsageTarget.MethodTarget;
      case PARAMETER:
        return UsageTarget.ParameterTarget;
    }
    return AllTarget;
  }

  public static UsageTarget getForFeature( IFeatureInfo fi )
  {
    if( fi instanceof IConstructorInfo )
    {
      return ConstructorTarget;
    }
    else if( fi instanceof IPropertyInfo )
    {
      return PropertyTarget;
    }
    else if( fi instanceof IMethodInfo )
    {
      return MethodTarget;
    }
    else if( fi instanceof IParameterInfo )
    {
      return ParameterTarget;
    }
    else if( fi instanceof ITypeInfo )
    {
      return TypeTarget;
    }
    else
    {
      return AllTarget;
    }
  }
}
