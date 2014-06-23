/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.annotations;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.annotation.Annotations;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.lang.annotation.Annotation;

public class AnnotationMap
{
  private Map<String, List> _annotationsByFeatureName = new HashMap<String, List>();
  private List _currentAnnotationList;
  private Annotations.Builder _currentJavaAnnotationBuilder;
  private IJavaType _currentJavaType;

  public AnnotationMap startAnnotationInfoForFeature( String featureId )
  {
    _currentAnnotationList = new ArrayList();
    _annotationsByFeatureName.put( featureId, _currentAnnotationList );
    return this;
  }

  public AnnotationMap startJavaAnnotation( IType type )
  {
    _currentJavaType = (IJavaType)type;
    Class annotationClass = _currentJavaType.getBackingClass();
    _currentJavaAnnotationBuilder = Annotations.builder( annotationClass );
    return this;
  }

  public AnnotationMap withArg( String name, Object arg )
  {
    _currentJavaAnnotationBuilder.withElement( name, arg );
    return this;
  }

  public AnnotationMap finishJavaAnnotation()
  {
    Annotation annotation = _currentJavaAnnotationBuilder.create();
    _currentAnnotationList.add( annotation );
    return this;
  }

  public AnnotationMap addGosuAnnotation( Object annotation )
  {
    _currentAnnotationList.add( annotation );
    return this;
  }

  public Map<String, List> getAnnotations()
  {
    return Collections.unmodifiableMap( _annotationsByFeatureName );
  }
}
