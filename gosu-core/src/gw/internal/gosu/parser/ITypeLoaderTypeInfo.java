/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.BaseJavaTypeInfo;
import gw.lang.reflect.MethodList;

import java.util.Collections;
import java.util.List;

/**
 */
public class ITypeLoaderTypeInfo extends BaseJavaTypeInfo
{
  public ITypeLoaderTypeInfo()
  {
    super( ITypeLoader.class );
  }

  public List<? extends IPropertyInfo>/*<IPropertyInfo>*/ getProperties()
  {
    return Collections.EMPTY_LIST;
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    return null;
  }

  public MethodList/*<IMethodInfo>*/ getMethods()
  {
    return MethodList.EMPTY;
  }

  public List/*<IConstructorInfo>*/ getConstructors()
  {
    return Collections.EMPTY_LIST;
  }

  public List/*<IEventInfo>*/ getEvents()
  {
    return Collections.EMPTY_LIST;
  }

  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }
}
