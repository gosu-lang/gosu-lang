package gw.specContrib.annotations

@gw.lang.Deprecated("")
class Errant_AnnotationTargets2 {

  @gw.lang.Deprecated("")
  var str1 = "Hello world!"

  @gw.lang.Deprecated("")
  function somefun(@gw.lang.Deprecated("") x: int, @gw.lang.Deprecated("") y: int) {

    @gw.lang.Deprecated("")     //## issuekeys: MSG_
    var int1 = 42

    @gw.lang.Deprecated("")     //## issuekeys: MSG_
    if (true) {
      print("hello")
    }
  }
}