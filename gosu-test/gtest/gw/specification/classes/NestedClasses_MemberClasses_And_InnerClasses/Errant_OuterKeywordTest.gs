package gw.specification.classes.NestedClasses_MemberClasses_And_InnerClasses

uses java.lang.Runnable

class Errant_OuterKeywordTest {
  var n = outer  //## issuekeys: MSG_BAD_IDENTIFIER_NAME

  class A {
    class B {
      class C {  }

      function getC(): C {
        var c : A.B.C = new C()
        var b : B = c.outer
        return c
      }
    }

    function getB(): B {
      return new B()
    }

    function foo() {
      var c = new B().getC()
      var b : B = c.outer
      var a : A = c.outer.outer
    }

    function f1(b : B) {
      var a : A = b.outer
    }

    public class D {
      function f() {
        var d2: D = new Runnable() {
          override function run() {
            var d: D = outer
          }
        }.outer
      }
    }
  }

  static class E {
    static function f() {
      var x = outer  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }
  }

  static class F {
     class G {
      var x = outer
    }
  }

  public interface H {
    class I {
      var x = outer  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }
  }
}