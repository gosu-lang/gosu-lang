/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IHasInnerClass;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IPropertyInfo;

public interface IPropertiesType extends IFileBasedType, IHasInnerClass
{
  String getPropertiesFileKey( IPropertyInfo pi );
}
