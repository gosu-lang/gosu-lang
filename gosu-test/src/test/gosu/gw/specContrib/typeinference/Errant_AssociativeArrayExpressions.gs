package gw.specContrib.typeinference

uses java.lang.Runnable

class Errant_AssociativeArrayExpressions {
  class A {
    function foo() {}
  }
  class B {
    property get Prop(): Object { return new A() }
  }

  function test(b: B) {
    if (b["Prop"] typeis A) {
      b["Prop"].foo()
      b["Prop2"].foo()   //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
  }
}