package gw.specification.classes.Anonymous_Classes

class Errant_AnonymousClassesTest {
  var f0 : int
  final var f1 : int = 0
  var a : A

  var a2 = new A(1) { function bar() {
    var z = f0
    z = f1
  } }

  class A {
    public var x : int = 8

    construct(y : int) {
      var z = f0
      z = f1
      x = y
    }
  }

  interface B {
    function foo()
  }

  class A2 {}

  function noOwnConstructor() {
    new B() {
      override function foo() {
      }
      construct() {}  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT
      construct(i : int) {}  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT
    }
    new A2() { construct() {}  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT
              construct(i : int) {}  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT
            }
  }
  function m0() {
    a = new A(8) {
      public var y : int = 1

      function sumXY() : A {
        var z = f0
        z = f1
        x+=y
        return this
      }
    }
  }

  function m1() {
    new A(1) {
      construct() {   }  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT, MSG_NO_DEFAULT_CTOR_IN
    }
  }

  function m2() {
    new B() {
      override function foo() {
        var z = f0
        z = f1
      }
    }
  }

  function m3() {
    new B(1) {  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS
      override function foo() {
      }
    }
  }

  function noHidingX( x : Object) {
    new A(1) {
      function sumXY() : A {  x+=1
        return this
      }
    }
  }

  class C {
    construct(p : int) {
      a = new A(p) {
        public var y : int = p
        function sumXY() : A {  x+=y
                                var z = f0
                                z = f1
                                return this
                             }
      }.sumXY()
    }
 }

  static class D {
     construct () {
          new A(1) {}  //## issuekeys: MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE
        }
     }
}
