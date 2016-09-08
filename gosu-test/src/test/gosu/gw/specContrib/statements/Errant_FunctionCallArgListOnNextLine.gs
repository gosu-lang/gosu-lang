package gw.specContrib.statements

class Errant_FunctionCallArgListOnNextLine {

  function f2() {
    var auditPeriod = new String().toLowerCase()
        ("1" as String).length()

    print(auditPeriod)
    ("1" as CharSequence).subSequence(1,2)

  }

  function write(s : String) : String {
    print(s)
    return s
  }

  function f3() {

    print
        ("hello")

    print
        (("hello" as String).substring(1,4))

    var o : Errant_FunctionCallArgListOnNextLine
    var i : int
    i = 12
    (o as Object).toString()



    write
        ("1" as String).substring(0,1)
  }
}