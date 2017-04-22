package gw.lang.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used for a ISourceProducer to map a generated Java
 * feature to the corresponding resource file location.
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
public @interface SourcePosition
{
  String url();
  int offset();
  int length();
}
