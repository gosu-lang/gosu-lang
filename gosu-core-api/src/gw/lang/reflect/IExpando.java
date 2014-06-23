/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Map;

/**
 */
public interface IExpando {
  Object getFieldValue( String field );
  void setFieldValue( String field, Object value );
  void setDefaultFieldValue( String field );
  Object invoke( String methodName, Object... args );
  Map getMap();
}
