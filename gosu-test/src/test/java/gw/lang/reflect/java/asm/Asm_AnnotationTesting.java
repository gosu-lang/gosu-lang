/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

@Deprecated()
@Annotation_Simple
@Annotation_Default("fish")
@Annotation_Fields( int_field=7,
                    enum_field= Asm_Enum.Larry,
                    intArray_field={1,2,3},
                    stringArray_field={"a", "b", "c"},
                    class_field=StringBuilder.class,
                    anno_field=@Annotation_Default("hi"),
                    annoArray_field={@Annotation_Default("hi"),@Annotation_Default("bye")})
public class Asm_AnnotationTesting {

  @Deprecated()
  @Annotation_Simple
  @Annotation_Default("meat")
  @Annotation_Fields( int_field=7,
                      enum_field=Asm_Enum.Larry,
                      intArray_field={1,2,3},
                      stringArray_field={"a", "b", "c"},
                      class_field=StringBuilder.class,
                      anno_field=@Annotation_Default("hi"),
                      annoArray_field={@Annotation_Default("hi"),@Annotation_Default("bye")})
  private String _string;
  private String _string2;
}
