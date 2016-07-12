package gw.specContrib.generics

class Errant_GenericMethod {
  public interface B extends java.io.Serializable, java.lang.Comparable<B> {}
  public abstract class C implements B {}
  var b: Boolean
  var c: C
  var col = {b, c}
  // IDE-1836
  var colFL = {#b, #c}

  class A<T> {}
  function foo<T>(p1: A<T>, p2: T) {}

  function test() {
    var a: A<java.math.BigDecimal>
    var d: double
    // IDE-1838
    foo(a, d)
  }

  // IDE-2285
  function foo<U>(o: Object): U {
    var r = o != null ? foo(null) : null
    return r      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'U'
  }

}
