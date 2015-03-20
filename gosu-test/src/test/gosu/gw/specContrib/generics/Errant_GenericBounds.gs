package gw.specContrib.generics

class Errant_GenericBounds {
  static class A<T> { }

  static class B<T extends A<T>> { }

  function testBounds() {
    //see IDE-1937
    var b = new B<A<String>>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  }

  static class AClass {}
  static class BClass {}
  static class DataBuilder<T, S extends DataBuilder<T, S>> {}
  static class CBuilder extends DataBuilder<AClass, CBuilder> {}
  //## IDE-1861
  static class DBuilder extends DataBuilder<BClass, CBuilder> {}  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
}