package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses gw.BaseVerifyErrantTest
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B0
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B1
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B2
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C0
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C1
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C2
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C3
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C12
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.I6
uses java.lang.Integer

class InheritanceTest  extends BaseVerifyErrantTest {
  static class W {
    static function foo() : int { return 2}
  }

  static class R extends W {
    static function foo() : int { return 3}
  }

  class J {
    function foo() : int { return 7}
    function foo(x1 : int) : int {return 17}
    var x : int as PropX = 6
  }

  class I extends J {
    final override function foo() : int {
      return super.foo() + 8
    }
    override property get PropX(): int { return 10}
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

    var c0 = new C0()
    assertEquals(c0.PropZ, 10)
    assertEquals(B0.PropZ, 6)
    assertEquals(C0.PropZ, 10)
    C0.PropZ = 12
    assertEquals(C0.PropZ, 10)
    assertEquals(B0.PropZ, 12)
    assertEquals((c0 as B0).PropZ, 12)
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
    assertEquals(b2.PropZ, 6)

    var c2 = new C2()
    assertEquals(c2.x, 1)
    c2.writeY()
    assertEquals(c2.x, 0)
    assertEquals(c2.y, 2)
    assertEquals(c2.PropZ, 6)
    c2.PropZ = 10
    assertEquals(c2.PropZ, 10)

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

    var i = new I()
    assertEquals(i.foo(), 15)
    assertEquals(i.PropX, 10)
    assertEquals(i.foo(3), 17)
    i.PropX = 11
    var j : J = i
    assertEquals(i.foo(), 15)
    assertEquals(i.PropX, 10)
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

  function testErrant_StaticMethodOverrideInstanceMethodTest() {
    processErrantType(Errant_StaticMethodOverrideInstanceMethodTest)
  }


  //// test interface member inheritance/override
  class K implements I6 {
    override function firstMethod() : int {
      return 1
    }

    override function secondMethod(): int {
      return 2
    }

    override function thirdMethod() {
    }
  }

  function testInterfaceInheritance(){
    var tmp : int

    tmp = K.RED
    assertEquals(tmp, 1)
    tmp = K.ORANGE
    assertEquals(tmp, 3)
    tmp = K.YELLOW
    assertEquals(tmp, 5)


    var k1 = new K()
    assertEquals(k1.firstMethod(), 1)
    assertEquals(k1.secondMethod(), 2)
  }
  /////////////////////////////////////////////////


  //// test instance method call for method override
  class Class1 {
    function test1() : int {
      return 1
    }
    function test2() : int {
      return test1()
    }
  }
  class Class2 extends Class1{
    override function test1() : int {
      return 2
    }
  }

  function testOverridenMethodCall(){
    var obj = new Class1()
    assertEquals(obj.test1(), 1)
    assertEquals(obj.test2(), 1)

    var obj1 = new Class2()
    assertEquals(obj1.test1(), 2)
    assertEquals(obj1.test2(), 2)

    var obj2 : Class1 = new Class2()
    assertEquals(obj2.test1(), 2)
    assertEquals(obj2.test2(), 2)
  }
  ////////////////////////////////////////////////


  //// test method override in generics. Subclass implements a method from interface and inherit a method from superclass, but these two methods have the same method signature
  static interface I10<T>{
    function id(x : T) : T
  }

  static class C10<T>{
    function id(x : T) : T {
      return x
    }
  }

  static class D10 extends C10<String> implements I10<Integer> {
    override function id(x: Integer) : Integer {
      return x
    }
    override function id(x: String) : String {
      return x
    }
  }

  static class D20 extends C10<String> implements I10<Integer> {
    override function id(x: Integer) : Integer {
      return x
    }
  }

  function testMethodOverrideInGenerics(){
    var obj1 = new D10()
    assertEquals(obj1.id(new Integer(1)), new Integer(1))
    assertEquals(obj1.id("first"), "first")

    var obj2 = new D20()
    assertEquals(obj2.id(new Integer(2)), new Integer(2))
//    assertEquals(obj2.id("second"), "second")                    //##KB(IDE-2239)
  }

  function testErrant_MethodOverrideInGenericsTest() {
    processErrantType(Errant_MethodOverrideInGenericsTest)
  }
  ////////////////////////////////////////////////
}