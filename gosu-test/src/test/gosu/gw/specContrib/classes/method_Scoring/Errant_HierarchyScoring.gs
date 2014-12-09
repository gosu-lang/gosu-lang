package gw.specContrib.classes.method_Scoring

uses java.lang.Integer

class Errant_HierarchyScoring {
  class A {}
  class B extends A {}
  class C extends B {}

  function f(a: A, b: B) {}
  function f(b: B, a: A) {}

  function d( n: int ) {}
  function d( n: java.lang.Number ) : A { return null }

  function main(a: A, b: B, c: C) {
    f(a, b);  // no error - good
    f(c, c);  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    f(b, c);  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

    var A = d( new Integer(1) )
  }
}