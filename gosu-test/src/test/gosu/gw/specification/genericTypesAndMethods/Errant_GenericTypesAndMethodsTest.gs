package gw.specification.genericTypesAndMethods

class Errant_GenericTypesAndMethodsTest {

  static class C<T1> {}
  static interface IC {}
  static structure SC {}

  function testGenericClass() {
    var v0 : C<Integer>
    var v1 : C<String>
    var v2 : C<C<String>>
    var v3 : C<String[]>
    var v4 : C<IC>
    var v5 : C<SC>
    var v6 : C<void>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v7 : C<int>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v8 : C<block(a : int) : int>
    var v9 : C<dynamic.Dynamic>
  }

  static interface I<T1> {}

  function testGenericInterface() {
    var v0 : I<Integer>
    var v1 : I<String>
    var v2 : I<I<String>>
    var v3 : I<String[]>
    var v4 : I<IC>
    var v5 : I<SC>
    var v6 : I<void>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v7 : I<int>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v8 : I<block(a : int) : int>
    var v9 : I<dynamic.Dynamic>
  }

  static interface S<T1> {}

  function testGenericStructure() {
    var v0 : S<Integer>
    var v1 : S<String>
    var v2 : S<S<String>>
    var v3 : S<String[]>
    var v4 : S<IC>
    var v5 : S<SC>
    var v6 : S<void>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v7 : S<int>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
    var v8 : S<block(a : int) : int>
    var v9 : S<dynamic.Dynamic>
  }

  static class OuterClass<T> {
    class NonstaticInnerClass<T> {}  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    static class StaticInnerClass<T> {}
  }
}