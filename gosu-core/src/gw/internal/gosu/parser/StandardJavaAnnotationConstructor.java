/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.annotation.Annotations;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.java.IJavaClassMethod;

import java.lang.annotation.Annotation;

class StandardJavaAnnotationConstructor implements IConstructorHandler {
  private final IJavaClassMethod[] methods;
  private JavaTypeInfo javaTypeInfo;

  public StandardJavaAnnotationConstructor(JavaTypeInfo javaTypeInfo, IJavaClassMethod[] methods) {
    this.javaTypeInfo = javaTypeInfo;
    this.methods = methods;
  }

  @Override
  public Object newInstance( Object... args )
  {
    if (!(javaTypeInfo.getAnnotatedElement() instanceof ClassJavaClassInfo)) {
      throw new RuntimeException("ClassJavaClassInfo expected but found " + javaTypeInfo.getAnnotatedElement().getClass().getSimpleName());
    }
    Class<?> javaClass = ((ClassJavaClassInfo) javaTypeInfo.getAnnotatedElement()).getJavaClass();
    Annotations.Builder<? extends Annotation> builder = Annotations.builder( (Class) javaClass );
    for( int i = 0; i < args.length; i++ )
    {
      builder.withElement( methods[i].getName(), args[i] );
    }
    return builder.create();
  }
}
