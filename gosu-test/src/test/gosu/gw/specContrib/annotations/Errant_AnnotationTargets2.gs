package gw.specContrib.annotations

@gw.lang.Deprecated("")
class Errant_AnnotationTargets2 {

  @gw.lang.Deprecated("")
  var str1 = "Hello world!"

  @gw.lang.Deprecated("")
  function somefun(@gw.lang.Deprecated("") x: int, @gw.lang.Deprecated("") y: int) {

    @gw.lang.Deprecated("")    //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NO_EXPLICIT_TYPE_INFO_FOUND, MSG_NO_EXPLICIT_TYPE_INFO_FOUND, MSG_NO_SUCH_FUNCTION
    var int1 = 42

    @gw.lang.Deprecated("")       //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NO_EXPLICIT_TYPE_INFO_FOUND, MSG_NO_EXPLICIT_TYPE_INFO_FOUND, MSG_NO_SUCH_FUNCTION
    if (true) {
      print("hello")
    }
  }
}