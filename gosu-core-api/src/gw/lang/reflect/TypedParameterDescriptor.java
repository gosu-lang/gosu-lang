/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.beans.ParameterDescriptor;

public class TypedParameterDescriptor extends ParameterDescriptor implements IIntrinsicTypeReference
{
  private final Class _paramType;

  public TypedParameterDescriptor( String parameterName, Class paramType )
  {
    setName( parameterName );
    _paramType = paramType;
  }

  //----------------------------------------------------------------------------
  // -- ITypedFeatureInfo implementation --

  public IType getFeatureType()
  {
    return TypeSystem.get( _paramType );
  }

}
