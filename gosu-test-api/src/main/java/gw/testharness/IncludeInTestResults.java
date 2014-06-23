/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations which have this annotation will be added to the JUnit test results XML file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface IncludeInTestResults {
}