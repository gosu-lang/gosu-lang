package gw.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Callable;

/**
 * Properties that are null and are annotated with @Autocreate will be populated automatically during an assignment
 * of a subproperty. If a block is supplied, the block will be called to create the new object, otherwise
 * the parameterless contructor will be used.
 *
 *  Copyright 2013 Guidewire Software, Inc.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Autocreate {
  Class<? extends Callable> value() default Callable.class;
}
