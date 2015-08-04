package gw.specContrib.interfaceMethods.defaultMethods

uses java.lang.Integer
uses java.lang.Long
uses java.util.ArrayList

class Errant_DefaultMethodsGenerics {
  //Parameterized Interface
  interface IParent<T> {
    function foo() {
    }
  }

  class MyImpl implements IParent<String> {
    function test() {
      //IDE-2581 - OS Gosu Issue
      super[IParent<Integer>].foo()      //## issuekeys: TYPE PARAMETERS ARE NOT ALLOWED IN 'SUPER' EXPRESSION
    }
  }


  ////////////////////////////////
  interface I<T> {
    function fun(p: T) {
    }
  }

  class C implements I<String> {
    function test(o: Object) {
      //Expected error because 'o' isn't String
      fun(o)      //## issuekeys: 'FUN(JAVA.LANG.STRING)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSGENERICS.I' CANNOT BE APPLIED TO '(JAVA.LANG.OBJECT)'
      super[I].fun(o)      //## issuekeys: 'FUN(JAVA.LANG.STRING)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSGENERICS.I' CANNOT BE APPLIED TO '(JAVA.LANG.OBJECT)'
    }
  }

  ////////////////////////////////
  interface I2<T extends java.lang.Number> {
    function foo(p: T) {
    }
  }

  class C2 implements I2<Integer> {
    function test(o: Integer) {
      foo(o)
      super[I2].foo(o)
    }

    function test(long1: Long) {
      foo(long1)      //## issuekeys: 'FOO(JAVA.LANG.INTEGER)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSGENERICS.I2' CANNOT BE APPLIED TO '(JAVA.LANG.LONG)'
      super[I2].foo(long1)      //## issuekeys: 'FOO(JAVA.LANG.INTEGER)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSGENERICS.I2' CANNOT BE APPLIED TO '(JAVA.LANG.LONG)'
    }
  }

  ////////////////////////////////
  interface I3<T> {
    function foo(p: T) : T { return null}
  }

  class C3 implements I3<ArrayList<Integer>> {
    function test(i: Integer) {
      var x : ArrayList = foo({i})
      var x2 : ArrayList<Integer> = foo({i})
      var x3 : ArrayList<String> = foo({i})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'
      var x4 : ArrayList = super[I3].foo({1, 2, i})
      var x5 : ArrayList<Integer> = super[I3].foo({1, 2, i})
      var x6 : ArrayList<String> = super[I3].foo({1, 2, i})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'
    }
  }


}