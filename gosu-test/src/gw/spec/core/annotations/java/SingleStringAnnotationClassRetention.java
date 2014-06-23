/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface SingleStringAnnotationClassRetention
{
  public abstract String value();
}