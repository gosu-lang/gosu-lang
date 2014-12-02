package gw.internal.gosu.parser;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import gw.lang.reflect.java.IJavaType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the runtime type of the annotated type is an extension of {@link gw.lang.reflect.java.IJavaType}. This is
 * an internal annotation whose sole purpose is to preserve runtime behavior provided by legacy type loaders.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ExtendedType {
}
