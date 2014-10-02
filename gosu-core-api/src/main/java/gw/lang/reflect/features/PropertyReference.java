/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertyReference<R, T> extends FeatureReference<R, T> implements IPropertyReference<R, T>
{
  private IPropertyInfo _pi;
  private IType _rootType;

  public PropertyReference( IType rootType, String property )
  {
    _rootType = rootType;
    _pi = getPropertyInfo( rootType, property );
  }

  public PropertyReference( IType rootType, IPropertyInfo property )
  {
    _rootType = rootType;
    _pi = property;
  }

  static IPropertyInfo getPropertyInfo( IType rootType, String propName )
  {
    ITypeInfo typeInfo = rootType.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      return ((IRelativeTypeInfo)typeInfo).getProperty( rootType, propName );
    }
    else
    {
      return typeInfo.getProperty( propName );
    }
  }

  public T get( R ctx )
  {
    return (T) _pi.getAccessor().getValue( ctx );
  }

  public void set( R ctx, T val )
  {
    _pi.getAccessor().setValue( (R) ctx, (T) val );
  }

  public IType getRootType()
  {
    return _rootType;
  }

  public IPropertyInfo getPropertyInfo() {
    return _pi;
  }

  @Override
  protected Object evaluate(Iterator args) {
    Object ctx = null;
    if (!_pi.isStatic()) {
      ctx = args.next();
    }
    return _pi.getAccessor().getValue(ctx);
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getPropertyInfo();
  }

  @Override
  public List<IType> getFullArgTypes() {
    ArrayList<IType> lst = new ArrayList<IType>();
    if (!_pi.isStatic()) {
      lst.add(_pi.getOwnersType());
    }
    return lst;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    PropertyReference that = (PropertyReference)o;

    if( _pi != null ? !_pi.equals( that._pi ) : that._pi != null )
    {
      return false;
    }
    if( _rootType != null ? !_rootType.equals( that._rootType ) : that._rootType != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _pi != null ? _pi.hashCode() : 0;
    result = 31 * result + (_rootType != null ? _rootType.hashCode() : 0);
    return result;
  }
}
