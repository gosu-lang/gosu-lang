package gw.specification.expressions.objectInitializers

uses java.lang.*
uses java.util.*

class Errant_ObjectInitializersTest {
  function testBasic() {
    var x0 : C0 = new C0() {:a = 1, :b = 2 }
    var x1 : C0 = new C0() {:a = 1 }
    var x2 : C0 = new C0() {a = 1 }  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var x3 : C0 = new C0() {:a }  //## issuekeys: MSG_EQUALS_FOR_INITIALIZER_EXPR, MSG_SYNTAX_ERROR
    var x8 : C0 = new C0() {:c = 1 }
    var x9 : C0 = new C0() {:a = 1, :a = 1 }  //## issuekeys: MSG_REDUNTANT_INITIALIZERS
    var x10 : C0 = new C0() {:a = 1,, :b = 1 }  //## issuekeys: MSG_EXPECTING_NAME_VALUE_PAIR
    var x11 : C3 = new C3() { :f = 1 }  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var x12 : C0 = new C0() { ::a = 1 }  //## issuekeys: MSG_ONLY_ONE_COLON_IN_INITIALIZERS
    var x14 : C1 = new C1(1, 2) { :a = 1, :d = 1  }  //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE

    var x4 : C0 = new C0() { }

    var x5 : C1 = new C1(1, 2) { }
    var x6 : C1 = new C1() { }  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS

    var x7 : C1 = new C1(1, 2) { :a = 5}
    var x16 : C4 = new C4() {:a = 1, :b = new C4() { :a = 2 } }

    var x17 = new Integer[]{
        1,
        ""    //## issuekeys: MSG_TYPE_MISMATCH
    }

    var x18 = new ArrayList<Integer>(){
        "a",           //## issuekeys: MSG_TYPE_MISMATCH
        23,
        1.1            //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    }

    var x19 = new C0() { print("?") }  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
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