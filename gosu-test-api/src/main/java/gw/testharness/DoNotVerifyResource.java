/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * Tells the com.guidewire.testharness.multiapp.VerifyAllResourcesAndPCFFilesTest (in pl-test module) to ignore this type. Used for classes
 * that are intentionally invalid (for tests).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DoNotVerifyResource {
}
