/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.JavaExceptionInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleMethodInfo extends BaseFeatureInfo implements IMethodInfo
{
  private IJavaClassMethod _method;
  private ScriptabilityModifier _modifier;
  private ArrayList<IExceptionInfo> _exceptions;
  private IMethodCallHandler _callHandler;
  private IParameterInfo[] _paramInfo;

  public SimpleMethodInfo( ScriptabilityModifier modifier, IJavaClassInfo clazz, String methodName, IJavaClassInfo... paramTypes )
  {
    super( clazz.getJavaType() );
    _modifier = modifier;
    try
    {
      _method = clazz.getMethod( methodName, paramTypes );
    }
    catch( NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }

    _exceptions = new ArrayList<IExceptionInfo>();
    IJavaClassInfo[] exceptionTypes = _method.getExceptionTypes();
    for (IJavaClassInfo aClass : exceptionTypes) {
      _exceptions.add(new JavaExceptionInfo(this, aClass, null));
    }

    _callHandler = new IMethodCallHandler()
    {
      @Override
      public Object handleCall( Object ctx, Object... args )
      {
        try
        {
          return _method.invoke( ctx, args );
        }
        catch( IllegalAccessException e )
        {
          throw new RuntimeException( e );
        }
        catch( InvocationTargetException e )
        {
          throw new RuntimeException( e );
        }
      }
    };

    ArrayList<IParameterInfo> list = new ArrayList<IParameterInfo>();
    for (IJavaClassInfo aClass : paramTypes) {
      list.add(new SimpleParameterInfo(this, aClass.getJavaType(), 0));
    }
    _paramInfo = list.toArray( new IParameterInfo[list.size()] );
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  @Override
  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return _modifier.satisfiesConstraint( constraint );
  }

  @Override
  public boolean isStatic()
  {
    return Modifier.isStatic( _method.getModifiers() );
  }

  @Override
  public String getName()
  {
    return _method.getName();
  }

  @Override
  public IParameterInfo[] getParameters()
  {
    return _paramInfo;
  }

  @Override
  public IType getReturnType()
  {
    return _method.getReturnType();
  }

  @Override
  public IMethodCallHandler getCallHandler()
  {
    return _callHandler;
  }

  @Override
  public String getReturnDescription()
  {
    return "";
  }

  @Override
  public List<IExceptionInfo> getExceptions()
  {
    return _exceptions;
  }

}
