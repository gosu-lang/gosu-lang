/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.AnnotationUseSiteTarget;

import java.io.Serializable;

public interface IAnnotationInfo extends Serializable {

  IType getType();

  Object getInstance();

  Object getFieldValue( String field );

  String getName();

  String getDescription();

  IType getOwnersType();

  default AnnotationUseSiteTarget getTarget()
  {
    return null;
  }
}
