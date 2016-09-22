package gw.specContrib.statements

class Errant_FunctionCallArgListOnNextLine {

  function f2() {
    var auditPeriod = new String().toLowerCase()
        ("1" as String).length() //## issuekeys: MSG_UNNECESSARY_COERCION

    print(auditPeriod)
    ("1" as CharSequence).subSequence(1,2) //## issuekeys: MSG_UNNECESSARY_COERCION

  }

  function write(s : String) : String {
    print(s)
    return s
  }

  function f3() {

    print
        ("hello")

    print
        (("hello" as String).substring(1,4)) //## issuekeys: MSG_UNNECESSARY_COERCION

    var o : Errant_FunctionCallArgListOnNextLine
    var i : int
    i = 12
    (o as Object).toString() //## issuekeys: MSG_UNNECESSARY_COERCION



    write
        ("1" as String).substring(0,1) //## issuekeys: MSG_UNNECESSARY_COERCION
  }
}
