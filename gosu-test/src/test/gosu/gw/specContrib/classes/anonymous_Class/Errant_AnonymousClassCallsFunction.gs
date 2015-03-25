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
    //IDE-1059
    var xx = new AA() { foo() }                //## issuekeys: UNEXPECTED TOKEN
  }

  function someFunTwo() {
    //IDE-1059
    var yy = new AA() { print("hello") }       //## issuekeys: UNEXPECTED TOKEN
  }
}