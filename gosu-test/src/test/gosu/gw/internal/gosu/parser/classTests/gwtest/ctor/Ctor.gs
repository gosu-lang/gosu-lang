package gw.internal.gosu.parser.classTests.gwtest.ctor

class Ctor {

  private var x : int

  construct() {
    this(2, 2)
  }

  construct(a : int, b : int) {
    this(a + b)
  }

  construct(n : int) {
    x = n
  }

  construct( blk: block() ) {
  }

  function foo() : int {
    return x
  }

}