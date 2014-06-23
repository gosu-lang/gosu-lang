/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaParameterDescriptor;
import gw.lang.reflect.IType;
import gw.lang.reflect.IIntrinsicTypeReference;

import java.beans.ParameterDescriptor;

public class ParameterDescriptorJavaParameterDescriptor implements IJavaParameterDescriptor {
  private ParameterDescriptor _pd;

  public ParameterDescriptorJavaParameterDescriptor(ParameterDescriptor pd) {
    _pd = pd;
  }

  @Override
  public String getName() {
    return _pd.getName();
  }

  @Override
  public String getDisplayName() {
    return _pd.getDisplayName();
  }

  @Override
  public String getShortDescription() {
    return _pd.getShortDescription();
  }

  @Override
  public boolean isHidden() {
    return _pd.isHidden();
  }

  @Override
  public IType getFeatureType() {
    if( _pd instanceof IIntrinsicTypeReference)
    {
      return ((IIntrinsicTypeReference)_pd).getFeatureType();
    }
    return null;    
  }
}