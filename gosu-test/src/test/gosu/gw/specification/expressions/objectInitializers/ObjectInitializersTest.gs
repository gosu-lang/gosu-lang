package gw.specification.expressions.objectInitializers

uses gw.BaseVerifyErrantTest

class ObjectInitializersTest extends BaseVerifyErrantTest {

  function testErrant_ObjectInitializersTest() {
    processErrantType(Errant_ObjectInitializersTest)
  }

  function testBasic() {
    var x0 : C0 = new C0() {:a = 1, :b = 2 }
    assertEquals(1, x0.a)
    assertEquals(2, x0.b)

    var x1 : C0 = new C0() {:a = 1 }
    assertEquals(1, x1.a)
    assertEquals(0, x1.b)

    var x8 : C0 = new C0() {:c = 1 }

    var x4 : C0 = new C0() { }
    assertEquals(0, x4.a)
    assertEquals(0, x4.b)

    var x5 : C1 = new C1(1, 2) { }
    assertEquals(1, x5.a)
    assertEquals(2, x5.b)
    assertEquals(0, x5.intC())

    var x7 : C1 = new C1(1, 2) { :a = 5}
    assertEquals(5, x7.a)
    assertEquals(2, x7.b)
    assertEquals(0, x7.intC())
    var x16 : C4 = new C4() {:a = 1, :b = new C4() { :a = 2 } }
    assertEquals(1, x16.a)
    assertEquals(2, x16.b.a)
    assertEquals(null, x16.b.b)
  }

  class C0 {
    public var a : int
    var _b : int as b
    var c : int
  }

  class C1 {
    public var a : int
    var _b : int as b
    var c : int
    var _d : int as readonly d

    construct(x : int, y : int) {
      a = x
      _b = y
    }

    construct(x : int, y : int, z : int) {
      a = x
      _b = y
      c = z
    }

    function intC() : int {
      return c
    }
  }

  class C4 {
    public var a : int
    public var b : C4
  }

}