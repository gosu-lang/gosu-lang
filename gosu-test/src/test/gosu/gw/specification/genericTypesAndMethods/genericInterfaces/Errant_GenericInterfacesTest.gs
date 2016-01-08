package gw.specification.genericTypesAndMethods.genericInterfaces

class Errant_GenericInterfacesTest {
  static interface I0<T0> { function m0(a : T0) }
  static interface I1<T0> extends I0<T0> { function m1(a : T0) }
  
  static class A0 {}
  static class B0 extends A0 {}
  static interface C0<T1 extends A0, T2> {}

  static class A1 {}
  static interface C1<T1 extends A1, T2> {}

  static interface A2 {}
  static interface C2<T1 extends A2, T2> {}

  static structure A3 {}
  static interface C3<T1 extends A3, T2> {}

  static interface C4<T1 extends > {}    //## issuekeys: MSG_EXPECTING_TYPE_NAME

  static interface C5<T1 extends T1> {}  //## issuekeys: MSG_CYCLIC_INHERITANCE
  static interface C6<T1, T2 extends  T1> {}
  static interface C62<T1, T2 extends  T2> {}  //## issuekeys: MSG_CYCLIC_INHERITANCE
  static interface C7<T1, T2 extends  T3, T3> {}  //## issuekeys: MSG_INVALID_TYPE

  static interface B<U> {}
  static interface Z extends B<Z> {}
  static interface Z2 extends B<String> {}
  static interface C8<T1 extends B<T1>> {}
  static interface C9<T1, T2 extends B<T1>> {}
  static interface C10<T1, T2 extends B<T2>> {}
  static interface C11<T1, T2 extends B<T3>, T3> {}  //## issuekeys: MSG_INVALID_TYPE

  static interface Z3 extends C12<Z3> {}
  static interface Z4 extends C13<String, Z4> {}
  static interface Z5 extends C14<Z5, Z5> {}

  static interface C12<T1 extends C12<T1>> {}
  static interface C13<T1, T2 extends C13<T1, T2>> {}
  static interface C131<T1, T2 extends C13<Object, Object>> {}  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  static interface C14<T1, T2 extends C14<T2, T2>> {}
  static interface C15<T1, T2 extends C15<T3, T2, T3>, T3> {}  //## issuekeys: MSG_INVALID_TYPE, MSG_INVALID_TYPE

  static interface si {}
  static interface  M extends si[] {}  //## issuekeys: MSG_INTERFACE_CANNOT_EXTEND_CLASS
  static interface C16<T1 extends Number> {}
  static interface C17<T1 extends Object[]> {}
  static interface C171<T1 extends int[]> {}

  static class F extends A0 implements A2 {}
  static interface C18<T1 extends A0 & A1> {}  //## issuekeys: MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static interface C19<T1 extends A0 & A2> {}
  static interface C20<T1, T2 extends A0 & T2> {}  //## issuekeys: MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static interface C21<T1, T2 extends T2 & A0> {}  //## issuekeys: MSG_ONLY_ONE_TYPE_VARIABLE, MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE
  static interface C22<T1, T2 extends T1 & A2> {}  //## issuekeys: MSG_ONLY_ONE_TYPE_VARIABLE

  function testUsage() {
    var i0 : C0<A0, String> = null
    var i1 : C0<B0, String> = null
    var i2 : C0<Object, String> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i3 : C1<A1, Integer> = null
    var i4 : C1<A0, Integer> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i5 : C3<A3, Integer> = null
    var i6 : C3<A0, Integer> = null //## KB(PL-34008)
    var i7 : C6<A0, B0> = null
    var i8 : C8<Z> = null
    var i9 : C9<Z, Z> = null
    var i10 : C9<String, Z2> = null
    var i11 : C10<Z, Z> = null
    var i12 : C10<String, Z> = null
    var i13 : C12<Z3> = null
    var i14 : C13<Integer, Z4> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i15 : C13<String, Z4> = null
    var i16 : C14<Integer, Z5> = null
    var i17 : C16<Number> = null
    var i18 : C16<Integer> = null
    var i19 : C17<Integer[]> = null
    var i20 : C17<int[]> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var pp :  C17<Integer[]>
    var i21 : C17<Object[]> = pp
    var i22 : C171<int[]> = null
    var i23 : C171<long[]> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i24 : C171<short[]> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i25 : C19<A0> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i26 : C19<A2> = null  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var i27 : C19<F> = null
    var i28 : C22<A0, F> = null //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    var x0 : I0<String> = null
    var x1 : I0<Object> = x0
    var x2 : I1<String> = null
    x0 = x2
  }
}