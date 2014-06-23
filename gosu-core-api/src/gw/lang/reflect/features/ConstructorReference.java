/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.PublishedName;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ConstructorReference<R, T> extends FeatureReference<R, T> implements IConstructorReference<R,T>
{
  private IConstructorInfo _ci;
  private Object[] _boundValues;

  public ConstructorReference( IType rootType, IType[] params, Object[] boundValues )
  {
    ITypeInfo typeInfo = rootType.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      _ci = ((IRelativeTypeInfo)typeInfo).getConstructor( rootType, params );
    }
    else
    {
      _ci = typeInfo.getConstructor( params );
    }
    _boundValues = boundValues;
  }

  public IConstructorInfo getConstructorInfo()
  {
    return _ci;
  }

  public Object evaluate( Object... args )
  {
    return evaluate( Arrays.asList( args ).iterator() );
  }

  public IType getRootType()
  {
    return _ci.getType();
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getConstructorInfo();
  }

  @Override
  protected Object evaluate(Iterator args) {
    Object[] argArray = new Object[_ci.getParameters().length];
    if( _boundValues != null )
    {
      args = Arrays.asList(_boundValues).iterator();
    }
    for (int i = 0; i < argArray.length; i++) {
      argArray[i] = args.next();
    }
    return _ci.getConstructor().newInstance(argArray);

  }

  @Override
  public List<IType> getFullArgTypes() {
    ArrayList<IType> lst = new ArrayList<IType>();
    if( _boundValues == null )
    {
      for (IParameterInfo pi : _ci.getParameters()) {
        lst.add(pi.getFeatureType());
      }
    }
    return lst;
  }

  public T toBlock()
  {
    return (T)BlockWrapper.toBlock( this );
  }

  @Override
  public Object[] getBoundArgValues()
  {
    return _boundValues;
  }

  @PublishedName("invoke")
  public T getinvoke()
  {
    return toBlock();
  }

  public Object[] getBoundValues() {
    return _boundValues;
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

    ConstructorReference that = (ConstructorReference)o;

    if( !Arrays.equals( _boundValues, that._boundValues ) )
    {
      return false;
    }
    if( _ci != null ? !_ci.equals( that._ci ) : that._ci != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _ci != null ? _ci.hashCode() : 0;
    result = 31 * result + (_boundValues != null ? Arrays.hashCode( _boundValues ) : 0);
    return result;
  }
}
