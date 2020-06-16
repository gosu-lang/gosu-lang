/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.PropertyInfoBase;
import gw.lang.reflect.ReflectUtil;

import manifold.rt.api.Bindings;
import java.util.Collections;
import java.util.List;

/**
 */
public class DynamicPropertyInfo extends PropertyInfoBase implements IPropertyAccessor
{
  private String _strName;


  DynamicPropertyInfo( ITypeInfo container, String strName )
  {
    super( container );
    _strName = strName;
  }

  @Override
  public boolean isReadable()
  {
    return true;
  }

  @Override
  public boolean isWritable( IType whosAskin )
  {
    return true;
  }

  @Override
  public IPropertyAccessor getAccessor()
  {
    return this;
  }

  @Override
  public IType getFeatureType()
  {
    return getOwnersType();
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  @Override
  public boolean hasAnnotation( IType type )
  {
    return false;
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  @Override
  public Object getValue( Object ctx )
  {
    if( ctx instanceof Bindings ) {
      return ((Bindings)ctx).get( getName() );
    }
    return ReflectUtil.getProperty( ctx, getName() );
  }

  @Override
  public void setValue( Object ctx, Object value )
  {
    if( ctx instanceof Bindings ) {
      ((Bindings)ctx).put( getName(), value );
    }
    else {
      ReflectUtil.setProperty( ctx, getName(), value );
    }
  }
}
