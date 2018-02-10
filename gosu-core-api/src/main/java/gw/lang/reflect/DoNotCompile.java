package gw.lang.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to indicate that a Gosu type should not be statically compiled as part of a project's build process.
 * Instead the class should be compiled dynamically at runtime.
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
public @interface DoNotCompile
{
}
