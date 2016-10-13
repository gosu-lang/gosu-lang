package editor.search;

import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodInfoDelegate;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.PropertyInfoDelegate;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaConstructorInfo;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;

import java.util.List;

/**
 */
public class FeatureUtil
{
  static IConstructorInfo findRootConstructorInfo( IConstructorInfo ci )
  {
    IType ownersType = ci.getOwnersType();
    IType genType = getGenericType( ownersType );
    if( genType != ownersType )
    {
      if( ci instanceof IGosuConstructorInfo )
      {
        IReducedDynamicFunctionSymbol dfs = ((IGosuConstructorInfo)ci).getDfs();
        if( dfs != null )
        {
          IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
          while( backingDfs != null && backingDfs != dfs )
          {
            ci = (IConstructorInfo)backingDfs.getMethodOrConstructorInfo();
            dfs = backingDfs;
            backingDfs = dfs.getBackingDfs();
          }
        }
      }
      else if( ci instanceof IJavaConstructorInfo )
      {
        return findConstructor( (IJavaType)genType, ((IJavaConstructorInfo)ci).getJavaConstructor() );
      }
    }
    return ci;
  }

  static IPropertyInfo findRootPropertyInfo( IPropertyInfo pi )
  {
    while( pi instanceof PropertyInfoDelegate )
    {
      pi = ((PropertyInfoDelegate)pi).getSource();
    }

    IType ownersType = pi.getOwnersType();
    pi = findRootPropertyInfo( ownersType, pi );
    ownersType = pi.getOwnersType();
    IType genType = getGenericType( ownersType );
    if( genType != ownersType )
    {
      ITypeInfo ti = genType.getTypeInfo();
      if( ti instanceof IRelativeTypeInfo )
      {
        pi = ((IRelativeTypeInfo)ti).getProperty( genType, pi.getDisplayName() );
      }
      else
      {
        pi = ti.getProperty( pi.getDisplayName() );
      }
    }
    return pi;
  }

  static IMethodInfo findRootMethodInfo( IMethodInfo mi )
  {
    while( mi instanceof MethodInfoDelegate )
    {
      mi = ((MethodInfoDelegate)mi).getSource();
    }

    IType ownersType = mi.getOwnersType();
    mi = findRootMethodInfo( ownersType, mi );
    ownersType = mi.getOwnersType();
    IType genType = getGenericType( ownersType );
    if( genType != ownersType )
    {
      if( mi instanceof IGosuMethodInfo )
      {
        IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo)mi).getDfs();
        if( dfs != null )
        {
          IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
          while( backingDfs != null && backingDfs != dfs )
          {
            mi = (IMethodInfo)backingDfs.getMethodOrConstructorInfo();
            dfs = backingDfs;
            backingDfs = dfs.getBackingDfs();
          }
        }
      }
      else if( mi instanceof IJavaMethodInfo )
      {
        return findMethod( (IJavaType)genType, ((IJavaMethodInfo)mi).getMethod() );
      }
    }
    return mi;
  }

  private static IMethodInfo findMethod( IJavaType genType, IJavaClassMethod method )
  {
    MethodList methods = ((IRelativeTypeInfo)genType.getTypeInfo()).getMethods( genType );
    for( IMethodInfo mi: methods.getMethods( method.getName() ) )
    {
      if( mi instanceof IJavaMethodInfo && ((IJavaMethodInfo)mi).getMethod() == method )
      {
        return mi;
      }
    }
    return null;
  }

  private static IConstructorInfo findConstructor( IJavaType genType, IJavaClassConstructor ctor )
  {
    List<? extends IConstructorInfo> constructors = ((IRelativeTypeInfo)genType.getTypeInfo()).getConstructors( genType );
    for( IConstructorInfo ci : constructors )
    {
      if( ci instanceof IJavaConstructorInfo && ci.getConstructor() == ctor )
      {
        return ci;
      }
    }
    return null;
  }

  private static IPropertyInfo findRootPropertyInfo( IType genType, IPropertyInfo pi )
  {
    IType supertype = genType.getSupertype();
    if( supertype != null )
    {
      ITypeInfo ti = supertype.getTypeInfo();
      IPropertyInfo superPi;
      if( ti instanceof IRelativeTypeInfo )
      {
        superPi = ((IRelativeTypeInfo)ti).getProperty( genType, pi.getDisplayName() );
      }
      else
      {
        superPi = ti.getProperty( pi.getDisplayName() );

      }
      if( superPi != null )
      {
        return findRootPropertyInfo( supertype, superPi );
      }
    }
    for( IType iface: genType.getInterfaces() )
    {
      ITypeInfo ti = iface.getTypeInfo();
      IPropertyInfo ifacePi;
      if( ti instanceof IRelativeTypeInfo )
      {
        ifacePi = ((IRelativeTypeInfo)ti).getProperty( iface, pi.getDisplayName() );
      }
      else
      {
        ifacePi = ti.getProperty( pi.getDisplayName() );

      }
      if( ifacePi != null )
      {
        return findRootPropertyInfo( iface, ifacePi );
      }
    }
    return pi;
  }

  private static IMethodInfo findRootMethodInfo( IType genType, IMethodInfo mi )
  {
    IType supertype = genType.getSupertype();
    if( supertype != null )
    {
      ITypeInfo ti = supertype.getTypeInfo();
      IMethodInfo superMi;
      if( ti instanceof IRelativeTypeInfo )
      {
        superMi = ((IRelativeTypeInfo)ti).getMethod( genType, mi.getDisplayName(), getParamTypes( mi.getParameters() ) );
      }
      else
      {
        superMi = ti.getMethod( mi.getDisplayName(), getParamTypes( mi.getParameters() ) );

      }
      if( superMi != null )
      {
        return findRootMethodInfo( supertype, superMi );
      }
    }
    for( IType iface: genType.getInterfaces() )
    {
      ITypeInfo ti = iface.getTypeInfo();
      IMethodInfo ifacePi;
      if( ti instanceof IRelativeTypeInfo )
      {
        ifacePi = ((IRelativeTypeInfo)ti).getMethod( iface, mi.getDisplayName(), getParamTypes( mi.getParameters() ) );
      }
      else
      {
        ifacePi = ti.getMethod( mi.getDisplayName(), getParamTypes( mi.getParameters() ) );

      }
      if( ifacePi != null )
      {
        return findRootMethodInfo( iface, ifacePi );
      }
    }
    return mi;
  }

  static boolean methodInfosEqual( IMethodInfo mi, IMethodInfo targetMi )
  {
    if( mi == targetMi )
    {
      return true;
    }

    String name = mi.getDisplayName();
    if( name == null || !name.equals( targetMi.getDisplayName() ) )
    {
      return false;
    }

    IType targetType = targetMi.getOwnersType();
    if( !targetType.isAssignableFrom( mi.getOwnersType() ) )
    {
      return false;
    }

    IParameterInfo[] miParams = mi.getParameters();
    IParameterInfo[] targetMiParams = targetMi.getParameters();
    if( miParams.length != targetMiParams.length )
    {
      return false;
    }

    if( miParams.length == 0 )
    {
      return true;
    }

    IType ownersType = mi.getOwnersType();
    ITypeInfo typeInfo = ownersType.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      // get the latest version if it could have been modified in Lab (a Gosu class)
      mi = ((IRelativeTypeInfo)typeInfo).getMethod( mi.getOwnersType(), mi.getDisplayName(), getParamTypes( mi.getParameters() ) );

      if( mi == targetMi )
      {
        return true;
      }
    }
    return false;
  }

  static boolean constructorInfosEqual( IConstructorInfo ci, IConstructorInfo targetCi )
  {
    if( ci == targetCi )
    {
      return true;
    }

    IType targetType = targetCi.getOwnersType();
    if( !targetType.isAssignableFrom( ci.getOwnersType() ) )
    {
      return false;
    }

    IParameterInfo[] miParams = ci.getParameters();
    IParameterInfo[] targetMiParams = targetCi.getParameters();
    if( miParams.length != targetMiParams.length )
    {
      return false;
    }

    if( miParams.length == 0 )
    {
      return true;
    }

    IType ownersType = ci.getOwnersType();
    ITypeInfo typeInfo = ownersType.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      // get the latest version if it could have been modified in Lab (a Gosu class)
      ci = ((IRelativeTypeInfo)typeInfo).getConstructor( ci.getOwnersType(), getParamTypes( ci.getParameters() ) );

      if( ci == targetCi )
      {
        return true;
      }
    }
    return false;
  }

  public static IType[] getParamTypes( IParameterInfo[] paramInfos )
  {
    IType[] argTypes = new IType[paramInfos.length];
    for( int i = 0; i < paramInfos.length; i++ )
    {
      IParameterInfo iParameterInfo = paramInfos[i];
      argTypes[i] = iParameterInfo.getFeatureType();
    }
    return argTypes;
  }

  static IType getGenericType( IType ownersType )
  {
    if( ownersType.isParameterizedType() )
    {
      return ownersType.getGenericType();
    }
    return ownersType;
  }
}
