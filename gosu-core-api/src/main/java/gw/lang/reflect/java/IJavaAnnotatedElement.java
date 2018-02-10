/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IAnnotationInfo;

import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.LocationInfo;
import gw.lang.reflect.SourcePosition;
import gw.util.GosuExceptionUtil;
import java.lang.annotation.Annotation;
import java.net.URL;

public interface IJavaAnnotatedElement {

  /**
   * Returns true if an annotation for the specified type
   * is present on this element, else false.  This method
   * is designed primarily for convenient access to marker annotations.
   *
   * @param annotationClass the Class object corresponding to the
   *                        annotation type
   * @return true if an annotation for the specified annotation
   *         type is present on this element, else false
   * @throws NullPointerException if the given annotation class is null
   * @since 1.5
   */
  boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

  /**
   * Returns this element's annotation for the specified type if
   * such an annotation is present, else null.
   *
   * @param annotationClass the Class object corresponding to the
   *                        annotation type
   * @return this element's annotation for the specified annotation type if
   *         present on this element, else null
   * @throws NullPointerException if the given annotation class is null
   * @since 1.5
   */
  IAnnotationInfo getAnnotation(Class<? extends Annotation> annotationClass);

  /**
   * Returns all annotations that are directly present on this
   * element.  Unlike the other methods in this interface, this method
   * ignores inherited annotations.  (Returns an array of length zero if
   * no annotations are directly present on this element.)  The caller of
   * this method is free to modify the returned array; it will have no
   * effect on the arrays returned to other callers.
   *
   * @return All annotations directly present on this element
   * @since 1.5
   */
  IAnnotationInfo[] getDeclaredAnnotations();


  IJavaClassInfo getEnclosingClass();

  default ILocationInfo getLocationInfo()
  {
    IAnnotationInfo anno = getAnnotation( SourcePosition.class );
    if( anno != null )
    {
      try
      {
        return new LocationInfo( ((Integer)anno.getFieldValue( "offset" )).intValue(),
                                 ((Integer)anno.getFieldValue( "length" )).intValue(), -1, -1,
                                 new URL( (String)anno.getFieldValue( "url" ) ) );
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
    return ILocationInfo.EMPTY;
  }

}
