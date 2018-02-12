/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.ir.IRType;

import java.util.List;
import manifold.ext.ExtensionMethod;

public interface IRMethod {

  IRType getReturnType();

  List<IRType> getExplicitParameterTypes();

  List<IRType> getAllParameterTypes();

  String getName();

  IRType getOwningIRType();

  IType getOwningIType();

  IRelativeTypeInfo.Accessibility getAccessibility();

  boolean isStatic();

  IRType getTargetRootIRType( );

  IGenericTypeVariable[] getTypeVariables();

  boolean isBytecodeMethod();

  boolean couldHaveTypeVariables();

  IFunctionType getFunctionType();

  boolean isGeneratedEnumMethod();

  default boolean isManifoldExtension()
  {
    IAnnotatedFeatureInfo fi = getFeatureInfo();
    return fi != null && fi.getAnnotation( TypeSystem.getByFullNameIfValid( ExtensionMethod.class.getName() ) ) != null;
  }

  default IAnnotatedFeatureInfo getFeatureInfo()
  {
    return null;
  }
}
