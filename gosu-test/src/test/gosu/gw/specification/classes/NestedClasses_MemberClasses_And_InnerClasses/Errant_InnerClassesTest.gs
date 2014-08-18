package gw.specification.classes.NestedClasses_MemberClasses_And_InnerClasses

class Errant_InnerClassesTest {
  static var sf: int
  var nf: int

  static class SMC {
    static var ssf: int = sf + Errant_InnerClassesTest.sf
    var snf: int = sf + Errant_InnerClassesTest.sf

    var s0 = outer.nf  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }

  class NMC {
    static var f : int  //## issuekeys: MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE
    final static var g : int  //## issuekeys: MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE, MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    var nnf1t = sf + nf
    var nnf2 = Errant_InnerClassesTest.sf + outer.nf
    var err =  Errant_InnerClassesTest.this.nf  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

    function m0() {}
    static function m1() {}  //## issuekeys: MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE

    class inner0 {}
    static class inner1 {}  //## issuekeys: MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE
  }

  function NoShadowing() {
    var a = \ a : int -> ""
    var b = new Object() {
      function foo() {
        if (this != b) {  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
          b = this;  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
        }
      }
    } ;
  }

  var x : int
  static var y : int

  class B {
    var w: int
    class C {
      class D {
        var i = outer.outer.outer.x
        var j = i
        var u = Errant_InnerClassesTest.y
        var z = i
        var o = outer.outer.w
      }
    }
  }

  class Foo {

    class Foo {  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND
    }

    class Bar {

      class Bar {    //## issuekeys: MSG_DUPLICATE_CLASS_FOUND
      }
    }
  }

}
