package gw.specContrib.classes.method_Scoring

class FunctionToInterfaceMethodScoring {

  function test() {
    a( \-> { return new Object() })  // No longer ambiguous
  }

  function test2() {
    a(\-> { return "" })  // No longer ambiguous
  }

  function test3() {
    a( \-> { return 1 })  // No longer ambiguous
  }

  function test4() {
    a(\-> { })          // OS Gosu likes I1 the best
  }

  function a(s: I1) {}

  function a(s: I2) {}

  function a(s: I3) {}

  interface I1 {
    function m()
  }

  interface I2 {
    function get(): Object
  }

  interface I3 {
    function get(): String
  }

}
