/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.GosuShop;
import gw.lang.function.IBlock;
import gw.lang.parser.IResolvingCoercer;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.features.IMethodReference;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FunctionToInterfaceCoercer extends BaseCoercer implements IResolvingCoercer
{
  private static FunctionToInterfaceCoercer _instance = new FunctionToInterfaceCoercer();

  public static FunctionToInterfaceCoercer instance()
  {
    return _instance;
  }

  private FunctionToInterfaceCoercer()
  {
  }

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value instanceof IMethodReference )
    {
      return coerceValue( typeToCoerceTo, ((IMethodReference)value).toBlock() );
    }
    else if( value instanceof IBlock )
    {
      IGosuClass proxyClass = GosuShop.getBlockToInterfaceConversionClass( typeToCoerceTo, TypeSystem.get( value.getClass().getEnclosingClass() ) );
      IBlock blk = (IBlock)value;
      try {
        //noinspection unchecked
        return proxyClass.getBackingClass().getConstructor( IBlock.class ).newInstance( blk );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    else
    {
      throw new IllegalStateException();
    }
  }

  //## WARNING: call interfaceType.getFunctionalInterface() instead, it caches the result
  public static IFunctionType getRepresentativeFunctionType( IType interfaceType )
  {
    IMethodInfo javaMethodInfo = getSingleMethod( interfaceType );
    if( javaMethodInfo != null )
    {
      return GosuShop.createFunctionType( javaMethodInfo );
    }
    else
    {
      return null;
    }
  }

  public static IMethodInfo getSingleMethod( IType interfaceType )
  {
    if( !interfaceType.isInterface() )
    {
      return null;
    }

    List<IMethodInfo> list = new ArrayList<>( interfaceType.getTypeInfo().getMethods() );

    //extract all object methods since they are guaranteed to be implemented
    ITypeInfo objTypeInfo = JavaTypes.OBJECT().getTypeInfo();
    for( Iterator<? extends IMethodInfo> it = list.iterator(); it.hasNext(); )
    {
      IMethodInfo methodInfo = it.next();
      IParameterInfo[] parameterInfos = methodInfo.getParameters();
      IType[] paramTypes = new IType[parameterInfos.length];
      for( int i = 0; i < parameterInfos.length; i++ )
      {
        paramTypes[i] = parameterInfos[i].getFeatureType();
      }
      if( objTypeInfo.getMethod( methodInfo.getDisplayName(), paramTypes ) != null ||
          methodInfo.getOwnersType() instanceof IGosuEnhancement )
      {
        it.remove();
      }
      else if( methodInfo.getOwnersType().getName().contains( IGosuObject.class.getName() ) )
      {
        it.remove();
      }
      else if( !methodInfo.isAbstract() )
      {
        it.remove();
      }
    }

    if( list.size() == 1 )
    {
      IMethodInfo mi = list.get( 0 );
      if( mi instanceof IJavaMethodInfo || mi instanceof IGosuMethodInfo )
      {
        return mi;
      }
    }
    return null;
  }

  public static IJavaClassMethod getSingleMethodFromJavaInterface( IJavaType interfaceType )
  {
    if( !interfaceType.isInterface() )
    {
      return null;
    }

    return getSingleMethodFromJavaInterface( interfaceType.getBackingClassInfo() );
  }

  public static IJavaClassMethod getSingleMethodFromJavaInterface( IJavaClassInfo interfaceType )
  {
    if( !interfaceType.isInterface() )
    {
      return null;
    }

    List<IJavaClassMethod> list = new ArrayList<>( Arrays.asList( interfaceType.getDeclaredMethods() ) );

    // extract all "default" and Object methods
    IJavaClassInfo objTypeInfo = JavaTypes.OBJECT().getBackingClassInfo();
    for( Iterator<? extends IJavaClassMethod> it = list.iterator(); it.hasNext(); )
    {
      IJavaClassMethod method = it.next();
      IJavaClassInfo[] paramTypes = method.getParameterTypes();
      if( hasMethod( objTypeInfo, method.getName(), paramTypes ) )
      {
        it.remove();
      }
      else if( !Modifier.isAbstract( method.getModifiers() ) )
      {
        it.remove();
      }
    }

    if( list.size() == 1 )
    {
      return list.get( 0 );
    }
    return null;
  }

  private static boolean hasMethod( IJavaClassInfo jci, String name, IJavaClassInfo[] params )
  {
    outer: for( IJavaClassMethod method : jci.getDeclaredMethods() )
    {
      if( !method.getName().equals( name ) )
      {
        continue;
      }
      IJavaClassInfo[] methodParamTypes = method.getParameterTypes();
      if( params.length != methodParamTypes.length )
      {
        continue;
      }
      for( int i = 0; i < params.length; i++ )
      {
        if( !params[i].equals( methodParamTypes[i] ) )
        {
          continue outer;
        }
      }
      return true;
    }
    return false;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 2;
  }

  public IType resolveType( IType target, IType source )
  {
    if( TypeSystem.get( IMethodReference.class ).isAssignableFrom( source ) )
    {
      return resolveType( target, source.getTypeParameters()[1] );
    }

    IFunctionType sourceFun = (IFunctionType)source;
    IType returnType = sourceFun.getReturnType();
    IType methodReturnType = extractReturnTypeFromInterface( target );
    if( methodReturnType instanceof ITypeVariableType )
    {
      IType[] paramTypes = target.getTypeParameters();
      IType[] parameterizationTypes = new IType[paramTypes.length];
      for( int i = 0; i < paramTypes.length; i++ )
      {
        IType param = paramTypes[i];
        if( param instanceof ITypeVariableType && param.getName().equals( methodReturnType.getName() ) )
        {
          parameterizationTypes[i] = returnType;
        }
        else
        {
          parameterizationTypes[i] = target.getTypeParameters()[i];
        }
      }
      return target.getParameterizedType( parameterizationTypes );
    }
    else
    {
      return target;
    }
  }

  private IType extractReturnTypeFromInterface( IType target )
  {
    IFunctionType funcType = target.getFunctionalInterface();
    return funcType == null ? null : funcType.getReturnType();
  }

}
