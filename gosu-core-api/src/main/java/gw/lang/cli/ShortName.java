/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ShortName
{
  public String name();
}