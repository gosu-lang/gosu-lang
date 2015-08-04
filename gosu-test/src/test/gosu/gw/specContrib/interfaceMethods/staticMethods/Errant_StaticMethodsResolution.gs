package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticMethodsResolution {

  interface IA {
    static function foo() {
    }
  }

  class MyClass3 implements IA {
    function test() {
      foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      MyClass3.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

      var instance1: MyClass3
      instance1.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

      IA.foo()
      super[IA].foo()
    }
  }
}