package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest

class Errant_VarianceOfTypeParametersTest {
  static class B {}
  static class C extends B {}

  static class I0<out T> {
    function retrieve() : T {
      var t : T
      return t
    }

    function put(t : T) {

    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }
  static class I1<in T> {
    function retrieve() : T {
      var t : T
      return t
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function m0() {
      var i : Fun<T, T, T>
    }
    function put(t : T) {

    }
  }
  static class I2<in out T> {
    function retrieve() : T {
      var t : T
      return t
    }

    function put(t : T) {

    }
  }

  static class Fun<in A, out R, in out Z> {  }


  static class I3a<out T>  extends Fun<T,String, String>{ }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  static class I3b<out T>  extends Fun<String, T , String>{ }
  static class I3c<out T>  extends Fun<String, String, T>{ }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

  static class I4a<in T>  extends Fun<T,String, String>{ }
  static class I4b<in T>  extends Fun<String, T , String>{ }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  static class I4c<in T>  extends Fun<String, String, T>{ }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

  static class I5a<in out T>  extends Fun<T,String, String>{ }
  static class I5b<in out T>  extends Fun<String, T , String>{ }
  static class I5c<in out T>  extends Fun<String, String, T>{ }

  static class I6<in T> {
    var f0 : Fun<String, String , T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var f1 : Fun<T, String , String>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var f2 : Fun<String, T , String>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I7<out T> {
    var f0 : Fun<String, String , T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var f1 : Fun<T, String , String>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var f2 : Fun<String, T , String>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I8<in out T> {
    var f0 : Fun<String, String , T>
    var f1 : Fun<T, String , String>
    var f2 : Fun<String, T , String>
  }

  static class I9<in T> {
    function g0() : Fun<T, String, String> {
      return null
    }

    function g1() : Fun<String, T, String> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function g2() : Fun<String, String, T> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I10<out T> {
    function g0() : Fun<T, String, String> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function g1() : Fun<String, T, String> {
      return null
    }

    function g2() : Fun<String, String, T> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I11<in out T> {
    function g0() : Fun<T, String, String> {
      return null
    }

    function g1() : Fun<String, T, String> {
      return null
    }

    function g2() : Fun<String, String, T> {
      return null
    }
  }

  static class I12<in T> {
    function s0(x : Fun<T, String, String>) {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function s1(x : Fun<String, T, String>) {}

    function s2(x : Fun<String, String, T>) {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I13<out T> {
    function s0(x : Fun<T, String, String>) {}

    function s1(x : Fun<String, T, String>) {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function s2(x : Fun<String, String, T>) {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class I14<in out T> {
    function s0(x : Fun<T, String, String>) {}

    function s1(x : Fun<String, T, String>) {}

    function s2(x : Fun<String, String, T>) {}
  }

  function testBasicVarianceTypeParam() {
    var ic0 : I0<C>
    var ib0 : I0<B>

    ic0 = ib0  //## issuekeys: MSG_TYPE_MISMATCH
    ib0 = ic0

    var ic1 : I1<C>
    var ib1 : I1<B>

    ic1 = ib1
    ib1 = ic1  //## issuekeys: MSG_TYPE_MISMATCH

    var ic2 : I2<C>
    var ib2 : I2<B>

    ic2 = ib2  //## issuekeys: MSG_TYPE_MISMATCH
    ib2 = ic2  //## issuekeys: MSG_TYPE_MISMATCH
  }
}