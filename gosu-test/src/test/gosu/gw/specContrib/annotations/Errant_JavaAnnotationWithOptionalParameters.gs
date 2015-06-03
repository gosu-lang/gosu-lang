package gw.specContrib.annotations

class Errant_JavaAnnotationWithOptionalParameters {
  @JavaAnnotations.JavaAnno1
  function fun1() {}

  // IDE-2252
  @JavaAnnotations.JavaAnno1("1")
  function fun2() {}

  @JavaAnnotations.JavaAnno1("1", "2")
  function fun3() {}
}