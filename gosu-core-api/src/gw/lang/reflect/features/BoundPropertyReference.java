/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoundPropertyReference<R, T> extends FeatureReference<R, T> implements IPropertyReference<R, T> {
  private IPropertyInfo _pi;
  private IType _rootType;
  private Object _ctx;

  public BoundPropertyReference( IType rootType, Object ctx, String property )
  {
    _rootType = rootType;
    _ctx = ctx;
    _pi = PropertyReference.getPropertyInfo( rootType, property );
  }

  public T get()
  {
    return (T) _pi.getAccessor().getValue( _ctx );
  }

  public void set( T val )
  {
    _pi.getAccessor().setValue( _ctx, val );
  }

  public IType getRootType()
  {
    return _rootType;
  }

  public Object getCtx()
  {
    return _ctx;
  }

  public IPropertyInfo getPropertyInfo() {
    return _pi;
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getPropertyInfo();
  }

  @Override
  protected Object evaluate(Iterator args) {
    return get();
  }

  @Override
  protected List<IType> getFullArgTypes() {
    return new ArrayList<IType>();
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

    BoundPropertyReference that = (BoundPropertyReference)o;

    if( _ctx != null ? !_ctx.equals( that._ctx ) : that._ctx != null )
    {
      return false;
    }
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
    result = 31 * result + (_ctx != null ? _ctx.hashCode() : 0);
    return result;
  }
}
