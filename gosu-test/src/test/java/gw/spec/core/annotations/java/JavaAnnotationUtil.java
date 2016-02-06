/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import gw.lang.ir.Internal;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JavaAnnotationUtil
{
  public static Annotation[] getMethodAnnotations( IGosuClass gsClass, String s )
  {
    Class<?> backingClass = gsClass.getBackingClass();
    for( Method method : TypeSystem.getDeclaredMethods( backingClass ) )
    {
      if( method.getName().equals( s ) )
      {
        return getAnnotationsMinusInternal( method );
      }
    }
    return null;
  }

  private static Annotation[] getAnnotationsMinusInternal( Method method ) {
    Annotation[] annotations = method.getAnnotations();
    List<Annotation> list = new ArrayList<Annotation>();
    for( Annotation a: annotations ) {
      if( a.annotationType() != Internal.class ) {
        list.add( a );
      }
    }
    return list.toArray( new Annotation[list.size()] );
  }
}
