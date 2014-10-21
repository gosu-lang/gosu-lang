package gw.lang.ir;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The compiler adds this to fields and methods that are explicitly assigned
 * 'internal' accessibility because private members may be compiled with internal
 * accessibility for the sake of inner class access.  Basically this annotation
 * serves to distinguish between what is explicitly static and what is not,
 * primarily for tooling.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Internal {
}
