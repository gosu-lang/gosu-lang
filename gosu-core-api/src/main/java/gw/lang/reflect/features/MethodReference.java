/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.PublishedName;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MethodReference<R, T> extends FeatureReference<R, T> implements IMethodReference<R, T>
{
  private IMethodInfo _mi;
  private IType _rootType;
  private Object[] _boundValues;
  private boolean _voidReturn;

  public MethodReference( IType rootType, String funcName, IType[] params, Object[] boundValues )
  {
    _rootType = rootType;
    _mi = getMethodInfo( rootType, funcName, params );
    _boundValues = boundValues;
  }

  private MethodReference()
  {}

  public MethodReference copyWithVoidReturn()
  {
    MethodReference<R, T> voidReturnCopy = new MethodReference<>();
    voidReturnCopy._mi = _mi;
    voidReturnCopy._rootType = _rootType;
    voidReturnCopy._boundValues = _boundValues;
    voidReturnCopy._voidReturn = true;
    return voidReturnCopy;
  }

  @Override
  protected boolean hasReturn()
  {
    return !_voidReturn && super.hasReturn();
  }

  static IMethodInfo getMethodInfo( IType rootType, String funcName, IType[] params )
  {
    ITypeInfo typeInfo = rootType.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      return ((IRelativeTypeInfo)typeInfo).getMethod( rootType, funcName, params );
    }
    else
    {
      return typeInfo.getMethod( funcName, params );
    }
  }

  public IMethodInfo getMethodInfo()
  {
    return _mi;
  }

  @PublishedName("invoke")
  @Override
  public T getInvoke()
  {
    return toBlock();
  }

  @Override
  public T toBlock() {
    return (T) BlockWrapper.toBlock(this);    
  }

  @Override
  public Object[] getBoundArgValues()
  {
    return _boundValues;
  }

  public Object evaluate( Object... args )
  {
    return evaluate( Arrays.asList( args ).iterator() );
  }

  @Override
  protected Object evaluate( Iterator args ) {
    Object ctx = null;
    if (!_mi.isStatic()) {
      ctx = args.next();
    }
    if( _boundValues != null )
    {
      args = Arrays.asList( _boundValues ).iterator();
    }    
    Object[] argArray = new Object[_mi.getParameters().length];
    for (int i = 0; i < argArray.length; i++) {
      argArray[i] = args.next();
    }
    return _mi.getCallHandler().handleCall(ctx, argArray);
  }

  @Override
  public List<IType> getFullArgTypes() {
    ArrayList<IType> argTypes = new ArrayList<IType>();
    if (!_mi.isStatic()) {
      argTypes.add(_mi.getOwnersType());
    }
    if( _boundValues == null )
    {
      for (IParameterInfo iParameterInfo : _mi.getParameters()) {
        argTypes.add(iParameterInfo.getFeatureType());
      }
    }
    return argTypes;
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getMethodInfo();
  }

  public IType getRootType()
  {
    return _rootType;
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

    MethodReference that = (MethodReference)o;

    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    if( !Arrays.equals( _boundValues, that._boundValues ) )
    {
      return false;
    }
    if( _mi != null ? !_mi.equals( that._mi ) : that._mi != null )
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
    int result = _mi != null ? _mi.hashCode() : 0;
    result = 31 * result + (_rootType != null ? _rootType.hashCode() : 0);
    result = 31 * result + (_boundValues != null ? Arrays.hashCode( _boundValues ) : 0);
    return result;
  }
}