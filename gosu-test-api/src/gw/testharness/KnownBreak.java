/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * See http://wiki/index.php/KnownBreak for detailed document
 */
@Retention(RUNTIME)
@Inherited
@IncludeInTestResults
public @interface KnownBreak {
  String targetUser() default "ignore";
  String targetBranch() default "none";
  String jira();
}
