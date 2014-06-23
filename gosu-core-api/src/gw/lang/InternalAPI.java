/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a public Java member is for internal use only, i.e., not supported for customer use. Such members
 * are subject to change or removal at any time.
 *
 * @deprecated This annotation is a temporary indicator of members on public types that are for internal use only.
 * Such members should be moved off the public type as soon as possible. See
 * https://confluence.guidewire.com/display/MOD/Removal+of+@InternalAPI+members+from+public+APIs for more details.
 */
@java.lang.Deprecated
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface InternalAPI {
}
