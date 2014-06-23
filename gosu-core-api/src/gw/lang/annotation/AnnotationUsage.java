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
@AnnotationUsage(target = UsageTarget.TypeTarget, usageModifier = UsageModifier.Many)
public @interface AnnotationUsage {
  UsageTarget target();
  UsageModifier usageModifier();
}
