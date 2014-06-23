/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuMethodParamInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;
import java.util.ArrayList;

@Deprecated
public class AnnotationReflectUtil
{
  public static String evalDeprecationReason( IAnnotatedFeatureInfo featureInfo )
  {
    if( isGosuFeature( featureInfo ) )
    {
      String deprecatedReason = "";
      List<IAnnotationInfo> annotation = featureInfo.getAnnotationsOfType( JavaTypes.GW_LANG_DEPRECATED() );
      if( annotation.size() > 0 )
      {
        try
        {
          IAnnotationInfo annotationInfo = annotation.get( 0 );
          if( annotationInfo != null )
          {
            deprecatedReason = (String)annotationInfo.getFieldValue("value");
          }
        }
        catch( Exception e )
        {
          //ignore
        }
      }
      return deprecatedReason;
    }
    else
    {
      return featureInfo.getDeprecatedReason();
    }
  }

  public static List<IExceptionInfo> evalThrowsInfos( IAnnotatedFeatureInfo typeInfo )
  {
    ArrayList<IExceptionInfo> lst = new ArrayList<IExceptionInfo>();
    for( IAnnotationInfo aThrows : evalThrows( typeInfo ) )
    {
      lst.add( new SyntheticExceptionInfo( typeInfo, ((IType)aThrows.getFieldValue( "ExceptionType" )).getName(), (String)aThrows.getFieldValue( "ExceptionDescription" ) ) );
    }
    return lst;
  }

  private static List<IAnnotationInfo> evalThrows( IAnnotatedFeatureInfo typeInfo )
  {
    List<IAnnotationInfo> throwsLst = new ArrayList<IAnnotationInfo>();
    try
    {
      for( IAnnotationInfo annotationInfo : typeInfo.getAnnotationsOfType( JavaTypes.THROWS() ) )
      {
        throwsLst.add( annotationInfo );
      }
    }
    catch( Exception e )
    {
      //ignore
    }
    return throwsLst;
  }

  public static String evalReturnDescription( IMethodInfo method )
  {
    if( method instanceof IGosuMethodInfo )
    {
      List<IAnnotationInfo> annotation = method.getAnnotationsOfType( JavaTypes.getGosuType( gw.lang.Returns.class ) );
      if( annotation.size() > 0 )
      {
        return (String)annotation.get( 0 ).getFieldValue("value");
      }
    }
    else
    {
      return method.getReturnDescription();
    }
    return "";
  }

  public static boolean evalVisibleForWebservice( IMethodInfo method )
  {
    // equivelent to method.isVisible( ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE)
    return !evalIsHidden(method);
  }

  public static boolean evalIsHidden( IAnnotatedFeatureInfo featureInfo )
  {
    // equivelent to method.isVisible( ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE)
    return !featureInfo.getAnnotationsOfType( JavaTypes.INTERNAL_API() ).isEmpty();
  }

  public static String evalParameterDecription( IParameterInfo parameterData )
  {
    // replicates logic in gw.internal.gosu.parser.GosuMethodParamInfo._description
    if( parameterData instanceof IGosuMethodParamInfo )
    {
      IFeatureInfo featureInfo = parameterData.getContainer();
      IAttributedFeatureInfo annotatedFI = (IAttributedFeatureInfo)featureInfo;
      List<IAnnotationInfo> annotation = annotatedFI.getAnnotationsOfType(JavaTypes.PARAM());
      for( IAnnotationInfo o : annotation )
      {
        if( o != null && o.getFieldValue( "FieldName" ).equals( parameterData.getName() ) )
        {
          return (String)o.getFieldValue( "FieldDescription" );
        }
      }
      return "";
    }
    else
    {
      return parameterData.getDescription();
    }
  }

  private static boolean isGosuFeature( IAnnotatedFeatureInfo featureInfo )
  {
    return featureInfo instanceof IGosuMethodInfo ||
           featureInfo instanceof IGosuPropertyInfo ||
           featureInfo instanceof IGosuConstructorInfo ||
           featureInfo instanceof IGosuClassTypeInfo;
  }

  public static class SyntheticExceptionInfo implements IExceptionInfo
  {
    private IFeatureInfo _container;
    private String _exceptionName;
    private String _exceptionDescription;

    public SyntheticExceptionInfo( IFeatureInfo container, String exceptionName, String exceptionDescription )
    {
      _container = container;
      _exceptionName = exceptionName;
      _exceptionDescription = exceptionDescription;
    }

    public IFeatureInfo getContainer()
    {
      return _container;
    }

    public IType getOwnersType()
    {
      return _container.getOwnersType();
    }

    public String getName()
    {
      return _exceptionName;
    }

    public String getDisplayName()
    {
      return _exceptionName;
    }

    public String getDescription()
    {
      return _exceptionDescription;
    }

    public IType getExceptionType()
    {
      return TypeSystem.getByFullNameIfValid( getName() );
    }
  }
}
