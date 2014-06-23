/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.annotation.Annotations;
import gw.lang.reflect.IAnnotationInfoFactory;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFeatureInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 */
public class AnnotationInfoFactoryImpl implements IAnnotationInfoFactory
{
  private static final AnnotationInfoFactoryImpl INSTANCE = new AnnotationInfoFactoryImpl();

  public static AnnotationInfoFactoryImpl instance() {
    return INSTANCE;
  }

  private AnnotationInfoFactoryImpl() {
  }

  public IAnnotationInfo create( Class type, Object[] expressionValue, IFeatureInfo owner )
  {
    Annotations.Builder builder = Annotations.builder(type);
    Method[] declaredMethods = type.getDeclaredMethods();
    if (declaredMethods.length != expressionValue.length) {
      throw new RuntimeException("Must pass " + declaredMethods.length + " arguments for annotation.");
    }
    for (int i = 0; i < declaredMethods.length; i++) {
      builder.withElement(declaredMethods[i].getName(), expressionValue[i]);
    }
    Annotation annotation = builder.create();
    return createJavaAnnotation(annotation, owner);
  }

  @Override
  public IAnnotationInfo createJavaAnnotation(Annotation annotation, IFeatureInfo owner) {
    return new ClassAnnotationInfo( annotation, owner == null ? null : owner.getOwnersType() );
  }
}