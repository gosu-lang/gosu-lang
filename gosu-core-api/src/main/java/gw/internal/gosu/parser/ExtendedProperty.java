package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaPropertyInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the annotated property method is an extended property, i.e., an extension of
 * {@link IJavaPropertyInfo}. This annotation must be used on classes annotated with {@link ExtendedType} in order to
 * have any effect.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExtendedProperty {
}
