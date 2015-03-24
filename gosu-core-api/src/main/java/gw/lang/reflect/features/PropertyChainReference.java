/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PropertyChainReference<R, T> extends FeatureReference<R, T> implements IPropertyReference<R, T>, IFeatureChain
{
  private FeatureReference _root;
  private IType _rootType;
  private IPropertyInfo _pi;

  public PropertyChainReference(IType rootType, FeatureReference root, String property)
  {
    _rootType = rootType;
    _pi = PropertyReference.getPropertyInfo( rootType, property );
    _root = root;
  }

  public T get( R ctx )
  {
    Object fromRoot = _root.evaluate(Collections.singleton(ctx).iterator());
    return (T)getPropertyInfo().getAccessor().getValue( fromRoot );
  }

  public void set( R ctx, T val )
  {
    Object fromRoot = _root.evaluate(Collections.singleton(ctx).iterator());
    getPropertyInfo().getAccessor().setValue( fromRoot, val );
  }

  public IType getRootType()
  {
    return _rootType;
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getPropertyInfo();
  }

  @Override
  protected Object evaluate(Iterator args) {
    Object ctx = _root.evaluate(args);
    return _pi.getAccessor().getValue(ctx);
  }

  @Override
  protected List<IType> getFullArgTypes() {
    return _root.getFullArgTypes();
  }

  @Override
  public IPropertyInfo getPropertyInfo() {
    return _pi;
  }

  @Override
  public IFeatureReference getRootFeatureReference() {
    return _root;
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

    PropertyChainReference that = (PropertyChainReference)o;

    if( _pi != null ? !_pi.equals( that._pi ) : that._pi != null )
    {
      return false;
    }
    if( _root != null ? !_root.equals( that._root ) : that._root != null )
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
    int result = _root != null ? _root.hashCode() : 0;
    result = 31 * result + (_rootType != null ? _rootType.hashCode() : 0);
    result = 31 * result + (_pi != null ? _pi.hashCode() : 0);
    return result;
  }
}
