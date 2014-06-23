package gw.lang.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ##todo: this is a temporary annotation, delete this and use java.lang.annotation.Repeatable when we move to Java 8
 * !!
 * !!NOTE: when you move to Java 8 you'll need to change JavaTypes#REPEATABLE() to point to the java.lang.annotations.Repeatable.class
 * !!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Repeatable {
  /**
   * Indicates the <em>containing annotation type</em> for the
   * repeatable annotation type.
   *
   * @return the containing annotation type
   */
  Class<? extends Annotation> value();
}
