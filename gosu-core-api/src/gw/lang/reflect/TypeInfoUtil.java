/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.IGenericTypeVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

public class TypeInfoUtil
{
  public static <S extends CharSequence, T extends IFeatureInfo> List<T> makeSortedUnmodifiableRandomAccessListFromFeatures( Map<S, T> map )
  {
    if( map.isEmpty() )
    {
      return Collections.emptyList();
    }

    Collection<T> values = map.values();
    return makeSortedUnmodifiableRandomAccessList( values );
  }

  public static <T extends IFeatureInfo> List<T> makeSortedUnmodifiableRandomAccessList( Collection<T> collection )
  {
    if( collection.isEmpty() )
    {
      return Collections.emptyList();
    }

    List<T> list = collection instanceof List && collection instanceof RandomAccess
                   ? (List<T>)collection
                   : new ArrayList<T>( collection );
    sortByName( list );

    return Collections.unmodifiableList( list );
  }

  public static MethodList makeSortedUnmodifiableRandomAccessList( MethodList collection )
  {
    if( collection.isEmpty() )
    {
      return MethodList.EMPTY;
    }

    List<IMethodInfo> list = new ArrayList<IMethodInfo>(collection);
    sortByName( list );

    return new MethodList(list);
  }

  public static <T extends IFeatureInfo> void sortByName( List<T> featureInfos )
  {
    if( featureInfos.isEmpty() )
    {
      return;
    }

    Collections.sort( featureInfos,
                      new Comparator<IFeatureInfo>()
                      {
                        public int compare( IFeatureInfo o1, IFeatureInfo o2 )
                        {
                          return o1.getName().compareToIgnoreCase( o2.getName() );
                        }
                      } );
  }

  public static String getMethodSignature( IMethodInfo mi )
  {
    IFunctionType type = TypeSystem.getOrCreateFunctionType( mi );
    return mi.getDisplayName() + getParameterDisplay( mi ) + " : " + getTypeName( type.getReturnType() );
  }

  public static String getConstructorSignature( IConstructorInfo constructorInfo )
  {
    return constructorInfo.getDisplayName() + getParameterDisplay( constructorInfo );
  }

  public static String getParameterDisplay( IMethodInfo mi )
  {
    IParameterInfo[] pd = mi.getParameters();
    return getParameterDisplay( pd );
  }

  public static String getParameterDisplay( IConstructorInfo ci )
  {
    IParameterInfo[] pd = ci.getParameters();
    return getParameterDisplay( pd );
  }

  private static String getParameterDisplay( IParameterInfo[] pds )
  {
    if( pds == null || pds.length == 0 )
    {
      return "()";
    }

    String strParams = "(";
    for( int i = 0; i < pds.length; i++ )
    {
      strParams += (i == 0 ? "" : ", ") + pds[i].getFeatureType().getName();
    }
    strParams += ")";

    return strParams;
  }

  public static String getTypeVarList( IFeatureInfo fi )
  {
    return getTypeVarList( fi, false );
  }

  public static String getTypeVarList( IFeatureInfo fi, boolean bRelative )
  {
    if( fi instanceof IGenericMethodInfo )
    {
      StringBuilder vars = new StringBuilder();
      int i = 0;
      for( IGenericTypeVariable tv : ((IGenericMethodInfo)fi).getTypeVariables() )
      {
        vars.append( i++ > 0 ? ',' : '<' );
        vars.append( tv.getNameWithBounds( bRelative ) );
      }
      if( i > 0 )
      {
        return vars.append( '>' ).toString();
      }
    }
    return "";
  }

  public static String getTypeVarListNoBounds( IFeatureInfo fi )
  {
    if( fi instanceof IGenericMethodInfo )
    {
      StringBuilder vars = new StringBuilder();
      int i = 0;
      for( IGenericTypeVariable tv : ((IGenericMethodInfo)fi).getTypeVariables() )
      {
        vars.append( i++ > 0 ? ',' : '<' );
        vars.append( tv.getName() );
      }
      if( i > 0 )
      {
        return vars.append( '>' ).toString();
      }
    }
    return "";
  }

  public static String getTypeName( IType type )
  {
    String strType;
    if( type.isArray() )
    {
      strType = getTypeName( type.getComponentType() ) + "[]";
    }
    else
    {
      strType = type.getRelativeName();
    }

    return strType;
  }

  public static String normalizePackageName( String packageName ) {
    StringBuilder sb = new StringBuilder();
    String[] parts = packageName.split( "\\." );
    for ( String part : parts ) {
      if ( sb.length() > 0 ) {
        sb.append( '.' );
      }
      sb.append( normalizeGosuIdentifier( part ) );
    }
    return sb.toString();
  }

  public static String normalizeGosuIdentifier( String propertyName, Set<String> usedPropertyNames, boolean caseSensitive ) {
    propertyName = normalizeGosuIdentifier( propertyName );
    int suffix = 2;
    String newPropertyName = propertyName;
    while ( ! usedPropertyNames.add( caseSensitive ? newPropertyName : newPropertyName.toLowerCase() ) ) {
      newPropertyName = propertyName + suffix++;
    }
    return newPropertyName;
  }

  public static String normalizeGosuIdentifier( String name ) {
    StringBuilder sb = new StringBuilder();
    for ( int i = 0; i < name.length(); i++ ) {
      char ch = name.charAt( i );
      if ( i == 0 ) {
        // some existing properties are composed solely of numbers, added isDigit() since they don't seem to be
        // syntactically invalid in Gosu, and are actively being used by CC.
        if ( Character.isJavaIdentifierStart( ch ) || Character.isDigit( ch ) ) {
          // use as-is
          sb.append( ch );
        }
        else if ( Character.isJavaIdentifierPart( ch ) ) {
          // prefix with "_"
          sb.append( '_' );
          sb.append( ch );
        }
        else {
          // replace with "_"
          sb.append( '_' );
        }
      }
      else if ( Character.isJavaIdentifierPart( ch ) ) {
        // use as-is
        sb.append( ch );
      }
      else {
        // replace with "_"
        sb.append( '_' );
      }
    }
    name = sb.toString();
    if ( name.length() == 0 ) {
      name = "_";
    }
    return name;
  }

}
