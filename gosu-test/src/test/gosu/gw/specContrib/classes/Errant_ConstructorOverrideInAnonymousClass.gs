package gw.specContrib.classes

uses java.lang.Integer
uses java.util.List

class Errant_ConstructorOverrideInAnonymousClass<T> {
  construct(t: T, i: int = 0) {
  }

  function test1() {
    var x = new Object() {
      construct(s: String) {}  //## issuekeys: INVALID_CONSTRUCTOR_OVERRIDING_IN_ANONYMOUS_CLASS
    }
  }

  function test2() {
    var a1 = new Errant_ConstructorOverrideInAnonymousClass(1) {
      construct(p: Object, i: int) {
        super(null)
      }
    }
    var a2 = new Errant_ConstructorOverrideInAnonymousClass<Integer>(1) {
      construct(p: Object, i: int) {  //## issuekeys: INVALID_CONSTRUCTOR_OVERRIDING_IN_ANONYMOUS_CLASS
        super(null)
      }
    }
    var a3 = new Errant_ConstructorOverrideInAnonymousClass<Integer>(1) {
      construct(p: Integer) {  //## issuekeys: INVALID_CONSTRUCTOR_OVERRIDING_IN_ANONYMOUS_CLASS
        super(null)
      }
      }
    var a4 = new Errant_ConstructorOverrideInAnonymousClass<List<Object>>(null) {
      construct(p: List<Integer>, i: int) {  //## issuekeys: INVALID_CONSTRUCTOR_OVERRIDING_IN_ANONYMOUS_CLASS
        super(null)
      }
    }
  }
}
