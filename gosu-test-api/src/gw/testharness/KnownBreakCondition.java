/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@KnownBreakQualifier(KnownBreakConditionPredicate.class)
public @interface KnownBreakCondition {
  public Class<? extends IEnvironmentalCondition>[] value();
}