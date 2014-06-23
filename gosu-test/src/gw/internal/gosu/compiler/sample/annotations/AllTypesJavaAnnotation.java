/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.sample.annotations;

public @interface AllTypesJavaAnnotation
{
  boolean a_boolValue();
  byte b_byteValue();
  char c_charValue();
  short d_shortValue();
  int e_intValue();
  long f_longValue();
  float g_floatValue();
  double h_doubleValue();
  String i_strValue();
  SampleAnnotationEnum j_enumValue();
  //NestedAnnotation k_annotationValue();

  boolean[] a_boolValueArr();
  byte[] b_byteValueArr();
  char[] c_charValueArr();
  short[] d_shortValueArr();
  int[] e_intValueArr();
  long[] f_longValueArr();
  float[] g_floatValueArr();
  double[] h_doubleValueArr();
  String[] i_strValueArr();
  SampleAnnotationEnum[] j_enumValueArr();
  //NestedAnnotation[] annotationValueArr();
}
