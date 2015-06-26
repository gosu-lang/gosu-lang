package gw.specContrib.interfaceMethods.defaultMethods

uses java.lang.Integer

class Errant_DefaultMethodsOverloadedMethods {

  ///////1 & 2
  interface MyInterface1 {
    function foo(s: String) {
    }
  }

  interface MyInterface2 {
    function foo(i: Integer) {
    }
  }

  class MyClass12 implements MyInterface1, MyInterface2 {
  }


  //////3 & 4
  interface MyInterface3 {
    function foo(s: String) {
    }
  }

  interface MyInterface4 {
    function foo(s: String) {
    }

    function foo(i: Integer) {
    }
  }

  class MyClass34 implements MyInterface3, MyInterface4 {      //## issuekeys: GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSOVERLOADEDMETHODS.MYCLASS34 INHERITS UNRELATED DEFAULTS FOR FOO(STRING) FROM TYPES GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSOVERLOADEDMETHODS.MYINTERFACE3 AND GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSOVERLOADEDMETHODS.MYINTERFACE4
  }

  //////5 & 6
  class Class55 {
  }

  class Class66 {
  }

  interface MyInterface5 {
    function foo(s: String): Class55 {
      return null
    }
  }

  interface MyInterface6 {
    function foo(i: Integer): Class66 {
      return null
    }
  }

  class MyClass56 implements MyInterface5, MyInterface6 {
    function foo(c: char) {
    }

    function test() {
      var x: Class66 = super[MyInterface6].foo(42)
    }
  }

  ///////7 & 8
  interface MyInterface7 {
    function foo(s: String) {
    }
  }

  interface MyInterface8 {
    //    function foo()
    function foo(i: Integer) {
    }
  }

  class MyClass78 implements MyInterface7, MyInterface8 {
    function foo(c: char) {
    }
  }
}