/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

public interface IPropertiesType extends IFileBasedType
{
  String getPropertiesFileKey( IPropertyInfo pi );
}
