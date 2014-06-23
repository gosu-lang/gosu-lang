/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IUsageSiteValidatorReference
{
  /**
   * A reference to a class implementing the IUsageSiteValidator interface and a no-arg constructor
   */
  public abstract Class<? extends IUsageSiteValidator> value();
}