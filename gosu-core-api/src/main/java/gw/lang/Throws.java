/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.annotation.Order;
import gw.lang.annotation.Repeatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Repeatable(Throwses.class)
public @interface Throws
{
  @Order( index=0 )
  Class ExceptionType();

  @Order( index=1 )
  String ExceptionDescription();
}
