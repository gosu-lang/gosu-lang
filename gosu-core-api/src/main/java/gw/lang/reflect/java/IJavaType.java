/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.internal.gosu.parser.IParameterizableType;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IHasInnerClass;
import gw.lang.reflect.ICanBeAnnotation;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.perf.objectsize.ObjectSize;

import java.util.List;

public interface IJavaType extends IJavaBackedType, IEnhanceableType, IHasInnerClass, IFileRepositoryBasedType, IParameterizableType, ICanBeAnnotation
{
  /**
   * Returns the java class for this java type
   * @return the java class for this java type
   * @deprecated Use only at runtime.  At compile time use getBackingClassInfo().
   */
  Class getIntrinsicClass();

  IJavaClassInfo getBackingClassInfo();
  /**
   * @return An array of Java types reflecting all the classes and interfaces
   * declared as members of the class represented by this Class object. These
   * include public, protected, internal, and private classes and interfaces
   * declared by the class, but excludes inherited classes and interfaces.
   */
  List<IJavaType> getInnerClasses();

  /**
   * If this is a parameterized type, returns the generic type this type
   * parameterizes. Otherwise, returns null.
   */
  IJavaType getGenericType();

  /**
   * Returns the Gosu proxy for this class.
   */
  IGosuClass getAdapterClass();

  /**
   * Creates the Gosu proxy type for this class.
   * @return The newly created proxy type.
   */
  IGosuClass createAdapterClass();

  ObjectSize getRetainedMemory();

  boolean isStructure();
}
