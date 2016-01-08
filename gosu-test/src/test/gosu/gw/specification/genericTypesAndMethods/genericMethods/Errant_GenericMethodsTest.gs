package gw.specification.genericTypesAndMethods.genericMethods

class Errant_GenericMethodsTest {
  static class C1 {
    function m<T1 extends A0>(a : T1) : T1 {
      return null
    }
  }

  static class C4 {
    function m<T1>(a : T1) : T1 {
      return null
    }
  }

  static class C2<T2, T3> {
    function m<T1>(a : T1, b : T2) : T2 {
      var c : T2
      return null
    }

    function m1<T3>() {  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }
    static function m2<T1>(a : T1, b : T2) : T2 {  //## issuekeys: MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT, MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT
      var c : T2  //## issuekeys: MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT
      return null
    }
  }

  static structure S1 {
    function m<T1>(a : T1) : T1 {
      return null
    }
  }

  static interface I1 {
    function m<T1>(a : T1) : T1 {
      return null
    }
  }

  function m0<T1>(a : T1) : T1 {
    return null
  }

  static class A0 {}
  static class C0 {}
  static class B0 extends A0 {}
  function m1<T1 extends A0>(a : T1) : T1 {
    var b : T1
    m1(new A0())
    m1(new B0())
    m1(new C0())  //## issuekeys: MSG_TYPE_MISMATCH
    return null
  }

  function fun() {}
  function fun<Z>() {}  //## issuekeys: MSG_FUNCTION_ALREADY_DEFINED
  function fun<Z, Y>() {}  //## issuekeys: MSG_FUNCTION_ALREADY_DEFINED

  static class A1 extends C1 {
    override function m<T1 extends C0>(a : T1) : T1 {  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE
      return null
    }
  }

  static class A2 extends C1 {
    override function m<T1 extends A0>(a : T1) : T1 {
      return null
    }
  }

  static class A3 extends C1 {
    override function m<HELLO extends A0>(a : HELLO) : HELLO {
      return null
    }
  }


  static class A4 extends C4 {
    function m0<T1>(a : T1) : T1 {
      return null
    }

    static function m1<T1>(a : T1) : T1 {
      return null
    }

    function m2<T1, T2>(a : T1, b : T2) {
    }

    function tesCall() {
      m("")
      m<String>("")
      m<Integer>("")  //## issuekeys: MSG_TYPE_MISMATCH
      this.m0<String>("")
      super.m<String>("")
      A4.m1<String>("")
      m2<String, Integer>("", 1)
      m2<String>("", 1)  //## issuekeys: MSG_WRONG_NUM_OF_ARGS, MSG_NO_SUCH_FUNCTION
    }
  }
}

