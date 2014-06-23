/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.PublishedName;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MethodChainReference<R, T> extends FeatureReference<R, T> implements IMethodReference<R, T>, IFeatureChain
{
  private FeatureReference _root;
  private IType _rootType;
  private IMethodInfo _mi;
  private Object[] _boundValues;

  public MethodChainReference( IType rootType, FeatureReference root, String funcName, IType[] params, Object[] boundValues )
  {
    _root = root;
    _rootType = rootType;
    _mi = MethodReference.getMethodInfo(rootType, funcName, params );
    _boundValues = boundValues;
  }

  public Object evaluate( Object... args )
  {
    return evaluate( Arrays.asList( args ).iterator() );
  }
  
  @Override
  protected Object evaluate( Iterator args ) {
    Object ctx = _root.evaluate(args);
    IMethodInfo mi = getMethodInfo();
    if( _boundValues != null )
    {
      args = Arrays.asList(_boundValues).iterator();
    }    
    Object[] argArray = new Object[mi.getParameters().length];
    for (int i = 0; i < argArray.length; i++) {
      argArray[i] = args.next();
    }
    return mi.getCallHandler().handleCall(ctx, argArray);
  }

  @Override
  public List<IType> getFullArgTypes() {
    List argTypes = _root.getFullArgTypes();
    if( _boundValues == null )
    {
      for (IParameterInfo pi : _mi.getParameters()) {
        argTypes.add(pi.getFeatureType());
      }
    }
    return argTypes;
  }

  @Override
  public IMethodInfo getMethodInfo() {
    return _mi;
  }

  @PublishedName("invoke")
  @Override
  public T getinvoke()
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

  @Override
  public IType getRootType() {
    return _rootType;
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return getMethodInfo();
  }

  public IFeatureReference getRootFeatureReference() {
    return _root;
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

    MethodChainReference that = (MethodChainReference)o;

    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    if( !Arrays.equals( _boundValues, that._boundValues ) )
    {
      return false;
    }
    if( _mi != null ? !_mi.equals( that._mi ) : that._mi != null )
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
    result = 31 * result + (_mi != null ? _mi.hashCode() : 0);
    result = 31 * result + (_boundValues != null ? Arrays.hashCode( _boundValues ) : 0);
    return result;
  }
}
