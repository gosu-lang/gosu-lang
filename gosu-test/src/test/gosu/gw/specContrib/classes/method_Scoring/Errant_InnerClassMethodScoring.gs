package gw.specContrib.classes.method_Scoring

class Errant_InnerClassMethodScoring {
  function foo(b: boolean): int {return 1}

  static class Inner {
    function foo(b: Boolean): String {return ""}

    function test() {
      var s1: String = foo(true)
      var s2: String = foo(Boolean.TRUE)
      var i1: int = foo(true)            //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'INT'
      var i2: int = foo(Boolean.TRUE)    //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'INT'
    }
  }
}