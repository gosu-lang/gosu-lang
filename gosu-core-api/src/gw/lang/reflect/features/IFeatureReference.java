/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;

public interface IFeatureReference<R, T>
{
  IType getRootType();

  IFeatureInfo getFeatureInfo();

}
