/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.GosuShop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;

public class FunctionFromInterfaceCoercer extends BaseCoercer
{
  private static FunctionFromInterfaceCoercer _instance = new FunctionFromInterfaceCoercer();

  public static FunctionFromInterfaceCoercer instance()
  {
    return _instance;
  }

  private FunctionFromInterfaceCoercer() {}

  public boolean handlesNull()
  {
    return false;
  }

  public Object coerceValue( IType typeToCoerceTo, final Object value )
  {
    return value; // Ideally I'd throw here, but there are places that rely on this working
  }

  @SuppressWarnings("UnusedDeclaration")
  public static Object doCoercion( Class classToCoerceTo, Class ifaceClass, final Object value )
  {
    final Method abstractMethod = findSingleAbstractMethod( ifaceClass );
    return Proxy.newProxyInstance( classToCoerceTo.getClassLoader(), new Class[]{classToCoerceTo},
     ( proxy, method, args ) -> {
       if( method.getName().startsWith( "invoke" ) )
       {
         return abstractMethod.invoke( value, args );
       }
       else
       {
         return method.invoke( value, args );
       }
     } );
  }

  private static Method findSingleAbstractMethod( Class ifaceClass )
  {
    if( ifaceClass.isInterface() )
    {
      for( Method m: ifaceClass.getDeclaredMethods() )
      {
        // Java 8 allows for "default" interface methods, so we look for the abstract one specifically
        if( Modifier.isAbstract( m.getModifiers() ) )
        {
          return m;
        }
      }
    }
    else
    {
      for( Class iface: ifaceClass.getInterfaces() )
      {
        if( iface.getName().equals( IGosuObject.class.getName() ) )
        {
          continue;
        }
        for( Method m: iface.getDeclaredMethods() )
        {
          // Java 8 allows for "default" interface methods, so we look for the abstract one specifically
          if( Modifier.isAbstract( m.getModifiers() ) )
          {
            return m;
          }
        }
      }
    }
    throw new IllegalStateException( "No abstract interface method found for " + ifaceClass );
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public static boolean areTypesCompatible( IFunctionType functionType, IType interfaceType )
  {
    if( interfaceType.isInterface() && interfaceType instanceof IJavaType)
    {
      IJavaType javaIntrinsicType = (IJavaType)interfaceType;
      List<? extends IMethodInfo> list = javaIntrinsicType.getTypeInfo().getMethods();
      int nonObjectMethods = 0;
      IMethodInfo singleMethod = null;
      for( IMethodInfo iMethodInfo : list )
      {
        if( !iMethodInfo.getOwnersType().equals( JavaTypes.OBJECT() ) && iMethodInfo instanceof IJavaMethodInfo )
        {
          nonObjectMethods++;
          singleMethod = iMethodInfo;
        }
      }
      if( nonObjectMethods == 1 )
      {
        IFunctionType tempFunctionType = GosuShop.createFunctionType( singleMethod );
        return functionType.isAssignableFrom( tempFunctionType );
      }
    }
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 2;
  }
}