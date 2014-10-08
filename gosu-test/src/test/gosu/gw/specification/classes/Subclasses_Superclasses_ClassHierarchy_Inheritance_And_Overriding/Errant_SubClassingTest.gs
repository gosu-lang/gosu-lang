package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

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
  }

  class I extends J {
    final override function foo() : int {
      return super.foo() + 8
    }
  }

  function testSubClassing() {
    var b = new B0()
    var c = new C0(2)

    var tmp : int

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
  }

  function testOverriding() {
    var tmp : int

    var b2 = new B2()
    tmp = b2.x
    b2.writeY()
    tmp = b2.x
    tmp = b2.y

    var c2 = new C2()
    tmp = c2.x
    c2.writeY()
    tmp = c2.x
    tmp = c2.y

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
}
