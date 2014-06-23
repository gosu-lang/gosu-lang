/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.lang.annotation.Annotation;

public interface IAnnotationInfoFactory
{
  IAnnotationInfo create( Class type, Object[] expressionValue, IFeatureInfo owner );
  IAnnotationInfo createJavaAnnotation(Annotation annotation, IFeatureInfo owner);
}