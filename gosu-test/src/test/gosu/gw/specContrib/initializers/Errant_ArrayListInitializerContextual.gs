package gw.specContrib.initializers

uses java.io.Serializable
uses java.lang.Cloneable
uses java.lang.Comparable

class Errant_ArrayListInitializerContextual {

  interface IA extends Serializable, Comparable<IA> {}
  interface IA2 extends Serializable, Comparable<IA> {}

  interface IC extends Serializable, Cloneable {}
  interface IE extends Comparable<IE>, Cloneable {}

  function foo<T>(p1: T, p2: T, p3: T): T {
    return null
  }

  function hello2() {
    var p1: IA
    var p2: IC
    var p3: IE

    var x11a = {p1, p1}
    //IDE-1855
    var x11b: List<Serializable & Comparable<Object>> = x11a
    var x22a = {p2, p2}
    var x22b: List<Serializable & Cloneable> = x22a
    var x33a = {p3, p3}
    //IDE-1855
    var x33b: List<Cloneable & Comparable<Object>> = x33a

    var x12a = {p1, p2}
    var x12b: List<Serializable & Comparable<Object> & Cloneable> = x12a      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CLONEABLE>'

    var p11: IA2
    var a1: Serializable & Comparable = foo(p1, p1, p11)
    var a2: Serializable & Comparable = foo(p1, p11, foo(p1, p1, p11))
    var a3: Serializable & Comparable = foo(p1, p11, foo(p1, p1, foo(p1, p1, p11)))
  }
}