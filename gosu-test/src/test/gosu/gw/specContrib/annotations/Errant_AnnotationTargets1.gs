package gw.specContrib.annotations

@TestAnnotation
class Errant_AnnotationTargets1 {

  @TestAnnotation
  var str1 = "Hello world!"

  @TestAnnotation
  function somefun(@TestAnnotation x: int, @TestAnnotation y: int) {

    @TestAnnotation     //## issuekeys: MSG_
    var int1 = 42

    @TestAnnotation     //## issuekeys: MSG_
    if (true) {
      print("hello")
    }
  }
}