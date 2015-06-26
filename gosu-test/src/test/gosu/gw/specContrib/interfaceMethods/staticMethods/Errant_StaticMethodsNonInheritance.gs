package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticMethodsNonInheritance {
  interface IA {
    static function foo() {
    }

    static function bar() {
    }
  }

  interface IB extends IA {
    static function bar() {
    }
  }

  class MyClass implements IB {
    function test() {
      IA.foo()
      IA.bar()
      IB.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      IB.bar()

      MyClass.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      MyClass.bar()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
    }
  }

  //
  interface AA {
    static function foo() {
    }
  }

  interface BB extends AA {
    //IDE-2608 Parser issue
    static function foo(): int {
      return 1;
    }
  }

}