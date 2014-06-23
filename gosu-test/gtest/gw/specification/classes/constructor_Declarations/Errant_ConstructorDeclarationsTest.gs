package gw.specification.classes.constructor_Declarations

uses java.util.LinkedList
uses java.util.Objects

class Errant_ConstructorDeclarationsTest {
  construct(x : int, y : float) : int {  }  //## issuekeys: MSG_NO_TYPE_AFTER_CONSTRUCTOR

  construct(x : int) {
    this(1)  //## issuekeys: MSG_RECURSIVE_CONSTRUCTOR
  }
  construct(x : int, y : int) {
    this(1)
    var k = 0
  }

  construct(c : double) {
    this(c as int)
  }

  construct(x : float) {
    var k = 0
    this(1)  //## issuekeys: MSG_NO_SUCH_FUNCTION
  }

  construct() {
    this()  //## issuekeys: MSG_RECURSIVE_CONSTRUCTOR
  }
  private construct(x : Object) {
    return
  }

  construct(x : LinkedList) {
    return 0  //## issuekeys: MSG_UNEXPECTED_TOKEN
  }

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

  class D extends A {
    construct() {
      var c = 0
      super(2.3)  //## issuekeys: MSG_NO_SUCH_FUNCTION
    }
  }

}
