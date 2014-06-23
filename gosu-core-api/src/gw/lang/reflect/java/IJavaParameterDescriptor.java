/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IType;

import java.io.Serializable;

public interface IJavaParameterDescriptor extends Serializable {
  String getName();

  String getDisplayName();

  String getShortDescription();

  boolean isHidden();

  IType getFeatureType();
}