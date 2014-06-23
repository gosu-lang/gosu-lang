/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public interface IJavaClassParameterizedType extends IJavaClassType {

  IJavaClassType[] getActualTypeArguments();

  IJavaClassType getConcreteType();

}