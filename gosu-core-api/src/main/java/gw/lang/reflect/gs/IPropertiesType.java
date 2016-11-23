/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IHasInnerClass;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

public interface IPropertiesType extends IType, IHasInnerClass, IFileRepositoryBasedType
{
  String getPropertiesFileKey( IPropertyInfo pi );
}
