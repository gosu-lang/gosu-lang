package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses gw.BaseVerifyErrantTest


class InheritanceTest  extends BaseVerifyErrantTest {
  static class W {
    static function foo() : int { return 2}
  }

  static class R extends W {
    static function foo() : int { return 3}
  }

  class Q extends W {
    function foo() : int { return 6}
  }

  class J {
    function foo() : int { return 7}
  }

  class I extends J {
    final override function foo() : int {
      return super.foo() + 8
    }
  }

  function testErrant_SubClassingTest() {
    processErrantType(Errant_SubClassingTest)
  }

  function testSubClassing() {
    var b = new B0()
    var c = new C0(2)

    assertEquals(b.x, 1)
    assertEquals(b.readY(), 1)
    b = new B0(3, 3)
    assertEquals(b.x, 3)
    assertEquals(b.readY(), 3)

    assertEquals(c.x, 2)
    assertEquals(c.readX(), 2)
    assertEquals(c.readY(), 1)

    assertTrue(b typeis B0)
    assertFalse(b typeis C0)
    assertTrue(b typeis Object)
    assertTrue(c typeis B0)
    assertTrue(c typeis C0)
    assertTrue(c typeis Object)
  }

  function testErrant_constructorsTest() {
    processErrantType(Errant_constructorsTest)
  }

  function testOverriding() {
    var b2 = new B2()
    assertEquals(b2.x, 0)
    b2.writeY()
    assertEquals(b2.x, 1)
    assertEquals(b2.y, 1)

    var c2 = new C2()
    assertEquals(c2.x, 1)
    c2.writeY()
    assertEquals(c2.x, 0)
    assertEquals(c2.y, 2)

    var c3 = new C3()
    assertEquals(c3.x, 0)
    c3.writeY()
    assertEquals(c3.x, 1)
    assertEquals(c3.y, 3)

    var c4 : B2 = new C2()
    assertEquals(c4.x, 1)
    c4.writeY()
    assertEquals(c4.x, 0)
    assertEquals(c4.y, 2)

    var c5 : B2= new C3()
    assertEquals(c5.x, 0)
    c5.writeY()
    assertEquals(c5.x, 1)
    assertEquals(c5.y, 3)

    var r = new R()
    assertEquals(r.foo(), 3)
    var r2 : W = r
    assertEquals(r2.foo(), 2)
    assertEquals(R.foo(), 3)
    assertEquals(W.foo(), 2)

    var q = new Q()
    assertEquals(q.foo(), 6)
    var w : W = q
    assertEquals(w.foo(), 2)
    assertEquals(W.foo(), 2)

    var i = new I()
    assertEquals(i.foo(), 15)
    var j : J = i
    assertEquals(i.foo(), 15)
  }

  function testSuper() {
    var b1 = new B1()
    var c1 = new C1()
    var c12 = new C12()

    assertEquals(b1.x, 2)
    assertEquals(c1.x, 2)
    assertEquals(c1.y, 8)
    assertEquals(c12.x, 2)
    assertEquals(c12.y, 8)
  }

  function testErrant_MultipleInheritanceTest() {
    processErrantType(Errant_MultipleInheritanceTest)
  }
}