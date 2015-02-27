package gw.specContrib.classes.anonymous_Class

/**
 * IDE-1059 : No parse error when print statement directly under anonymous class
 */
class Errant_AnonymousClassCallsFunction {
  class AA {
    function foo() {
    }
  }

  function somefunOne() {
    //IDE-1059 - Error shown on different lines in Parser as compared to OS Gosu. Not an issue. Just implementation difference
    var xx = new AA() {
      foo()                 //## issuekeys: UNEXPECTED TOKEN
    }
  }

  function someFunTwo() {
    //IDE-1059 - Error shown on different lines in Parser as compared to OS Gosu. Not an issue. Just implementation difference
    var yy = new AA() {
      print("hello")        //## issuekeys: UNEXPECTED TOKEN
    }
  }

}