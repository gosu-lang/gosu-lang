package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsAnonymousClass {

  interface IA {
    function foo() {}
  }
  class MyClass implements IA {
    function test() {
      var x = new MyClass() {
        function innerTest() {
          foo()
          super.foo()
          super[MyClass].foo()
          super[IA].foo()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSANONYMOUSCLASS.IA' IS NOT A DIRECT SUPERTYPE
        }
      }
    }
  }

}