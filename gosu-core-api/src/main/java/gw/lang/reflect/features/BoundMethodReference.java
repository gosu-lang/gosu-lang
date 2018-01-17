/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.PublishedName;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BoundMethodReference<R, T> extends FeatureReference<R, T> implements IMethodReference<R, T> {
  private IMethodInfo _mi;
  private IType _rootType;
  private Object _ctx;
  private Object[] _boundValues;
  private boolean _voidReturn;

  public BoundMethodReference( IType rootType, Object ctx, String funcName, IType[] params, Object[] boundValues )
  {
    _rootType = rootType;
    _ctx = ctx;
    _mi = MethodReference.getMethodInfo(rootType, funcName, params );
    _boundValues = boundValues;
  }

  private BoundMethodReference()
  {}

  public BoundMethodReference copyWithVoidReturn()
  {
    BoundMethodReference<R, T> voidReturnCopy = new BoundMethodReference<>();
    voidReturnCopy._mi = _mi;
    voidReturnCopy._rootType = _rootType;
    voidReturnCopy._ctx = _ctx;
    voidReturnCopy._boundValues = _boundValues;
    voidReturnCopy._voidReturn = true;
    return voidReturnCopy;
  }

  @Override
  protected boolean hasReturn()
  {
    return !_voidReturn && super.hasReturn();
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

  public Object evaluate( Object... args )
  {
    return evaluate( Arrays.asList( args ).iterator() );
  }
  
  @Override
  public Object evaluate( Iterator args )
  {
    if( _boundValues != null )
    {
      args = Arrays.asList(_boundValues).iterator();
    }    
    Object[] argArray = new Object[_mi.getParameters().length];
    for( int i = 0; i < argArray.length; i++ )
    {
      argArray[i] = args.next();
    }
    return _mi.getCallHandler().handleCall( _ctx, argArray );
  }

  public IType getRootType()
  {
    return _rootType;
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

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getMethodInfo();
  }

  @Override
  public List<IType> getFullArgTypes() {
    ArrayList<IType> lst = new ArrayList<IType>();
    if( _boundValues == null )
    {
      for (IParameterInfo iParameterInfo : _mi.getParameters()) {
        lst.add(iParameterInfo.getFeatureType());
      }
    }
    return lst;
  }

  public Object getCtx()
  {
    return _ctx;
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

    BoundMethodReference that = (BoundMethodReference)o;

    if( !Arrays.equals( _boundValues, that._boundValues ) )
    {
      return false;
    }
    if( _ctx != null ? !_ctx.equals( that._ctx ) : that._ctx != null )
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
    result = 31 * result + (_ctx != null ? _ctx.hashCode() : 0);
    result = 31 * result + (_boundValues != null ? Arrays.hashCode( _boundValues ) : 0);
    return result;
  }
}
