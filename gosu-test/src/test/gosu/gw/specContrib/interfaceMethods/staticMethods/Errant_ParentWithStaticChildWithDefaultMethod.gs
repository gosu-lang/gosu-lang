package gw.specContrib.interfaceMethods.staticMethods

class Errant_ParentWithStaticChildWithDefaultMethod {
  interface IA {
    static function bar() {
    }
  }

  interface IB extends IA {
    function bar() {
    }
  }
}