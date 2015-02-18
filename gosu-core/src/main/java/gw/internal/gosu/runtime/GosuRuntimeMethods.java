/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.runtime;

import gw.config.CommonServices;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IExpando;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GosuRuntimeMethods {

  public static Object getProperty( Object root, IType type, String propertyName )
  {
    if( root != null && IExpando.class.isAssignableFrom( root.getClass() ) )
    {
      return ((IExpando)root).getFieldValue( propertyName );
    }

    if( isDynamic( type ) )
    {
      type = TypeSystem.getFromObject( root );
    }

    Object ret = invokePropertyGetter( "$getProperty", root, type, propertyName );
    if( ret != IPlaceholder.UNHANDLED )
    {
      return ret;
    }

    IPropertyInfo propertyInfo = getPropertyInfo( root, type, propertyName );
    if( propertyInfo == null )
    {
      ret = invokePropertyGetter( "$getMissingProperty", root, type, propertyName );
      if( ret == IPlaceholder.UNHANDLED )
      {
        throw new IllegalArgumentException( "No property named " + propertyName + " found on type " + type.getName() );
      }
      return ret;
    }
    return propertyInfo.getAccessor().getValue( root );
  }

  private static boolean isDynamic( IType type )
  {
    return (type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder()) ||
           (type instanceof IGosuClass && ((IGosuClass)type).isStructure());
  }

  private static Object invokePropertyGetter( String dispatchName, Object root, IType type, String propertyName )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    IMethodInfo method;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      method = ((IRelativeTypeInfo) typeInfo).getMethod( type, dispatchName, JavaTypes.STRING() );
    }
    else
    {
      method = typeInfo.getMethod( dispatchName, JavaTypes.STRING() );
    }
    return method == null
           ? IPlaceholder.UNHANDLED
           : method.getCallHandler().handleCall( root, propertyName );
  }

  public static Object getPropertyDynamically(Object rootObject, String propertyName) {
    if (rootObject == null) {
      throw new NullPointerException();
    }
    return getProperty(rootObject, TypeSystem.getFromObject(rootObject), propertyName);
  }

  public static void setProperty( Object root, IType type, String propertyName, Object value )
  {
    if( root != null && IExpando.class.isAssignableFrom( root.getClass() ) )
    {
      ((IExpando)root).setFieldValue( propertyName, value );
      return;
    }

    if( isDynamic( type ) )
    {
      type = TypeSystem.getFromObject( root );
    }

    Object ret = invokePropertySetter( "$setProperty", root, type, propertyName );
    if( ret != IPlaceholder.UNHANDLED )
    {
      return;
    }

    IPropertyInfo propertyInfo = getPropertyInfo( root, type, propertyName );
    if( propertyInfo == null )
    {
      ret = invokePropertySetter( "$setMissingProperty", root, type, propertyName );
      if( ret == IPlaceholder.UNHANDLED )
      {
        throw new IllegalArgumentException( "No property named " + propertyName + " found on type " + type.getName() );
      }
      return;
    }
    propertyInfo.getAccessor().setValue( root, value );
  }

  private static Object invokePropertySetter( String dispatchName, Object root, IType type, String propertyName, Object... args )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    IMethodInfo method;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      method = ((IRelativeTypeInfo) typeInfo).getMethod( type, dispatchName, JavaTypes.STRING(), JavaTypes.OBJECT() );
    }
    else
    {
      method = typeInfo.getMethod( dispatchName, JavaTypes.STRING(), JavaTypes.OBJECT() );
    }
    return method == null
           ? IPlaceholder.UNHANDLED
           : method.getCallHandler().handleCall( root, propertyName, args );
  }

  public static void setPropertyDynamically(Object rootObject, String propertyName, Object value) {
    if (rootObject == null) {
      throw new NullPointerException();
    }
    setProperty(rootObject, TypeSystem.getFromObject(rootObject), propertyName, value);
  }

  private static IPropertyInfo getPropertyInfo( Object rootObject, IType type, String propertyName )
  {
    IPropertyInfo propertyInfo = ReflectUtil.findProperty( type, propertyName );
    if( propertyInfo == null )
    {
      propertyInfo = ReflectUtil.findProperty( TypeSystem.getFromObject( rootObject ), propertyName );
      if( propertyInfo == null )
      {
        return null;
      }
    }
    return propertyInfo;
  }

  public static Object initMultiArray( IType componentType, Object instance, int iDimension, int[] sizes )
  {
    if( sizes.length <= iDimension-1 )
    {
      return instance;
    }

    int iLength = componentType.getArrayLength( instance );
    componentType = componentType.getComponentType();
    for( int i = 0; i < iLength; i++ )
    {
      Object component = componentType.makeArrayInstance( sizes[iDimension-1] );
      initMultiArray( componentType, component, iDimension + 1, sizes );
      componentType.setArrayComponent( instance, i, component );
    }
    return instance;
  }

  public static IType getType( Object obj )
  {
    return TypeSystem.get( obj.getClass() );
  }

  public static Object newInstance( IType type, Object ctx, Object[] args )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    IConstructorInfo method;
    IType[] runtimeTypes = ReflectUtil.extractRuntimeTypes( args );
    method = typeInfo.getCallableConstructor( runtimeTypes );
    args = ReflectUtil.coerceArgsIfNecessary( method.getParameters(), args );
    args = maybeAddOuter( type, ctx, args );
    return method.getConstructor().newInstance( args );
  }

  private static Object[] maybeAddOuter( IType type, Object ctx, Object[] args ) {
    if( ctx == null )
    {
      return args;
    }
    if( type instanceof IGosuClassInternal && ((IGosuClassInternal)type).isStatic() )
    {
      return args;
    }
    IType enclosingType = type.getEnclosingType();
    if( enclosingType == null )
    {
      return args;
    }
    IType outerType = TypeLord.getPureGenericType( TypeSystem.getFromObject( ctx ) );
    enclosingType = TypeLord.getPureGenericType( enclosingType );
    while( outerType != enclosingType )
    {
      try {
        Field outerThis = ctx.getClass().getDeclaredField( "this$0" );
        outerThis.setAccessible( true );
        ctx = outerThis.get( ctx );
      }
      catch( Exception e ) {
        return args;
      }
      outerType = TypeLord.getPureGenericType( TypeSystem.getFromObject( ctx ) );
    }
    Object[] args2 = new Object[args.length + 1];
    args2[0] = ctx;
    System.arraycopy( args, 0, args2, 1, args.length );
    return args2;
  }

  public static Object invokeMethod( Class c, String methodName, Class[] argTypes, Object root, Object[] args )
  {
    Method declaredMethod = AbstractElementTransformer.getDeclaredMethod( c, methodName, argTypes );
    try
    {
      return declaredMethod.invoke( root, args );
    }
    catch( IllegalAccessException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    catch( InvocationTargetException e )
    {
      throw GosuExceptionUtil.forceThrow( e.getTargetException() );
    }
  }

  public static Object invokeMethodInfo( IType type, String methodName, IType[] parameterTypes, Object root, Object[] args )
  {
    if( root instanceof IExpando )
    {
      return ((IExpando)root).invoke( methodName, args );
    }

    boolean bDynamicType = isDynamic( type );
    if( bDynamicType )
    {
      type = TypeSystem.getFromObject( root );
    }

    Object ret = invokeMethodInvoker( "$invokeMethod", root, type, methodName, args );
    if( ret != IPlaceholder.UNHANDLED )
    {
      return ret;
    }

    ITypeInfo typeInfo = type.getTypeInfo();
    IMethodInfo method;
    if( bDynamicType )
    {
      IType[] runtimeTypes = ReflectUtil.extractRuntimeTypes( args );
      method = ReflectUtil.findCallableMethod( methodName, runtimeTypes, type );
    }
    else
    {
      parameterTypes = replaceDynamicTypesWithRuntimeTypes( parameterTypes, args );
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        method = ((IRelativeTypeInfo)typeInfo).getMethod( type, methodName, parameterTypes );
      }
      else
      {
        method = typeInfo.getMethod( methodName, parameterTypes );
      }
    }

    if( method == null )
    {
      ret = invokeMethodInvoker( "$invokeMissingMethod", root, type, methodName, args );
      if( ret == IPlaceholder.UNHANDLED )
      {
        throw new IllegalStateException( "Could not find method for " + methodName + " on " + type.getName() + " with specified param types" );
      }
      return ret;
    }
    if( bDynamicType )
    {
      args = ReflectUtil.coerceArgsIfNecessary( method.getParameters(), args );
    }
    return method.getCallHandler().handleCall( root, args );
  }

  private static IType[] replaceDynamicTypesWithRuntimeTypes( IType[] parameterTypes, Object[] args ) {
    if( parameterTypes ==  null ) {
      return null;
    }
    IType[] ret = null;
    for( int i = 0; i < parameterTypes.length; i++ ) {
      IType type = parameterTypes[i];
      if( type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder() ) {
        if( ret == null ) {
          ret = new IType[parameterTypes.length];
          System.arraycopy( parameterTypes, 0, ret, 0, ret.length );
        }
        ret[i] = args[i] == null ? ret[i] : TypeSystem.getFromObject( args[i] );
      }
    }
    return ret == null ? parameterTypes : ret;
  }

  private static Object invokeMethodInvoker( String dispatchName, Object root, IType type, String methodName, Object... args )
  {
    ITypeInfo typeInfo = type.getTypeInfo();
    IMethodInfo method;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      method = ((IRelativeTypeInfo)typeInfo).getMethod( type, dispatchName, JavaTypes.STRING(), JavaTypes.OBJECT().getArrayType() );
    }
    else
    {
      method = typeInfo.getMethod( dispatchName, JavaTypes.STRING(), JavaTypes.OBJECT().getArrayType() );
    }
    return method == null
           ? IPlaceholder.UNHANDLED
           : method.getCallHandler().handleCall( root, methodName, args );
  }

  public static Class lookUpClass( String className ) {
    if (className.startsWith("L") && className.endsWith(";")) {
      className = className.substring(1, className.length() -1 );
    }
    className = className.replaceAll("/", ".");

    try
    {
      return Class.forName(className, false, GosuRuntimeMethods.class.getClassLoader());
    }
    catch( ClassNotFoundException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public static void invokeLockMethod( Object o )
  {
    if( o != null )
    {
      IMethodInfo iMethodInfo = TypeSystem.getFromObject( o ).getTypeInfo().getMethod( "lock" );
      if( iMethodInfo != null )
      {
        iMethodInfo.getCallHandler().handleCall( o );
      }
    }
  }

  public static IType typeof( Object o )
  {
    IType type = TypeSystem.getFromObject( o );
    if( type instanceof IJavaType && type.isGenericType() )
    {
      // Never return a generic type resulting from Java's generic type erasure.
      // Instead return the "erased" or default type.
      type = TypeLord.getDefaultParameterizedType( type );
    }
    return type;
  }

  public static boolean logicalNot( Object o )
  {
    if( o instanceof Boolean ) {
      return !((Boolean)o).booleanValue();
    }
    return !CommonServices.getCoercionManager().makePrimitiveBooleanFrom( o );
  }

  public static void invokeUnlockOrDisposeOrCloseMethod( Object o )
  {
    if( o != null )
    {
      ITypeInfo ti = TypeSystem.getFromObject( o ).getTypeInfo();
      IMethodInfo mi = ti.getMethod( "unlock" );
      if( mi != null )
      {
        mi.getCallHandler().handleCall( o );
      }
      else
      {
        mi = ti.getMethod( "dispose" );
        if( mi != null )
        {
          mi.getCallHandler().handleCall( o );
        }
        else
        {
          mi = ti.getMethod( "close" );
          if( mi != null )
          {
            mi.getCallHandler().handleCall( o );
          }
          else
          {

          }
        }
      }
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public static boolean isStructurallyAssignable( IType toType, IType fromType )
  {
    if( toType == null || fromType == null )
    {
      return false;
    }

    //noinspection SimplifiableIfStatement
    if( toType.isAssignableFrom( fromType ) )
    {
      // Nominal assignability check first
      return true;
    }

    return StandardCoercionManager.isStructurallyAssignable( toType, fromType );
  }

  public static void print( Object obj )
  {
    System.out.println( toString( obj ) );
  }

  public static String toString( Object obj ) {
    if ( obj == null )
    {
      return "null";
    }
    if ( obj instanceof Byte )
    {
      int value = (Byte) obj;
      if ( value < 0 ) {
        value = 256 + value;
      }
      return "0x" + Integer.toHexString( value );
    }
    IType type = TypeSystem.getFromObject( obj );
    if ( type.isArray() )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( '[' );
      int arrayLength = type.getArrayLength(obj);
      for ( int idx = 0; idx < arrayLength; idx++ )
      {
        if ( idx > 0 )
        {
          sb.append( ", " );
        }
        sb.append( toString( type.getArrayComponent( obj, idx ) ) );
      }
      sb.append( ']' );
      return sb.toString();
    }
    return obj.toString();
  }

  public static void error( Object strError )
  {
    System.out.println( strError );
    throw new Error( String.valueOf( strError ) );
  }
}
