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


  class Base<T, U extends Base<T,U>> {}
  class Derived<S, R> extends Base<S, Derived<S,R>> {}
  class Derived2<S, R> extends Base<S, Derived2<R,R>> {}  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
}