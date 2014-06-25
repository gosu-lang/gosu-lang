/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuObjectUtil;

import java.util.List;
import java.util.ArrayList;

public interface IAnnotatedFeatureInfo extends IFeatureInfo
{
  IAnnotationInfoHelper ANNOTATION_HELPER = new IAnnotationInfoHelper();

  /**
   * @return A map of AnnotationInfo objects by type representing all the annotations associated
   *         with this feature and all the features in its ancestry, or an empty map if there are no annotations.
   */
  public List<IAnnotationInfo> getAnnotations();

  /**
   * @return A map of AnnotationInfo objects by type representing all the annotations associated
   *         with this feature, or an empty map if there are no annotations.
   */
  public List<IAnnotationInfo> getDeclaredAnnotations();

  /**
   * @return A list of AnnotationInfo objects having the specified type.
   */
  public List<IAnnotationInfo> getAnnotationsOfType( IType type );

  /**
   * @return An AnnotationInfo object having the specified type, or null if no such AnnotationInfo exists.
   *
   * @throws IllegalStateException if more than one AnnotationInfo of the given type exists
   */
  public IAnnotationInfo getAnnotation( IType type );

  /**
   * @param  type The type of the annotation to test for existence.
   *
   * @return true If this feature has an annotation with of the given type, false otherwise
   */
  public boolean hasAnnotation( IType type );

  /**
   * @param  type The type of the annotation to test for existence.
   *
   * @return true If this feature has a declared annotation of the given type, false otherwise
   */
  public boolean hasDeclaredAnnotation( IType type );

  /**
   * @return True if this feature is deprecated.
   */
  public boolean isDeprecated();

  /**
   * @return The deprecation message, or null if the feature is not deprecated.
   */
  public String getDeprecatedReason();

  /**
   * @return True if this feature is the default implementation e.g., default interface method
   */
  boolean isDefaultImpl();

  class IAnnotationInfoHelper {
    private IAnnotationInfoHelper(){}

    public IAnnotationInfo getAnnotation( IType type, List<IAnnotationInfo> annotations, String featureName )
    {
      IAnnotationInfo result = null;
      for( IAnnotationInfo annotation : annotations )
      {
        if( GosuObjectUtil.equals( annotation.getType(), type ) )
        {
          if( result != null )
          {
            throw new IllegalStateException( "More than one instance of the annotation " + type + " exists!" );
          }
          else
          {
            result = annotation;
          }
        }
      }
      return result;
    }

    public boolean hasAnnotation( IType type, List<IAnnotationInfo> annotations )
    {
      for( IAnnotationInfo annotation : annotations )
      {
        if( GosuObjectUtil.equals( annotation.getType(), type ) )
        {
          return true;
        }
      }
      return false;
    }

    public List<IAnnotationInfo> getAnnotationsOfType( IType type, List<IAnnotationInfo> annotations )
    {
      ArrayList<IAnnotationInfo> result = new ArrayList<IAnnotationInfo>();
      for( IAnnotationInfo annotationInfo : annotations )
      {
        if( type.equals( annotationInfo.getType() ) )
        {
          result.add( annotationInfo );
        }
      }
      return result;
    }

    public boolean isInherited( IType type )
    {
      if (type == null) {
        return false;
      }
      return JavaTypes.IINHERITED().isAssignableFrom( type ) ||
             type.getTypeInfo().hasDeclaredAnnotation( JavaTypes.INHERITED() );
    }

    public boolean shouldAddInheritedAnnotation( IFeatureInfo fi, List<IAnnotationInfo> annotations, IAnnotationInfo annotationInfo )
    {
      for( IAnnotationInfo annotation : annotations )
      {
        if( annotation.getType().equals( annotationInfo.getType() ) )
        {
          return CommonServices.getGosuIndustrialPark().isAnnotationAllowedMultipleTimes( fi, annotationInfo );
        }
      }
      return true;
    }
  }
}
