/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME )
public @interface JavaEnumArrayAnnotation {
  JavaEnum[] value();
}
