/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.annotation.Order;
import java.lang.annotation.Repeatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(Params.class)
public @interface Param
{
  @Order( index=0 )
  String FieldName();

  @Order( index=1 )
  String FieldDescription();
}
