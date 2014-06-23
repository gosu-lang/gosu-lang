/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.Keyword;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.List;

public class LengthProperty extends JavaPropertyInfo
{
  private IPropertyAccessor _accessor;


  LengthProperty(ITypeInfo typeInfo) throws IntrospectionException
  {
    super( typeInfo,
           new PropertyDescriptorJavaPropertyDescriptor(new PropertyDescriptor( Keyword.KW_length.toString(), null, null )
           {
             @Override
             public Class getPropertyType()
             {
               return Integer.TYPE;
             }
           } , TypeSystem.getCurrentModule()));
    _accessor = LengthAccessor.INSTANCE;

  }

  @Override
  public IPropertyAccessor getAccessor()
  {
    return _accessor;
  }

  @Override
  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  @Override
  public boolean isPublic()
  {
    return true;
  }

  @Override
  public boolean isReadable()
  {
    return true;
  }

  @Override
  public List<IAnnotationInfo> getAnnotations()
  {
    return Collections.emptyList();
  }
}
