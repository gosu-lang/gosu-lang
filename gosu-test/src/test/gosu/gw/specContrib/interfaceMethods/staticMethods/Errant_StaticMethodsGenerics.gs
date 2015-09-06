package gw.specContrib.interfaceMethods.staticMethods

uses java.lang.Integer
uses java.util.ArrayList

class Errant_StaticMethodsGenerics {
  interface IA {
    static function foo1(arrayList: ArrayList) {
    }

    static function foo2(arrayList: ArrayList<Integer>) {
    }

    static function foo3<T extends java.lang.Number>(arrayList: ArrayList<T>) {
    }
  }

  class MyClass implements IA {
    function test() {
      IA.foo1(new ArrayList())
      IA.foo1(new ArrayList<Object>())
      IA.foo1(new ArrayList<Integer>())
      IA.foo1({1, 2, 3})
      IA.foo1({42, 32.5, 42.5})
      IA.foo1({1, "string", 42.5, new Object()})

      IA.foo2(new ArrayList())
      IA.foo2(new ArrayList<Object>())      //## issuekeys: 'FOO2(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICMETHODSGENERICS.IA' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>)'
      IA.foo2(new ArrayList<Integer>())
      IA.foo2({1, 2, 3})
      IA.foo2({42, 32.5, 42.5})      //## issuekeys: 'FOO2(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICMETHODSGENERICS.IA' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>)'
      IA.foo2({1, "string", 42.5, new Object()})      //## issuekeys: 'FOO2(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICMETHODSGENERICS.IA' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>)'

      IA.foo3(new ArrayList())
      IA.foo3(new ArrayList<Object>())      //## issuekeys: INFERRED TYPE 'JAVA.LANG.OBJECT' FOR TYPE PARAMETER 'T' IS NOT WITHIN ITS BOUND; SHOULD EXTEND 'JAVA.LANG.NUMBER'
      IA.foo3(new ArrayList<Integer>())
      IA.foo3({1, 2, 3})
      IA.foo3({42, 32.5, 42.5})
      IA.foo3({1, "string", 42.5, new Object()})      //## issuekeys: INFERRED TYPE 'JAVA.LANG.OBJECT' FOR TYPE PARAMETER 'T' IS NOT WITHIN ITS BOUND; SHOULD EXTEND 'JAVA.LANG.NUMBER'


      MyClass.foo1(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      MyClass.foo2(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      MyClass.foo3(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY

      var instance1: MyClass
      instance1.foo1(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      instance1.foo2(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      instance1.foo3(new ArrayList<Integer>())      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
    }

    function bar(a: ArrayList) {
    }

    function hello() {
      bar(new ArrayList<Integer>())
    }
  }

}