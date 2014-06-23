/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import gw.testharness.DoNotRunTest;
import gw.testharness.IncludeInTestResults;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DoNotRunTest
@IncludeInTestResults
public @interface   Disabled {
  String assignee();
  String reason();
}