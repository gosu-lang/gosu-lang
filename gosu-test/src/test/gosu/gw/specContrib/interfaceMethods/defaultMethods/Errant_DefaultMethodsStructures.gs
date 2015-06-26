package gw.specContrib.interfaceMethods.defaultMethods

uses java.lang.Integer
uses java.util.ArrayList

class Errant_DefaultMethodsStructures {
  interface MyInterface111 {
    function age(years: int): int {
      return years
    }
  }

  structure MyStructure111 {
    function age(years: int): int {
      return years * 7
    }
  }


  class MyClass111 implements MyInterface111, MyStructure111 {      //## issuekeys: GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSSTRUCTURES.MYCLASS111 INHERITS UNRELATED DEFAULTS FOR AGE(INT) FROM TYPES GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSSTRUCTURES.MYINTERFACE111 AND GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSSTRUCTURES.MYSTRUCTURE111
  }


  ///structure extending interface
  interface MyInterface222 {
    function foo() {
    }
  }

  structure MyStructure222 extends MyInterface222 {
    function foo(i: Integer) {
    }
  }

  class MyClass222 implements MyStructure222 {
  }

  ///////////////////Check Structural Typing - covariance/contravariance
  structure IA {
    function foo(i: ArrayList<Integer>): ArrayList {
      return null
    }
  }

  class CA {
    function foo(i: ArrayList): ArrayList<Integer> {
      return null
    }

    function test() {
      var x: IA = new CA()
    }
  }
}