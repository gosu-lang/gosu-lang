package gw.internal.gosu.parser;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IIntrinsicTypeReference;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles Gosu->Manifold extension method call adaptation
 */
public class ManifoldExtensionMethodCallAdapter implements IMethodCallHandler
{
  private boolean _static;
  private Method _method;
  private Class[] _argTypes;

  ManifoldExtensionMethodCallAdapter( JavaMethodInfo mi )
  {
    IAnnotationInfo extAnno = mi.getAnnotation( JavaTypes.EXTENSION_METHOD() );
    String extensionClass = (String)extAnno.getFieldValue( "extensionClass" );
    IType extType = TypeSystem.getByFullNameIfValid( extensionClass );

    List<IType> paramTypes = new ArrayList<>();
    _static = mi.isStatic();
    if( !_static )
    {
      paramTypes.add( mi.getOwnersType() );
    }
    paramTypes.addAll( Arrays.stream( mi.getParameters() ).map( IIntrinsicTypeReference::getFeatureType ).collect( Collectors.toList() ) );
    extType.getTypeInfo().getMethod( mi.getName(), paramTypes.toArray( IType.EMPTY_TYPE_ARRAY ) );
    IMethodInfo extMi = extType.getTypeInfo().getMethod( mi.getDisplayName(), paramTypes.toArray( IType.EMPTY_TYPE_ARRAY ) );
    if( extMi == null )
    {
      throw GosuExceptionUtil.forceThrow( new NoSuchMethodException( mi.toString() ) );
    }
    _method = ((MethodJavaClassMethod)((JavaMethodInfo)extMi).getMd().getMethod()).getJavaMethod();
    _argTypes = _method.getParameterTypes(); // Cache this so we don't have to create a copy every time
    _method.setAccessible( true );
  }

  public Object handleCall( Object ctx, Object... argValues )
  {
    ClassLoader previousClassLoader = null;
    boolean bMethodOnThread = Thread.class.isAssignableFrom( _method.getDeclaringClass() );
    if( !bMethodOnThread )
    {
      previousClassLoader = Thread.currentThread().getContextClassLoader();
      if(TypeSystem.getCurrentModule() != null) {
        Thread.currentThread().setContextClassLoader( TypeSystem.getGosuClassLoader().getActualLoader() ); //_method.getDeclaringClass().getClassLoader() );
      }
    }
    try
    {
      if( _static )
      {
        return _method.invoke( ctx, argValues );
      }
      else
      {
        Object[] args = new Object[argValues.length+1];
        args[0] = ctx;
        System.arraycopy( argValues, 0, args, 1, argValues.length );
        return _method.invoke( null, args );
      }
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
    StringBuilder params = new StringBuilder();
    for( int i = 0; i < method.getParameterTypes().length; i++ )
    {
      Class aClass = method.getParameterTypes()[i];
      if( i != 0 )
      {
        params.append( ", " );
      }
      params.append( aClass.getName() );
    }
    return new RuntimeException(
      "could not invoke " + method.getName() +
      "(" + params + ")" +
      " on class " + _method.getDeclaringClass().getName(), t );
  }
}