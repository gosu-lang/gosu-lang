/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.EvaluationException;
import gw.util.GosuExceptionUtil;
import gw.util.GosuClassUtil;

/**
 * Handles Gosu->Java method call adaptation
 */
public class MethodCallAdapter implements IMethodCallHandler
{
  private Method _method = null;
  private Class[] _argTypes = null;

  public MethodCallAdapter( Method method )
  {
    _method = method;
    _argTypes = method.getParameterTypes(); // Cache this so we don't have to create a copy every time
    _method.setAccessible( true );
  }

  public Object handleCall( Object ctx, Object... argValues )
  {
    if( argValues == null ? _argTypes.length != 0 : _argTypes.length != argValues.length )
    {
      throw new EvaluationException( "Wrong number of arguments for method " + _method.getName() + " in class " +
                                     _method.getDeclaringClass().getName() );
    }

    if( !Modifier.isStatic( _method.getModifiers() ) )
    {
      if( ctx == null )
      {
        throw new EvaluationException( "Tried to invoke method from null reference: " + _method.getDeclaringClass().getName() + "#" + _method.getName() );
      }
      if( !_method.getDeclaringClass().isAssignableFrom( ctx.getClass() ) )
      {
        throw new EvaluationException( "Tried to invoke method from a context not compatible with method's declaring class.\nContext: " + ctx.getClass().getName() + "\nMethod: " + _method.getDeclaringClass().getName() + "#" + _method.getName() );
      }
    }

    ClassLoader previousClassLoader = null;
    boolean bMethodOnThread = Thread.class.isAssignableFrom( _method.getDeclaringClass() );
    if( !bMethodOnThread )
    {
      previousClassLoader = Thread.currentThread().getContextClassLoader();
      if( TypeSystem.getModule() != null) {
        Thread.currentThread().setContextClassLoader( TypeSystem.getGosuClassLoader().getActualLoader() ); //_method.getDeclaringClass().getClassLoader() );
      }
    }
    try
    {
      return _method.invoke( ctx, argValues );
    }
    catch( InvocationTargetException ex )
    {
      throw GosuExceptionUtil.forceThrow( ex.getCause() );
    }
    catch( IllegalArgumentException ie )
    {
      GosuExceptionUtil.throwArgMismatchException( ie, "method \"" + GosuClassUtil.getShortClassName( _method.getDeclaringClass() ) + "#" + _method.getName() + "\"", _method.getParameterTypes(), argValues );
      return null;
    }
    catch( Throwable t )
    {
      throw makeMethodCallEvaluationException( _method, t );
    }
    finally
    {
      if( !bMethodOnThread )
      {
        Thread.currentThread().setContextClassLoader( previousClassLoader );
      }
    }
  }

  public Method getMethod()
  {
    return _method;
  }

  private RuntimeException makeMethodCallEvaluationException( Method method, Throwable t )
  {
    String params = "";
    for( int i = 0; i < method.getParameterTypes().length; i++ )
    {
      Class aClass = method.getParameterTypes()[i];
      if( i != 0 )
      {
        params += ", ";
      }
      params += aClass.getName();
    }
    return new RuntimeException(
      "could not invoke " + method.getName() +
      "(" + params + ")" +
      " on class " + _method.getDeclaringClass().getName(), t );
  }
}