package gw.lang.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used for a ITypeManifold to map a generated Java
 * feature to the corresponding resource file location.
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
public @interface SourcePosition
{
  String url();
  int offset() default -1;
  int length() default -1;
  String type() default "";
  String feature();
  int line() default -1;
}
