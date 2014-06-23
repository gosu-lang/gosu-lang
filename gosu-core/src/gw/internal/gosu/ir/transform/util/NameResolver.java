/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.internal.gosu.parser.*;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.parser.Keyword;
import gw.lang.reflect.java.JavaTypes;


public class NameResolver {

  public static String getGetterName( IPropertyInfo pi )
  {
    if( pi instanceof GosuPropertyInfo)
    {
      //## todo: in parser make compilation error for case where a getFoo method is defined in the presence of a Foo property
      ReducedDynamicPropertySymbol dps = ((GosuPropertyInfo)pi).getDps();
      return getGetterNameForDPS( dps );
    }
    else if( pi.getClass() == JavaPropertyInfo.class )
    {
      return ((JavaPropertyInfo)pi).getPropertyDescriptor().getReadMethod().getName();
    }
    else if( pi instanceof PropertyInfoBuilder.BuiltPropertyInfo )
    {
      if( pi.getAccessor() instanceof JavaPropertyInfo.PropertyAccessorAdaptor )
      {
        return ((JavaPropertyInfo.PropertyAccessorAdaptor)pi.getAccessor()).getGetterMethod().getName();
      }
      else
      {
        PropertyInfoBuilder.BuiltPropertyInfo bpi = (PropertyInfoBuilder.BuiltPropertyInfo)pi;
        if( bpi.getJavaMethodName() !=  null )
        {
          return bpi.getJavaMethodName();
        }
        return "get" + bpi.getName();
      }
    }
    else if( pi instanceof IPropertyInfoDelegate)
    {
      return getGetterName( ((IPropertyInfoDelegate)pi).getSource() );
    }
    throw new IllegalArgumentException( "Unexpected property type: " + pi.getClass() );
  }

  public static String getGetterNameForDPS( ReducedDynamicPropertySymbol dps )
  {
    return getFunctionName(dps.getGetterDfs());
  }

  public static String getGetterNameForDPS( DynamicPropertySymbol dps )
  {
    return getFunctionName(dps.getGetterDfs());
  }

  public static String getSetterName( IPropertyInfo pi )
  {
    if( pi instanceof GosuPropertyInfo)
    {
      //## todo: in parser make compilation error for case where a getFoo method is defined in the presence of a Foo property
      ReducedDynamicPropertySymbol dps = ((GosuPropertyInfo)pi).getDps();
      return getSetterNameForDPS( dps );
    }
    else if( pi.getClass() == JavaPropertyInfo.class )
    {
      return ((JavaPropertyInfo)pi).getPropertyDescriptor().getWriteMethod().getName();
    }
    else if( pi instanceof PropertyInfoBuilder.BuiltPropertyInfo )
    {
      if( pi.getAccessor() instanceof JavaPropertyInfo.PropertyAccessorAdaptor )
      {
        return ((JavaPropertyInfo.PropertyAccessorAdaptor)pi.getAccessor()).getSetterMethod().getName();
      }
      else
      {
        PropertyInfoBuilder.BuiltPropertyInfo bpi = (PropertyInfoBuilder.BuiltPropertyInfo)pi;
        if( bpi.getJavaMethodName() !=  null )
        {
          return bpi.getJavaMethodName();
        }
        return "set" + bpi.getName();
      }
    }
    else if( pi instanceof IPropertyInfoDelegate)
    {
      return getSetterName( ((IPropertyInfoDelegate)pi).getSource() );
    }
    throw new IllegalArgumentException( "Unexpected property type: " + pi.getClass() );
  }

  public static String getSetterNameForDPS( ReducedDynamicPropertySymbol dps )
  {
    return getFunctionName( dps.getSetterDfs() );
  }

  public static String getSetterNameForDPS( DynamicPropertySymbol dps )
  {
    return getFunctionName( dps.getSetterDfs() );
  }

  public static String getFunctionName( ReducedDynamicFunctionSymbol dfs )
  {
    String strName = dfs.getDisplayName();
    if( strName.startsWith( "@" ) )
    {
      final IType returnType = dfs.getReturnType();
      if( returnType == JavaTypes.pVOID() )
      {
        strName = Keyword.KW_set + strName.substring( 1 );
      }
      else
      {
        strName = resolveCorrectGetterMethodPrefixForBooleanDFS( dfs ) + strName.substring( 1 );
      }
    }
    return strName;
  }

  public static String getFunctionName( DynamicFunctionSymbol dfs )
  {
    String strName = dfs.getDisplayName();
    if( strName.startsWith( "@" ) )
    {
      final IType returnType = dfs.getReturnType();
      if( returnType == JavaTypes.pVOID() )
      {
        strName = Keyword.KW_set + strName.substring( 1 );
      }
      else
      {
        strName = resolveCorrectGetterMethodPrefixForBooleanDFS( dfs ) + strName.substring( 1 );
      }
    }
    return strName;
  }

  private static String resolveCorrectGetterMethodPrefixForBooleanDFS( IDynamicFunctionSymbol dfs )
  {
    while( dfs.getBackingDfs() != null && dfs.getBackingDfs() != dfs )
    {
      dfs = dfs.getBackingDfs();
    }
    IType returnType = dfs.getReturnType();
    if (returnType == JavaTypes.pBOOLEAN() || returnType == JavaTypes.BOOLEAN()) {
      IDynamicFunctionSymbol rootDFS = dfs;
      while (rootDFS.getSuperDfs() != null) {
        rootDFS = rootDFS.getSuperDfs();
        while( rootDFS.getBackingDfs() != null && rootDFS.getBackingDfs() != rootDFS )
        {
          rootDFS = rootDFS.getBackingDfs();
        }
      }

      if (rootDFS.getGosuClass() != null &&
          IGosuClass.ProxyUtil.isProxy(rootDFS.getGosuClass())) {
        IType proxiedType = IGosuClass.ProxyUtil.getProxiedType(rootDFS.getGosuClass());
        IPropertyInfo propertyInfo = proxiedType.getTypeInfo().getProperty(dfs.getDisplayName().substring(1));
        if (propertyInfo instanceof IJavaPropertyInfo) {
          IJavaPropertyDescriptor propertyDescriptor = ((IJavaPropertyInfo) propertyInfo).getPropertyDescriptor();
          IJavaClassMethod readMethod = propertyDescriptor.getReadMethod();
          if (readMethod != null) {
            if (readMethod.getName().startsWith("is")) {
              return "is";
            } else {
              assert(readMethod.getName().startsWith("get"));
              return "get";
            }
          }
        }
      }

      // default to "is" for other boolean properties
      return "is";
    } else {
      // Everything else defaults to "get"
      return "get";
    }
  }

  private static String resolveCorrectGetterMethodPrefixForBooleanDFS( IReducedDynamicFunctionSymbol dfs )
  {
    while( dfs.getBackingDfs() != null && dfs.getBackingDfs() != dfs )
    {
      dfs = dfs.getBackingDfs();
    }
    IType returnType = dfs.getReturnType();
    if (returnType == JavaTypes.pBOOLEAN() || returnType == JavaTypes.BOOLEAN()) {
      IReducedDynamicFunctionSymbol rootDFS = dfs;
      while (rootDFS.getSuperDfs() != null) {
        rootDFS = rootDFS.getSuperDfs();
      }

      if (rootDFS.getGosuClass() != null &&
          IGosuClass.ProxyUtil.isProxy(rootDFS.getGosuClass())) {
        IType proxiedType = IGosuClass.ProxyUtil.getProxiedType(rootDFS.getGosuClass());
        IPropertyInfo propertyInfo = proxiedType.getTypeInfo().getProperty(dfs.getDisplayName().substring(1));
        if (propertyInfo instanceof IJavaPropertyInfo) {
          IJavaPropertyDescriptor propertyDescriptor = ((IJavaPropertyInfo) propertyInfo).getPropertyDescriptor();
          IJavaClassMethod readMethod = propertyDescriptor.getReadMethod();
          if (readMethod != null) {
            if (readMethod.getName().startsWith("is")) {
              return "is";
            } else {
              assert(readMethod.getName().startsWith("get"));
              return "get";
            }
          }
        }
      }

      // default to "is" for other boolean properties
      return "is";
    } else {
      // Everything else defaults to "get"
      return "get";
    }
  }
}
