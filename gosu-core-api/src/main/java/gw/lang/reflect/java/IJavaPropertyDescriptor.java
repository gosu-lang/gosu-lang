/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;

import java.io.Serializable;

public interface IJavaPropertyDescriptor extends Serializable {
  String getName();

  IJavaClassMethod getReadMethod();
  IJavaClassMethod getWriteMethod();

  IType getPropertyType();

  boolean isHidden();

  boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);

  boolean isHiddenViaFeatureDescriptor();

  boolean isDeprecated();

  String getDisplayName();

  String getShortDescription();
}