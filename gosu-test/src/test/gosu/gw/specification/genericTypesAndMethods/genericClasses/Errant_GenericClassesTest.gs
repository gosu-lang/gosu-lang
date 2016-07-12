package gw.specification.genericTypesAndMethods.genericClasses

uses gw.specification.genericTypesAndMethods.genericClasses.tmp.*

class Errant_GenericClassesTest {
  interface I<T1> {}
  static class C0<T1, T2> extends T1 implements  I<T1> { }  //## issuekeys: MSG_CANNOT_EXTEND_FINAL_TYPE, MSG_NO_ENCLOSING_INSTANCE_IN_SCOPE
  static class C1<T1, T2> implements I<T1> {
    var f0 : T1
    static var f1 : T2  //## issuekeys: MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT

    function m0(a : T1, b : T2) {}
    static function m1(a : T1, b : T2) {}  //## issuekeys: MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT, MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT
  }

  static class C2<T1>  {
    var f0 : T1
    static class C3<T2> {
      var f1 : T1  //## issuekeys: MSG_INVALID_TYPE
      var f2 : T2
    }
  }

  static class C4<T1>  {
    var f0 : T1
    class C5<T2> {
      var f1 : T1
      var f2 : T2
    }
  }

  class C6<T1>  {
    var f0 : T1
    class C7<T2> {
      var f1 : T1
      var f2 : T2
    }
  }

  static class sC {}
  static class sC {}  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND
  static class sZ<T> {}
  static class sZ<U> {}  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND
  static class sV<T, U> {}
  static class sV<U> {}  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  static class SameSignature1<U, V> {
    function m(a : U) {}
    function m(a : U) {}  //## issuekeys: MSG_FUNCTION_ALREADY_DEFINED
  }

  static class SameSignature2<U, V> {
    function m(a : U) {}
    function m(a : V) {}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD
  }

  function testAccessibility() {
    new X<String>()  //## issuekeys: MSG_CTOR_HAS_XXX_ACCESS, MSG_TYPE_HAS_XXX_ACCESS
    new X<X>()  //## issuekeys: MSG_CTOR_HAS_XXX_ACCESS, MSG_TYPE_HAS_XXX_ACCESS, MSG_TYPE_HAS_XXX_ACCESS
    new Y()
    new C4<X>()  //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
    new C4<Y>()
  }

  static class iC<T> extends iB<T> implements iI<T> {}
  static class iB<T> {}
  static interface iI<T> {}

  static class Animal {}
  static class Dog extends Animal {}
  function testCompatibility() {
    var c = new iC<Dog>()
    var b : iB<Dog> = c
    var ii : iI<Dog> = c
    var c2 : iC<Animal> = c
    var b2 : iB<Animal> = c
    var ii2 : iI<Animal> = c
    var x = new iC<Animal>()
    c = x  //## issuekeys: MSG_TYPE_MISMATCH
    c2 = x
  }
}