package gw.specContrib.classes.method_Scoring

uses java.io.Serializable
uses java.lang.Runnable

class Errant_BlockScoring
{
  class BB {}
  class CC {}

  class B {}
  class C extends B {}

  function foo1( m(b : B), r: Serializable ) : BB {
    return null
  }
  function foo1( m(c : C), r: Runnable ) : CC {
    return null
  }

  function testMe() {
    var bb1: BB = foo1(\ b: B -> {}, new Serializable() {} )
    var bb2: BB = foo1(\ c: C -> {}, null)  //## issuekeys: MSG_TYPE_MISMATCH
    var cc: CC = foo1(\ c: C -> {}, null)
  }
}