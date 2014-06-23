/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * This annotation allows you to put multiple AnnotationUsage annotations on a java annotation.
 * It is not needed in Gosu classes, since multiple annotations are allowed.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@AnnotationUsage(target = UsageTarget.TypeTarget, usageModifier = UsageModifier.One)
public @interface AnnotationUsages
{
  AnnotationUsage[] value();
}
