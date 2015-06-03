package gw.specContrib.annotations;

public class JavaAnnotations {
  public static @interface JavaAnno1 {
    String a() default "a";
    String b() default "b";
  }

  public static @interface JavaAnno2 {
    String a() default "a";
    String b();
    String c() default "c";
    String d();
  }
}
