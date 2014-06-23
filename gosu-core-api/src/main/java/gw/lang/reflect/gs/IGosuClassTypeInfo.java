/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.ITypeInfo;

public interface IGosuClassTypeInfo extends IAttributedFeatureInfo, ITypeInfo, IRelativeTypeInfo
{
  IGosuClass getGosuClass();
}
