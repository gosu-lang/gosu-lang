package gw.specification.genericTypesAndMethods.constraintsOnTypeParameters

class Errant_ConstraintsOnTypeParametersTest {
  static class A0 {}
  static class B0 extends A0 {}
  static class C0<T1 extends A0, T2> {}

  final static class A1 {}
  static class C1<T1 extends A1, T2> {}

  static interface A2 {}
  static class C2<T1 extends A2, T2> {}

  static structure A3 {}
  static class C3<T1 extends A3, T2> {}

  static class C4<T1 extends > {}    //## issuekeys: MSG_EXPECTING_TYPE_NAME

  static class C5<T1 extends T1> {}  //## issuekeys: MSG_CYCLIC_INHERITANCE
  static class C6<T1, T2 extends  T1> {}
  static class C62<T1, T2 extends  T2> {}  //## issuekeys: MSG_CYCLIC_INHERITANCE
  static class C7<T1, T2 extends  T3, T3> {}

  static class B<U> {}
  static class Z extends B<Z> {}
  static class Z2 extends B<String> {}
  static class C8<T1 extends B<T1>> {}
  static class C9<T1, T2 extends B<T1>> {}
  static class C10<T1, T2 extends B<T2>> {}
  static class C11<T1, T2 extends B<T3>, T3> {}

  static class Z3 extends C12<Z3> {}
  static class Z4 extends C13<String, Z4> {}
  static class Z5 extends C14<Z5, Z5> {}

  static class C12<T1 extends C12<T1>> {}
  static class C13<T1, T2 extends C13<T1, T2>> {}
  static class C131<T1, T2 extends C13<Object, Object>> {}  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  static class C14<T1, T2 extends C14<T2, T2>> {}
  static class C15<T1, T2 extends C15<T3, T2, T3>, T3> {}

  static class M extends Integer[] {}  //## issuekeys: MSG_CANNOT_EXTEND_ARRAY, MSG_CANNOT_EXTEND_FINAL_TYPE
  static class C16<T1 extends Number> {}
  static class C17<T1 extends Object[]> {}
  static class C171<T1 extends int[]> {}

  static class F extends A0 implements A2 {}
  static class C18<T1 extends A0 & A1> {}  //## issuekeys: MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static class C19<T1 extends A0 & A2> {}
  static class C20<T1, T2 extends A0 & T2> {}  //## issuekeys: MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static class C21<T1, T2 extends T2 & A0> {}  //## issuekeys: MSG_ONLY_ONE_TYPE_VARIABLE, MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static class C22<T1, T2 extends T1 & A2> {}  //## issuekeys: MSG_ONLY_ONE_TYPE_VARIABLE

  function testUsage() {
    new C0<A0, String>()
    new C0<B0, String>()
    new C0<Object, String>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C1<A1, Integer>()
    new C1<A0, Integer>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C3<A3, Integer>()
    new C3<A0, Integer>()   //## KB(PL-34008)
    new C6<A0, B0>()
    new C8<Z>()
    new C9<Z, Z>()
    new C9<String, Z2>()
    new C10<Z, Z>()
    new C10<String, Z>()
    new C12<Z3>()
    new C13<Integer, Z4>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C13<String, Z4>()
    new C14<Integer, Z5>()
    new C16<Number>()
    new C16<Integer>()
    new C17<Integer[]>()
    new C17<int[]>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var c171 : C17<Object[]> = new C17<Integer[]>()
    new C171<int[]>()
    new C171<long[]>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C171<short[]>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C19<A0>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C19<A2>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    new C19<F>()
    new C22<A0, F>()   //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  }
}
