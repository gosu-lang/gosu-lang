package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticMethodsBasicTest {

  interface IA {
    static function foo() {
    }

    static function foo(s: String) {
    }
  }

  interface IB {
    static function foo() {
    }
  }


  //Case#1
  class Impl1 implements IA, IB {
  }

  //Case#2
  class Impl2 implements IA {

    function test() {
      foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      this.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

      IA.foo()

      foo("London")        //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      this.foo("London")   //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

      IA.foo("London")
    }
  }

  //Case#3
  class Impl3 implements IA {
    function foo() {
    }

    function test() {
      foo()
      this.foo()

      IA.foo()
    }
  }
}