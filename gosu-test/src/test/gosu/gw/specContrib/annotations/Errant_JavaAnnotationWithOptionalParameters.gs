package gw.specContrib.annotations

class Errant_JavaAnnotationWithOptionalParameters {
  @JavaAnnotations.JavaAnno1
  function fun11() {}

  // IDE-2252
  @JavaAnnotations.JavaAnno1("1")
  function fun12() {}

  @JavaAnnotations.JavaAnno1("1", "2")
  function fun13() {}


  @JavaAnnotations.JavaAnno2    //## issuekeys: CANNOT RESOLVE
  function fun21() {}

  @JavaAnnotations.JavaAnno2("1")  //## issuekeys: CANNOT RESOLVE
  function fun22() {}

  @JavaAnnotations.JavaAnno2("1", "2")
  function fun23() {}

  @JavaAnnotations.JavaAnno2("1", "2", "3")  //## issuekeys: CANNOT RESOLVE
  function fun24() {}

  @JavaAnnotations.JavaAnno2("1", "2", "3", "4")
  function fun25() {}


  @JavaAnnotations.JavaAnno3    //## issuekeys: CANNOT RESOLVE
  function fun31() {}

  @JavaAnnotations.JavaAnno3("1")
  function fun32() {}

  @JavaAnnotations.JavaAnno3("1", "2")
  function fun33() {}
}