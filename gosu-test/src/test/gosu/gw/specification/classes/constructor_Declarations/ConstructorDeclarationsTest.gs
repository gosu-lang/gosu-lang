package gw.specification.classes.constructor_Declarations

uses gw.BaseVerifyErrantTest

class ConstructorDeclarationsTest extends BaseVerifyErrantTest {
  class A {
    public var x : int
    construct() {
      x = 1
    }

    construct(y : int) {
      x = y
    }

    construct(y : double) {
      this(y as int + 1)
    }
  }

  class B extends A {}

  class C extends A {
    construct() {
      super(2.3)
    }
  }

  function testErrant_ConstructorModifiersTest() {
    processErrantType(Errant_ConstructorModifiersTest)
  }

  function testErrant_ConstructorDeclarationsTest() {
    processErrantType(Errant_ConstructorDeclarationsTest)
  }

  function testConstructorExecution() {
    assertEquals(new A().x, 1)
    assertEquals(new A(2).x, 2)
    assertEquals(new A(2.3).x, 3)

    assertEquals(new B().x, 1)

    assertEquals(new C().x, 3)
  }
}
