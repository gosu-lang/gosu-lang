package gw.specContrib.expressions.cast

class Errant_CompoundTypeCast {
  class A {
  }

  interface I {
  }

  class B extends A {
  }

  class CA extends A implements I {
  }

  class CB extends B implements I {
  }

  class R {}

  function test () {
    var x : A & I
    var y = x as B
    var yy = x as CA
    var yyy = x as CB
    var z = x as R  //## issuekeys: MSG_TYPE_MISMATCH
  }
}
