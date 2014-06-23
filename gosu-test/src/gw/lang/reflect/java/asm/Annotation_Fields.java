/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface Annotation_Fields {
  int int_field();
  String string_field() default "fred";
  Asm_Enum enum_field() default Asm_Enum.Curly;
  int[] intArray_field();
  String[] stringArray_field();
  Class<?> class_field();
  Annotation_Default anno_field();
  Annotation_Default[] annoArray_field();
}
