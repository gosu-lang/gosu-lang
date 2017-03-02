/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.AnnotationUseSiteTarget;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public interface IGosuAnnotation
{
  String getName();

  IType getType();

  String getNewExpressionAsString();

  IExpression getExpression();
  void clearExpression();

  boolean shouldPersistToClass();

  boolean shouldRetainAtRuntime();

  ICompilableType getOwnersType();

  AnnotationUseSiteTarget getTarget();

  default boolean isJavaAnnotation()
  {
    return JavaTypes.ANNOTATION().isAssignableFrom( getType() );
  }
}
