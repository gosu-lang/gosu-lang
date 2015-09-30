package gw.specification.genericTypesAndMethods.howCanTypeParametersBeUsed

class Errant_HowCanTypeParametersBeUsedTest {
  static class H<T> {
    var f : T
    static var sf : T  //## issuekeys: MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT
    var f2 : List<T> = new ArrayList<T>()

    interface d {
      function m2(a : T)  //## issuekeys: MSG_INVALID_TYPE
    }
    class e {
      function m2(a : T) {}
    }

    static class p {
      function m2(a : T) {}  //## issuekeys: MSG_INVALID_TYPE
    }

    construct() {
      var v : T
    }
    function m0(a : T) : T { return null }
  }

  static class Y<T> {
    function array() : T[] {
      return new T[1];
    }

    function stringTypeIs() : boolean  {
      return "foo" typeis T
    }

    function stringCast() : T {
      return "" as T
    }

    function typeOfT() : Type<T> {
      return T.Type
    }

    function newInstance() : T {
      return new T()
    }

    function typeOfopT() : Type<T> {
      return typeof T
    }

    function typeOfopY() : Type<Y<T>> {
      return typeof Y<T>
    }
  }

  function testTypeParametersUsage() {
    var y = new Y<String>()
    var strs : String[] = y.array()
    strs[0] = "Hello"
    try {
      y.stringCast()
    } catch( e : ClassCastException) {  }

    try {
      new Y<Integer>().stringCast()

    } catch( e : ClassCastException) {  }
  }

}