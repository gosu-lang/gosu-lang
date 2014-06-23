package gw.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To specify order of class members and such that otherwise lose declared order in bytecode.
 * We currently use this for ordering annotation methods so we can generate constructors in
 * declaration order. Note these constructors are for legacy purposes to provide backward
 * support for older Gosu annotation call sites where arguments were not named.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Order {
  String value() default "method";
  int index();
}
