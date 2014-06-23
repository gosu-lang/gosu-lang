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

public class ComplexPropertyChainReference<R, T> extends FeatureReference<R, T> implements IPropertyReference<R, T>, IFeatureChain
{
  private FeatureReference _root;
  private IPropertyInfo _pi;

  public ComplexPropertyChainReference( IType rootType, FeatureReference root, String property )
  {
    _root = root;
    _pi = PropertyReference.getPropertyInfo( rootType, property );
  }

  public T get( R ctx, List args ) {
    List newArgs = new ArrayList();
    newArgs.add(ctx);
    newArgs.addAll(args);
    Object fromRoot = _root.evaluate(newArgs.iterator());
    return (T) getPropertyInfo().getAccessor().getValue(fromRoot);
  }

  public void set( R ctx, List args, T val )
  {
    List newArgs = new ArrayList();
    newArgs.add(ctx);
    newArgs.addAll(args);
    Object fromRoot = _root.evaluate(newArgs.iterator());
    getPropertyInfo().getAccessor().setValue(fromRoot, val);
  }

  @Override
  public Object evaluate( Iterator args )
  {
    return getPropertyInfo().getAccessor().getValue(_root.evaluate( args ));
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getPropertyInfo();
  }

  @Override
  public List<IType> getFullArgTypes() {
    return _root.getFullArgTypes();
  }

  @Override
  public IPropertyInfo getPropertyInfo()
  {
    return _pi;
  }

  public IType getRootType()
  {
    return _root.getRootType();
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

    ComplexPropertyChainReference that = (ComplexPropertyChainReference)o;

    if( _pi != null ? !_pi.equals( that._pi ) : that._pi != null )
    {
      return false;
    }
    if( _root != null ? !_root.equals( that._root ) : that._root != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _root != null ? _root.hashCode() : 0;
    result = 31 * result + (_pi != null ? _pi.hashCode() : 0);
    return result;
  }
}
