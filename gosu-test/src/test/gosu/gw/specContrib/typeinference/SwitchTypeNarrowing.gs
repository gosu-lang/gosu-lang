package gw.specContrib.typeinference

class SwitchTypeNarrowing {
  private enum MyEnum  { ONE, TWO }

  function test() {
    var x: Object = "neat"
    var f = x typeis String

    switch (typeof( ((x)) )) {  // crazy parentheses are valid 
      case MyEnum:
        if (f) {
          switch (x) {
            case ONE:
              return
            case TWO:
              throw new java.lang.RuntimeException()
          }
        } else {
          break
        }
      case String:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        x.contains("ne")  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_SUCH_FUNCTION
    }
  }
}
