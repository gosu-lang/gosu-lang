package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticDefaultMethodsResolution_1 {
  //Class implementing interfaces having same name default and static methods
  interface IA3 {
    function foo() {
    }
  }

  interface IB3  {
    static function foo() {
    }
  }

  class MyClass3 implements IA3, IB3 {
    function test() {
      //IDE-2617 Parser issue. Should not be an error
      foo()

      //IDE-2617 Parser issue. Should not be an error
      var instance3 : MyClass3
      instance3.foo()

      IA3.foo()      //## issuekeys: NON-STATIC METHOD 'FOO()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      super[IA3].foo()
      IB3.foo()
      //IDE-2618 - OS Gosu issue, does not show error.
      super[IB3].foo()

      MyClass3.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
    }
  }


}