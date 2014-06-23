/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;

public interface IFeatureFilter
{
  public boolean acceptFeature(IType beanType, IFeatureInfo fi);
}
