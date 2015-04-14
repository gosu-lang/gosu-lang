package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses java.util.ArrayList
uses java.util.LinkedList
uses java.lang.Integer
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B0
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B2
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C0
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C2
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.C3

class Errant_SubClassingTest {
  class cycA extends cycA {}   //## issuekeys: MSG_CYCLIC_INHERITANCE

  class cycB extends cycC {}

  class cycC extends cycB {}   //## issuekeys: MSG_CYCLIC_INHERITANCE

  class X {
    final function foo() : int { return 1}
  }

  static class W {
    static function foo() : int { return 2}
  }

  static class R extends W {
   static function foo() : int { return 3}
  }

  class Y extends X {
    override function foo() : int { return 4}  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
  }

  class Z extends W {
    override function foo() : int { return 5}  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE
  }

  class Q extends W {
    function foo() : int { return 6}
  }

  class J {
    function foo() : int { return 7}
    function foo1() : int { return 7}
    internal function foo2() : int { return 7}
    function bar() : List<String> {return new ArrayList<String>()}
    function baz() : ArrayList<String> {return new ArrayList<String>()}
    function qux() : boolean {return true}
  }

  class I extends J {
    final override function foo() : int {
      return super.foo() + 8
    }

    // test overriden method's accessibility
    override internal function foo1() : int {return 10}  //## issuekeys: MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES
    override function foo2() : int {return 10}

    // test overriden method's return type
    override function bar() : List<String> {return new LinkedList<String>()}
    override function baz() : List<String> {return new LinkedList<String>()}  //## issuekeys: MSG_FUNCTION_CLASH, MSG_FUNCTION_NOT_OVERRIDE
    override function qux() : int {return 1}  //## issuekeys: MSG_FUNCTION_CLASH, MSG_FUNCTION_NOT_OVERRIDE
  }

  class K {
    function foo(a : int, list : List<String>) : int { return 1}
  }
  class L extends K {
    override function foo(a : int, list : List<Integer>) : int { return 1}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD, MSG_FUNCTION_NOT_OVERRIDE
  }

  function testSubClassing() {
    var b = new B0()
    var c = new C0(2)

    var tmp : int
    var tmpStr : String

    tmp = b.x
    b.readY()
    b = new B0(3, 3)
    tmp = b.x
    b.readY()

    tmp = c.x
    c.readX()
    c.readY()
    c = new C0()
    c = new C0(8, 8)  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR

    var isa : boolean

    isa = b typeis B0
    isa = b typeis C0
    isa = b typeis Object
    isa = c typeis B0
    isa = c typeis C0
    isa = c typeis Object

    var o : Object
    o = b
    o = c

    var c0 = new C0()
    c0.PropZ = 12  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    B0.PropZ = 6
    C0.PropZ = 12
    (c0 as B0).PropZ = 12  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER, MSG_UNNECESSARY_COERCION
    tmp = c0.hashCode()
    tmpStr = c0.toString()
    isa = c0.equals(c0)
  }

  function testOverriding() {
    var tmp : int

    var b2 = new B2()
    tmp = b2.x
    b2.writeY()
    tmp = b2.x
    tmp = b2.y
    tmp = b2.PropZ

    var c2 = new C2()
    tmp = c2.x
    c2.writeY()
    tmp = c2.x
    tmp = c2.y
    tmp = c2.PropZ
    c2.PropZ = 10
    tmp = c2.PropZ

    var c3 = new C3()
    tmp = c3.x
    c3.writeY()
    tmp = c3.x
    tmp = c3.y

    var c4 : B2 = new C2()
    tmp = c4.x
    c4.writeY()
    tmp = c4.x
    tmp = c4.y

    var c5 : B2= new C3()
    tmp = c5.x
    c5.writeY()
    tmp = c5.x
    tmp = c5.y
  }



  // test abstract/non-abstract method override-inheritance-hide
  class J1 {
    function test() { print("from J class") }
    function test1(){ print("from J class") }
    function test2(){ print("from J class") }
  }
  abstract class L1 extends J1 {
    override abstract function test()          //## KB(IDE-2223)
    abstract override function test1()
    abstract function test2()                  //## KB(IDE-2223)
  }
  class M1 extends L1{
    override function test() { print("from M class") }
    override function test1() {}
    override function test2() {}
  }

  interface I1{
    function test1()
    function test2()
    function test3()
  }
  abstract class AC1 implements I1 {
    override abstract function test1()         //## KB(IDE-2223)
    abstract override function test2()
    abstract function test3()                  //## KB(IDE-2223)
  }
  ///////////////////////////////////////////////////////////////
}
