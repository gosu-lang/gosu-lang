/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodInfoBase;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.SimpleParameterInfo;

import java.util.Collections;
import java.util.List;

/**
 */
public class DynamicMethodInfo extends MethodInfoBase implements IMethodCallHandler
{
  private String _strName;
  private IType[] _paramTypes;
  private IParameterInfo[] _paramInfos;


  protected DynamicMethodInfo( ITypeInfo container, String strName, IType... paramTypes )
  {
    super( container );
    _strName = strName;
    _paramTypes = paramTypes == null ? IType.EMPTY_ARRAY : paramTypes;
    makeParameters();
  }

  private void makeParameters()
  {
    _paramInfos = new IParameterInfo[_paramTypes.length];
    for( int i = 0; i < _paramTypes.length; i++ )
    {
      _paramInfos[i] = new SimpleParameterInfo( this, _paramTypes[i], i );
    }
  }

  @Override
  public IParameterInfo[] getParameters()
  {
    return _paramInfos;
  }

  @Override
  public IType getReturnType()
  {
    return getOwnersType();
  }

  @Override
  public IMethodCallHandler getCallHandler()
  {
    return this;
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  @Override
  public Object handleCall( Object ctx, Object... args )
  {
    return ReflectUtil.invokeMethod( ctx, getName(), args );
  }
}
