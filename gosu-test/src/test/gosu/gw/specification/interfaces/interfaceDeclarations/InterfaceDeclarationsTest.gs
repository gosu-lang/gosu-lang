package gw.specification.interfaces.interfaceDeclarations

uses gw.BaseVerifyErrantTest

class InterfaceDeclarationsTest extends BaseVerifyErrantTest {
  function testErrant_InterfaceDeclarationsTest() {
    processErrantType(Errant_InterfaceDeclarationsTest)
  }

  function testErrant_topLevelInterfaceTest() {
    processErrantType(Errant_topLevelInterfaceTest)
  }

  interface I0 {
    var i : int = 0
    var j : int = 0
    function m0() : int {
      this.hello()
      return 0
    }

    function hello() {    }
    function m1() : int
    static function ms() : boolean { return false }
    class A {}
    interface I01 {
      function m00(a : int)
    }
  }

  private interface I4 {
     var i : int = 8
  }

  protected interface I6 {
    var i : int = 8
    var j : int = (\ -> 3)()
  }

  internal interface I8 {
    var i : int = 8
    function m8() : int { return 8 }
  }

  interface I9 extends I0, I8  {
    function m9() : int { return 9 }
    static function ms() : boolean { return true }
  }

  class Impl implements  I9 {
    override function m1() : int {
      return 100
    }
  }

  function testExtends() {
    var impl : Impl = new Impl()
    var i9 : I9 = impl
    var i0 : I0 = impl
    var i8 : I8 = impl

    assertEquals(0, impl.m0())
    assertEquals(100, impl.m1())
    assertEquals(8, impl.m8())
    assertEquals(9, impl.m9())
    assertEquals(0, i9.m0())
    assertEquals(100, i9.m1())
    assertEquals(8, i9.m8())
    assertEquals(9, i9.m9())
    assertEquals(0, i9.i)
    assertTrue(i9.ms())
    assertEquals(0, i0.m0())
    assertEquals(100, i0.m1())
    assertEquals(0, i0.i)
    assertEquals(0, I0.i)
    assertFalse(i0.ms())
    assertEquals(8, i8.m8())
    assertEquals(8, i8.i)
    assertEquals(8, I8.i)
  }

  interface Top  {
    function name() : String {
      return "unnamed"
    }
  }
  interface Left extends Top {
    override function name() : String {
      return "fromLeft"
    }
  }
  interface Right extends Top {
  }
  interface Bottom extends Left, Right {
  }


  function testDefaultMethods() {
    var r : Right = new Right() {}
    var b : Bottom = new Bottom() {}
    assertEquals("unnamed", r.name())
    assertEquals("fromLeft", b.name())

  }

  static class Diamond0 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
    }
    interface C extends A {
    }
    static class D implements B, C {
    }
  }

  static class Diamond1 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return 1
      }
    }
    interface C extends A {
    }
    static class D implements B, C {
    }
  }

  static class Diamond22 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return 1
      }
    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        super[C].m()
        return super[B].m()
      }

    }
  }

  static class Diamond222 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return super[A].m()
      }
    }
    interface C extends A {

    }
    static class D implements B, C {

    }
  }

  static class Diamond33 {
    interface A  {

    }
    interface B extends A {
      function m() : int {
        return 1
      }
    }
    interface C extends A {
      function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        super[C].m()
        return super[B].m()
      }
    }
  }


  static class Diamond4 {
    interface A  {
      function m() : int {
        return 1
      }
    }
    interface B extends A {

    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
    }
  }

  static class Diamond5 {
    interface A  {
      function m() : int {
        return 1
      }
    }
    interface B extends A {

    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        return 3
      }
    }
  }

  function testDiamond() {
    assertEquals(0, new Diamond0.D().m())
    assertEquals(1, new Diamond1.D().m())
    assertEquals(2, new Diamond4.D().m())
    assertEquals(3, new Diamond5.D().m())
    assertEquals(1, new Diamond22.D().m())
    assertEquals(1, new Diamond33.D().m())
    assertEquals(0, new Diamond222.D().m())
  }

  interface Superinterface  {
    function foo() : int {
      return 0
    }
  }
  class Subclass2 implements Superinterface {
    override function foo() : int {
      return 1
    }
    function tweak() : int {
      return super[Superinterface].foo()
    }
  }

  function testSuperSyntaxt() {
     assertEquals(0, new Subclass2().tweak())
     assertEquals(1, new Subclass2().foo())
  }
}