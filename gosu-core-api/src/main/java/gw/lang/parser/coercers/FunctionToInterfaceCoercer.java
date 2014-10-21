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
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
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
    if( value instanceof IBlock )
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
    if( interfaceType.isInterface() && (interfaceType instanceof IJavaType || interfaceType instanceof IGosuClass) )
    {
      List<IMethodInfo> list = new ArrayList<IMethodInfo>( interfaceType.getTypeInfo().getMethods() );

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
      }

      if( list.size() == 1 )
      {
        IMethodInfo mi = list.get( 0 );
        if( mi instanceof IJavaMethodInfo || mi instanceof IGosuMethodInfo )
        {
          return mi;
        }
      }
    }
    return null;
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
    IFunctionType sourceFun = (IFunctionType)source;
    IType returnType = sourceFun.getReturnType();
    IType methodReturnType = extractReturnTypeFromInterface( target );
    if( methodReturnType instanceof ITypeVariableType )
    {
      IGenericTypeVariable[] typeVariables = target.getGenericTypeVariables();
      IType[] parameterizationTypes = new IType[typeVariables.length];
      for( int i = 0; i < typeVariables.length; i++ )
      {
        IGenericTypeVariable typeVariable = typeVariables[i];
        if( typeVariable.getName().equals( methodReturnType.getName() ) )
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
    for( IMethodInfo methodInfo : target.getTypeInfo().getMethods() )
    {
      if( methodInfo.getOwnersType().equals( target ) )
      {
        return methodInfo.getReturnType();
      }
    }
    return null;
  }

}
