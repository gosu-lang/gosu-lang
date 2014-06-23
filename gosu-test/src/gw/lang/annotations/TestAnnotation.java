/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {

  String s();

  int i() default 42;

  String[] arr() default { "hello", "world" };

  int[] primArr() default { 42 };
}
