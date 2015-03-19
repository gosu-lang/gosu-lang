package gw.specContrib.generics

class Errant_GenericBounds {
  static class A<T> { }

  static class B<T extends A<T>> { }

  function testBounds() {
    //see IDE-1937
    var b = new B<A<String>>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  }
}