/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.util.GosuStringUtil;
import gw.util.GosuExceptionUtil;

import java.util.List;
import java.lang.reflect.Method;

public class ReflectUtil
{
  public static <T> T construct( String typeName, Object... args )
  {
    IType type = TypeSystem.getByFullName( typeName );
    ensureTypeIsValid( type );

    IType[] runtimeTypes = extractRuntimeTypes( args );

    IConstructorInfo constructorInfo = findCallableConstructor( type, runtimeTypes );

    if( constructorInfo != null )
    {
      return (T) constructorInfo.getConstructor().newInstance( coerceArgsIfNecessary( constructorInfo.getParameters(), args ) );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a constructor on " + type.getName() + " with parameters compatible with arg types ["
                                          + GosuStringUtil.join( runtimeTypes, "," ) + "]");
    }
  }

  public static IGosuObject constructGosuClassInstance( String className, Object... args )
  {
    return (IGosuObject) construct( className, args );
  }

  public static Object invokeStaticMethod( String className, String methodName, Object... args )
  {
    IType type = TypeSystem.getByFullName( className );
    ensureTypeIsValid( type );

    IType[] runtimeTypes = extractRuntimeTypes( args );

    IMethodInfo info = findCallableMethod( methodName, runtimeTypes, type );

    if( info != null && info.isStatic() )
    {
      return info.getCallHandler().handleCall( type, coerceArgsIfNecessary( info.getParameters(), args ) );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a static method on " + type.getName() + " with name " +  methodName +
                                          " and parameters compatible with arg types [" + GosuStringUtil.join( runtimeTypes, "," ) + "]");
    }
  }

  public static void setStaticProperty( String className, String propertyName, Object value )
  {
    IType type = TypeSystem.getByFullName( className );
    setStaticProperty( type, propertyName, value );
  }

  public static void setStaticProperty( IType type, String propertyName, Object value )
  {
    ensureTypeIsValid( type );
    IPropertyInfo propertyInfo = findProperty( type, propertyName );

    if( propertyInfo != null && propertyInfo.isStatic() )
    {
      IType iType = propertyInfo.getFeatureType();
      propertyInfo.getAccessor().setValue( type, coerce( value, iType ) );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a static property on " + type.getName() + " with name " +  propertyName );
    }
  }

  public static Object getStaticProperty( String className, String propertyName )
  {
    IType type = TypeSystem.getByFullName( className );
    return getStaticProperty( type, propertyName );
  }

  public static Object getStaticProperty( IType type, String propertyName )
  {
    ensureTypeIsValid( type );
    IPropertyInfo propertyInfo = findProperty( type, propertyName );

    if( propertyInfo != null && propertyInfo.isStatic() )
    {
      return propertyInfo.getAccessor().getValue( type );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a static property on " + type.getName() + " with name " +  propertyName );
    }
  }

  public static Object invokeMethod( Object instance, String methodName, Object... args )
  {
    IType[] runtimeTypes = extractRuntimeTypes( args );

    IType type = TypeSystem.getFromObject( instance );
    IMethodInfo methodInfo = findCallableMethod( methodName, runtimeTypes, type );

    if( methodInfo != null )
    {
      return methodInfo.getCallHandler().handleCall( instance, coerceArgsIfNecessary( methodInfo.getParameters(), args ) );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a method on " + type.getName() + " with name " +  methodName +
                                          " and parameters compatible with arg types [" + GosuStringUtil.join( runtimeTypes, "," ) + "]" );
    }
  }

  public static void setProperty( Object instance, String propertyName, Object value )
  {
    IType type = TypeSystem.getFromObject( instance );
    IPropertyInfo propertyInfo = findProperty( type, propertyName );

    if( propertyInfo != null )
    {
      propertyInfo.getAccessor().setValue( instance, coerce( value, propertyInfo.getFeatureType() ) );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a property on " + type.getName() + " with name " + propertyName );
    }
  }

  public static Object getProperty( Object instance, String propertyName )
  {
    IType type = TypeSystem.getFromObject( instance );
    IPropertyInfo propertyInfo = findProperty( type, propertyName );

    if( propertyInfo != null )
    {
      return propertyInfo.getAccessor().getValue( instance );
    }
    else
    {
      throw new IllegalArgumentException( "Unable to find a property on " + type.getName() + " with name " + propertyName );
    }
  }

  public static IGosuClass getClass( String fullyQualifiedName )
  {
    return (IGosuClass)TypeSystem.getByFullName( fullyQualifiedName );
  }

  public static IGosuClass getClassButThrowIfInvalid( String fullyQualifiedName )
  {
    IGosuClass gosuClass = getClass( fullyQualifiedName );
    if( !gosuClass.isValid() )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      throw new RuntimeException( gosuClass.getParseResultsException() );
    }
    return gosuClass;
  }

  public static Object[] coerceArgsIfNecessary( IParameterInfo[] parameters, Object... args )
  {
    for( int i = 0; i < parameters.length; i++ )
    {
      args[i] = coerce( args[i], parameters[i].getFeatureType() );
    }
    return args;
  }

  public static IPropertyInfo findProperty( IType type, String propertyName )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    return typeInfo instanceof IRelativeTypeInfo ?
           ((IRelativeTypeInfo)typeInfo).getProperty( type, propertyName ) :
           typeInfo.getProperty( propertyName );
  }

  public static IMethodInfo findCallableMethod( String methodName, IType[] runtimeTypes, IType type )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    MethodList methodInfos = typeInfo instanceof IRelativeTypeInfo ?
                                              ((IRelativeTypeInfo)typeInfo).getMethods( type ) : typeInfo.getMethods();

    return ITypeInfo.FIND.callableMethod( methodInfos, methodName, runtimeTypes );
  }

  private static IConstructorInfo findCallableConstructor( IType type, IType[] runtimeTypes )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    List<? extends IConstructorInfo> constructors = typeInfo instanceof IRelativeTypeInfo ?
                                                    ((IRelativeTypeInfo)typeInfo).getConstructors( type ) :
                                                    typeInfo.getConstructors();
    return ITypeInfo.FIND.callableConstructor( constructors, runtimeTypes );
  }

  private static void ensureTypeIsValid( IType type )
  {
    if( !type.isValid() )
    {
      if( type instanceof IGosuClass )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        throw new IllegalArgumentException( "The type " + type.getName() + " is invalid", ((IGosuClass)type).getParseResultsException() );
      }
      else
      {
        throw new IllegalArgumentException( "The type " + type.getName() + " is invalid" );
      }
    }
  }

  public static IType[] extractRuntimeTypes( Object... args )
  {
    IType[] runtimeTypes = new IType[args.length];
    for( int i = 0; i < args.length; i++ )
    {
      runtimeTypes[i] = TypeSystem.getFromObject( args[i] );
    }
    return runtimeTypes;
  }

  public static Object coerce( Object value, IType iType )
  {
    return CommonServices.getCoercionManager().convertValue(value, iType );
  }

  public static IGosuObject getEnclosingClassInstance( IGosuObject obj )
  {
    Class cls = obj.getClass();
    IGosuObject outer = null;
    while( Modifier.isStatic( cls.getModifiers() ) && cls.getEnclosingClass() != null )
    {
      try
      {
        Method m = cls.getDeclaredMethod( "access$0", obj.getClass() );
        m.setAccessible( true );
        obj = outer = (IGosuObject)m.invoke( null, obj );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
      cls = outer.getClass();
    }
    return outer;
  }

}
