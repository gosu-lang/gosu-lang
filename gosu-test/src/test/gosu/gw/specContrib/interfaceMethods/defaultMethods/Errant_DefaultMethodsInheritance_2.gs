package gw.specContrib.interfaceMethods.defaultMethods

uses java.lang.Cloneable

class Errant_DefaultMethodsInheritance_2 {
  interface IA {
    function foo() {}
  }

  class A{
    function foo() {}
  }
  class B extends A implements IA {
    function test() {
      foo()
      super[IA].foo()
    }
  }

  class C1 implements Cloneable {
    function clone(): Object {
      //This will be fixed in Parser with IDE-2282
      return super[Cloneable].clone()
    }
  }

  class C2 implements Cloneable {
    function clone(): Object {
      return super[Object].clone()
    }
  }
  class C3 implements Cloneable {
    function clone(): Object {
      return super.clone()
    }
  }

}
