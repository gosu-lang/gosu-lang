/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.util.Pair;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE } )
@Inherited
@Deprecated("This annotation is deprecated and will be ignored if you add it to a new file. Do NOT use.")
@java.lang.Deprecated
public @interface PublishedTypes
{
  PublishedType[] value();
}