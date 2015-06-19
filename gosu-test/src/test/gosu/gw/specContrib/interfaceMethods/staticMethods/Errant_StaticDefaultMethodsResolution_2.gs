package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticDefaultMethodsResolution_2 {
  interface IA2 {
    static function foo() {
    }
  }

  interface IB2 extends IA2 {
    function foo() {
    }

  }

  class MyClass2 implements IB2 {
    function test() {
      foo()

      var instance1: MyClass2
      instance1.foo()

      IA2.foo()
      super[IA2].foo()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICDEFAULTCONFLICT_1.IA2' IS NOT A DIRECT SUPERTYPE
      IB2.foo()            //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      super[IB2].foo()

      MyClass2.foo()            //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

    }
  }


}