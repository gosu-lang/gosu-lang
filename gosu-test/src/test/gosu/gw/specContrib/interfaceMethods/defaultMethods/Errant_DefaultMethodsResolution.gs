package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsResolution {
  interface MyInterface {
    function foo() {
    }
  }
  class MyClass implements MyInterface {
    function test() {
      foo()
      this.foo()
      super[MyInterface].foo()
      new MyClass().foo()

      //Error expected in the following cases
      MyClass.foo()      //## issuekeys: NON-STATIC METHOD 'FOO()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      MyInterface.foo()      //## issuekeys: NON-STATIC METHOD 'FOO()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
    }
  }

  interface MyInterface22 extends MyInterface {
    function test() {
      foo()
      this.foo()
      super[MyInterface].foo()

      super.foo()      //## issuekeys: UNSPECIFIED SUPER REFERENCE IS NOT ALLOWED IN EXTENSION METHOD

    }
  }


}